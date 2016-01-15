package akashagarwal45.bunkeasy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

import javax.security.auth.Subject;

public class SubjectDetails extends FragmentActivity {

    AttendanceDBHandler db;
    SubjectDBHandler sub_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        db = new AttendanceDBHandler(this, null, null, 1);
        sub_db = new SubjectDBHandler(this,null,null,1);

        //Creating caldroid Fragment to display calendar
        CaldroidFragment calView = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        calView.setArguments(args);

        //Setting calView as calendar
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, calView).commit();

        //Receiving subject name from previous activity
        Bundle bundle = getIntent().getExtras();
        String subName = bundle.getString("SUBJECTNAME");

        //Getting cursor to database wuery
        Cursor c = db.showAttendanceonDate(subName);
        c.moveToFirst();

        while(!c.isAfterLast()){

            String stringDate=c.getString(c.getColumnIndex("date"));

            //Converting date from sql string format to java Date format
            DateFormat df=new SimpleDateFormat("yyyyMMdd");
            Date date=null;
            try {
                date=df.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int response=db.forMultipleClasses(subName,stringDate);

            if(response==0)     //All Absents are marked, color should be set to red
                calView.setBackgroundResourceForDate(R.color.caldroid_light_red,date);
            else if(response==1)            //Some Present and some absent are marked, color should be set to orange
                calView.setBackgroundResourceForDate(R.color.colorAccent,date);
            else if(response==2)                           //All present
                calView.setBackgroundResourceForDate(R.color.caldroid_holo_blue_light,date);
            else
                calView.setBackgroundResourceForDate(R.color.caldroid_white,date);
            c.moveToNext();
        }

        //Displaying current status of attendance in the subject
        TextView status=(TextView)findViewById(R.id.attendanceStatusDescription);

        //Getting subject information
        Cursor cursor=sub_db.subjectDetailsByName(subName);

        cursor.moveToFirst();

        String message;         //To display attendance status for the subject

        int total=cursor.getInt(cursor.getColumnIndex("total")),absent=cursor.getInt(cursor.getColumnIndex("missed"));

        float percent,need;       //need to store classes needed to attend to cover shortage of attendance
        if(total!=0){
            percent=(float)((total-absent)*100)/total;

            //To display message
            if(percent<Subjects.shortPercent) {      //If attendance is less than 75%, then display shortage message

                need=(float)(Subjects.shortPercent*total-100*(total-absent))/(100- Subjects.shortPercent);

                message="Your attendance is short\n\nAttend next " + (int)need + " classes";
                status.setText(message);
                status.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
            else{               //If attendance is good
                message="Your attendance is above "+Subjects.shortPercent+"%";
                status.setText(message);
                status.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }
        }
    }

}