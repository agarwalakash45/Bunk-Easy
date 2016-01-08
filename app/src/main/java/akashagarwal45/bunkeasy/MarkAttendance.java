package akashagarwal45.bunkeasy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    SubjectDBHandler sub_db;
    ScheduleDBHandler db;
    ListView markAttendanceListView;
    String subject;     //To store subject name from list view clicked by user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        markAttendanceListView=(ListView)findViewById(R.id.markAttendanceListView);

        db=new ScheduleDBHandler(this,null,null,1);
        sub_db=new SubjectDBHandler(this,null,null,1);

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

                ArrayList<String> subjects=db.subjectsListGivenDay(day[day_index]);

                //Toast.makeText(getApplicationContext(),day[day_index],Toast.LENGTH_SHORT).show();

                if(subjects.isEmpty())
                    Toast.makeText(getApplicationContext(),"No classes :)",Toast.LENGTH_SHORT).show();

                ListAdapter adapter=new ArrayAdapter<String>(MarkAttendance.this,android.R.layout.simple_list_item_1,subjects);

                markAttendanceListView.setAdapter(adapter);

            }
        };

        //Setting caldroid listener for fragment
        caldroidFragment.setCaldroidListener(listener);


        //Setting onItemClickListener for listView items
        markAttendanceListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Retrieving Subject name from clicked item
                        subject=markAttendanceListView.getItemAtPosition(position).toString();

                        //Creating dialog to record respone from user
                        AlertDialog.Builder alert=new AlertDialog.Builder(MarkAttendance.this);
                        alert.setTitle("Mark Attendance");

                        alert.setPositiveButton("PRESENT",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        sub_db.markAttendance(subject,1);            //To mark PRESENT, 1 is passed

                                        Toast.makeText(getApplicationContext(),"Present Marked",Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        alert.setNegativeButton("ABSENT",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        sub_db.markAttendance(subject,0);      //To mark ABSENT, 0 is passed

                                        Toast.makeText(getApplicationContext(),"Absent Marked",Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        alert.show();
                    }
                }
        );
    }
}
