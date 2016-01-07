package akashagarwal45.bunkeasy;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class Schedule extends FragmentActivity {


    final String [] days={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    final static int DIALOG_ID=0;
    int hour,minute_x;
    ScheduleDBHandler db;
    int day_pos;
    String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Create a database object to store subject schedule in database
        db=new ScheduleDBHandler(this,null,null,1);

        //Log.d("Akash","In Schedule Activity");

        //receiving sbject name from previous activity
        Bundle bundle=getIntent().getExtras();
        subjectName=bundle.getString("subName");

        //Creating List View to display days of a week

        ListView dayListView=(ListView)findViewById(R.id.scheduleListView);

        ListAdapter dayListadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,days);

        dayListView.setAdapter(dayListadapter);

        //setting click listener for list items
        dayListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //method to show dialog to pick time when list item is clicked
                        //Log.d("Akash",String.valueOf(position));
                        day_pos=position;
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }

    //Overriding onCreateDialog to return TimePickerDialog
    @Override
    protected Dialog onCreateDialog(int id) {
        if(DIALOG_ID==id)
            return new TimePickerDialog(this,TimePickerListener,hour,minute_x,true);    //true-24 hour format else 12 hour format
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener TimePickerListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            saveSchedule(hourOfDay,minute);

            Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
        }
    };

    //method to enter subject schedule details into database
    public void saveSchedule(int hour,int minute){
        String day=days[day_pos];
        db.addSchedule(subjectName,hour,minute,day);
    }
}
