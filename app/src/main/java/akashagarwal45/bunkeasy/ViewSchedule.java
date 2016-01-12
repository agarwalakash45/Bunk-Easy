package akashagarwal45.bunkeasy;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ViewSchedule extends AppCompatActivity {

    ScheduleDBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        //Log.d("Akash", "I am here");

        db=new ScheduleDBHandler(this,null,null,1);

        //Log.d("Akash", "I am here");

        Cursor c=db.todoCursor();
        Log.d("Akash", "I am here");

        CustomAdapterSchedule scheduleAdapter=new CustomAdapterSchedule(this,c,0);
        Log.d("Akash", "I am here");

        ListView scheduleListView=(ListView)findViewById(R.id.viewScheduleListView);
        Log.d("Akash", "I am here");

        scheduleListView.setAdapter(scheduleAdapter);
        //Log.d("Akash", "I am here");

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.delete_schedule,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //String
        return super.onContextItemSelected(item);
    }
}
