package akashagarwal45.bunkeasy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MarkAttendance extends FragmentActivity {

    AttendanceDBHandler att_db;     //To manage queries related to attendance database
    SubjectDBHandler sub_db;        //To manage queries related to subject database
    ScheduleDBHandler db;           //To manage queries related to schedule database
    ListView markAttendanceListView;
    String subject;     //To store subject name from list view clicked by user

    Cursor subjectsCursor;

    String clickedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        markAttendanceListView=(ListView)findViewById(R.id.markAttendanceListView);

        db=new ScheduleDBHandler(this,null,null,1);
        sub_db=new SubjectDBHandler(this,null,null,1);
        att_db=new AttendanceDBHandler(this,null,null,1);

        //Creating Caldroid Fragment to display calendar
        CaldroidFragment caldroidFragment=new CaldroidFragment();
        Bundle args=new Bundle();
        Calendar cal=Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH,cal.get(Calendar.MONTH)+1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));

        caldroidFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.layoutCal,caldroidFragment).commit();

        //Setting textview to display date clicked
        final TextView showDate=(TextView)findViewById(R.id.showDateTextView);
        final DateFormat df=new SimpleDateFormat("dd-MM-yyyy");


        //Setting Caldroid listener to display subjects when user clicks on a date
        final CaldroidListener listener=new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

                //Setting textview to date clicked
                String sDate="Date: "+df.format(date);
                showDate.setText(sDate);

                //Retrieving day of week from the date clicked by the user on calendar
                Calendar cal1=Calendar.getInstance();
                cal1.setTime(date);
                int day_index=cal1.get(Calendar.DAY_OF_WEEK)-1;

                String day[]={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

                subjectsCursor=db.subjectsListGivenDayCursor(day[day_index]);

                //Toast.makeText(getApplicationContext(),day[day_index],Toast.LENGTH_SHORT).show();
                if(subjectsCursor.getCount()==0)
                    Toast.makeText(getApplicationContext(),"No classes :)",Toast.LENGTH_SHORT).show();

                    CustomAdapterAttendance adapter = new CustomAdapterAttendance(MarkAttendance.this, subjectsCursor, 0);

                    markAttendanceListView.setAdapter(adapter);

                //Retreiving date of the date selected
                Calendar cal=Calendar.getInstance();
                cal.setTime(date);
                String month,day_of_month;

                if(cal.get(Calendar.MONTH)>=9)
                    month=String.valueOf(cal.get(Calendar.MONTH)+1);
                else
                    month="0"+String.valueOf(cal.get(Calendar.MONTH)+1);

                if(cal.get(Calendar.DAY_OF_MONTH)>9)
                    day_of_month=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                else
                    day_of_month="0"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

                clickedDate=String.valueOf(cal.get(Calendar.YEAR))+month+day_of_month;

                Log.d("Akash",clickedDate);


            }
        };

        //Setting caldroid listener for fragment
        caldroidFragment.setCaldroidListener(listener);


        //Setting onItemClickListener for listView items
        markAttendanceListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final String options[]={"Absent","Present","Holiday/Cancelled"};

                        //Retrieving Subject name from clicked item
                        subject=markAttendanceListView.getItemAtPosition(position).toString();

                        //Creating dialog to record respone from user
                        AlertDialog.Builder alert=new AlertDialog.Builder(MarkAttendance.this);
                        alert.setTitle("Mark Attendance");

                        //Retreiving subject name from textview
                        final String subjectname=((TextView)view.findViewById(R.id.attendanceSubjectNameTextView)).getText().toString();
                        final String hour=((TextView) view.findViewById(R.id.attendanceHourTextView)).getText().toString();
                        final String minute= ((TextView)view.findViewById(R.id.attendanceMinTextView)).getText().toString();

                        //Getting default option as attendance marked
                        int def=2;

                        if(att_db.getResponse(subjectname,clickedDate,hour,minute)!=-1)
                                def=att_db.getResponse(subjectname, clickedDate,hour,minute);

                        //To create alert dialog with radio buttons to mark attendance
                        final int finalDef = def;
                        alert.setSingleChoiceItems(options, def,
                                 new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        att_db.saveAttendanceRecord(subjectname,clickedDate,which,hour,minute);

                                        switch (which) {
                                            case 0:         //Mark absent

                                                if(finalDef ==2)        //Attendance marked from holiday to absent
                                                   sub_db.markAttendance(subjectname,2);
                                                else if(finalDef==1) {    //Attendance marked from present to absent
                                                    sub_db.markAttendance(subjectname,3);
                                                }


                                                Toast.makeText(getApplicationContext(),"Absent marked",Toast.LENGTH_SHORT).show();

                                                break;
                                            case 1:         //mark present

                                                if(finalDef ==2)        //Attendance marked from holiday to present
                                                    sub_db.markAttendance(subjectname,1);
                                                else if(finalDef==0) {    //Attendance marked from absent to present
                                                    sub_db.markAttendance(subjectname,4);
                                                }

                                                Toast.makeText(getApplicationContext(),"Present marked",Toast.LENGTH_SHORT).show();

                                                break;

                                            case 2:         //mark holiday/cancelled

                                                if(finalDef==0)         //Attendance marked from absent to holiday
                                                    sub_db.markAttendance(subjectname,5);
                                                else if(finalDef==1)                   //Attendance marked from present to holiday
                                                    sub_db.markAttendance(subjectname,6);

                                                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                                                break;
                                        }

                                        //To dismiss dialog box when user marks attendance
                                        dialog.dismiss();
                                    }
                                }
                        );

                        //To show alert
                        alert.show();
                    }
                }
        );
    }
}
