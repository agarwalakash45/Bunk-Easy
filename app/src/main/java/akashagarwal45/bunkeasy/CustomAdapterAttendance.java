package akashagarwal45.bunkeasy;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomAdapterAttendance extends CursorAdapter{

    public CustomAdapterAttendance(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.subjects_attendance_list,parent,false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView subName=(TextView)view.findViewById(R.id.attendanceSubjectNameTextView);
        TextView timeSchedule=(TextView)view.findViewById(R.id.attendanceTimeTextView);

        String name=cursor.getString(cursor.getColumnIndex("subname"));
        int hour=cursor.getInt(cursor.getColumnIndex("hour"));
        int minute=cursor.getInt(cursor.getColumnIndex("minute"));

        subName.setText(name);
        if(minute>=0&&minute<=9)
            timeSchedule.setText(String.valueOf(hour)+":0"+String.valueOf(minute));
        else
            timeSchedule.setText(String.valueOf(hour)+":"+String.valueOf(minute));
    }
}
