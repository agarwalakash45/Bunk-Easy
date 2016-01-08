package akashagarwal45.bunkeasy;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomAdapterSchedule extends CursorAdapter{

    public CustomAdapterSchedule(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.schedule_list,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView subName=(TextView)view.findViewById(R.id.subjectNameSchedule);
        TextView dayName=(TextView)view.findViewById(R.id.daySchedule);
        TextView timeSchedule=(TextView)view.findViewById(R.id.timeSchedule);

        String name=cursor.getString(cursor.getColumnIndex("subname"));
        int hour=cursor.getInt(cursor.getColumnIndex("hour"));
        int minute=cursor.getInt(cursor.getColumnIndex("minute"));
        String day=cursor.getString(cursor.getColumnIndex("day"));

        subName.setText(name);
        dayName.setText(day);
        if(minute>=0&&minute<=9)
            timeSchedule.setText(String.valueOf(hour)+":0"+String.valueOf(minute));
        else
            timeSchedule.setText(String.valueOf(hour)+":"+String.valueOf(minute));
    }
}
