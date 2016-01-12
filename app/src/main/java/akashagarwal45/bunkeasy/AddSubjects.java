package akashagarwal45.bunkeasy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddSubjects extends AppCompatActivity {

    ListView subListView;
    SubjectDBHandler db=new SubjectDBHandler(this,null,null,1);
    ScheduleDBHandler sch_db;
    AttendanceDBHandler att_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Checking for Errors
        //Log.d("Test","onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subjects);

        subListView=(ListView)findViewById(R.id.subjectListView);

        sch_db=new ScheduleDBHandler(AddSubjects.this,null,null,1);

        att_db=new AttendanceDBHandler(AddSubjects.this,null,null,1);

        Cursor c=db.todoCursor();       //return cursor to all the records in the subject table

        CustomAdapter customAdapter=new CustomAdapter(this,c,0);        //create a custom adapter by passing context and cursor as parameters

        subListView.setAdapter(customAdapter);      //setting ListView adapter as customAdapter

        //setting up ItemClickListener for each item in ListView
        subListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(AddSubjects.this, SubjectDetails.class);
                        //Log.d("Akash","Starting intent");

                        String subName=((TextView)view.findViewById(R.id.subjectNameTextView)).getText().toString();

                        //Sending subject name through extras
                        intent.putExtra("SUBJECTNAME",subName);
                        startActivity(intent);
                    }
                }
        );

        //registering ListView for floating context menu
        registerForContextMenu(subListView);
    }

    //To set acion bar to the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.action_bar_subject,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            //Starting activity Mark Attendance
            case R.id.mark_attendance:

                Intent intent3=new Intent(AddSubjects.this,MarkAttendance.class);
                startActivity(intent3);
                break;

            case R.id.action_settings:
 //               Log.d("Akash","Settings button pressed");
                Intent intent1=new Intent(AddSubjects.this,SubjectSettings.class);
   //             Log.d("Akash","Settings button pressed");
                startActivity(intent1);
                //Log.d("Akash", "Settings button pressed");
                break;

            case R.id.view_schedule:

                //Intent to start activity View Schedule
                Intent intent2=new Intent(AddSubjects.this,ViewSchedule.class);
                startActivity(intent2);
                break;

            case R.id.about:

                break;
            case R.id.reset:
                //CAlling method to reset all data
               resetData();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Method to reset data
    public void resetData(){
        //Creating Alert Dialog to ask for confirmation
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setMessage("All data will be erased");
        alert.setTitle("\nAre you sure?");
        alert.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Calling method to remove data from all the tables
                        db.deleteAllFromTable();;
                        sch_db.deleteAllFromTable();
                        att_db.deleteAllFromTable();

                        Toast.makeText(getApplicationContext(),"Reset Successful",Toast.LENGTH_SHORT).show();

                        //Starting activity to referesh
                        Intent intent=getIntent();
                        finish();
                        startActivity(intent);

                    }
                }
        );

        alert.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }
        );

        alert.show();
    }


    public void addSubjectButtonClicked(View view){
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Add Subject");

        final EditText input = new EditText(this);
        input.setHint("Enter Subject name");

        alert.setView(input);

        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Subject name can't be empty", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Subjects newSubject = new Subjects(input.getText().toString());
                    final SubjectDBHandler db;
                    db = new SubjectDBHandler(AddSubjects.this, null, null, 1);

                    db.addSubject(newSubject);
                    Toast toast = Toast.makeText(getApplicationContext(), "Subject added!!", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            }
        });

        alert.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(getApplicationContext(), "No subject added", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        alert.show();
    }


    //method to edit subject name when edit option is clicked in context menu
    public void editSubjectName(final String oldSubjectName){
        //To create alert dialog to enter new subject name
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Edit Subject Name");

        //create a edit text field to enter new subject name
        final EditText newSubjectName=new EditText(this);
        newSubjectName.setHint("Subject Name");

        alert.setView(newSubjectName);

        //setting OK button as positive button
        alert.setPositiveButton("OK", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(newSubjectName.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Subject name can't be empty!!",Toast.LENGTH_SHORT).show();
                else {
                    //Updating subject name in subject database
                    db.editSubjectName(newSubjectName.getText().toString(), oldSubjectName);
                    //update subject name in attendance database
                    att_db.updateSubjectName(oldSubjectName,newSubjectName.getText().toString());
                    //Updating subject name in schedule database
                    sch_db.updateSubjectName(oldSubjectName,newSubjectName.getText().toString());
                }
                //Restarting the subjects display activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        //setting negative button as CANCEL Button
        alert.setNegativeButton("CANCEL", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //creating TOAST to display message
                Toast.makeText(getApplicationContext(), "Subject name not changed", Toast.LENGTH_SHORT).show();
            }
        });

        //showing the alert
        alert.show();
    }

    //method to delete subject when delete is clicked in context menu
    public void deleteSubject(final String subjectName){
        //Create alert dialog for delete confirmation
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Are you sure to delete?");

        //setting YES positive button
        alert.setPositiveButton("YES", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteSubject(subjectName);

                att_db.deleteSubject(subjectName);

                sch_db.deleteSubjectFromSchedule(subjectName);
                //Toast to display deletion message
                Toast.makeText(getApplicationContext(), "Subject deleted!!", Toast.LENGTH_SHORT).show();

                //Restarting the subjects display activity
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        //setting NO negative button
        alert.setNegativeButton("NO", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    //To create floating context menu when an item is long pressed
    //It is for single item only
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.context_menu_subject,menu);         //inflating menu layout as floating context menu
    }

    //method to decide action to be taken when a context item is selected
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        String subjectName=((TextView)info.targetView.findViewById(R.id.subjectNameTextView)).getText().toString();

        switch(item.getItemId()){
            case R.id.delete_subject:
                deleteSubject(subjectName);
                break;

            case R.id.schedule:
                //Creating intent and passing subject name to next activity
                Intent intent=new Intent(AddSubjects.this,Schedule.class);
                intent.putExtra("subName",subjectName);
                startActivity(intent);

                break;

            case R.id.edit_subject:
                editSubjectName(subjectName);      //calling method to edit subject name
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onRestart() {

        Intent intent=getIntent();
        finish();
        startActivity(intent);

        super.onRestart();
    }
}