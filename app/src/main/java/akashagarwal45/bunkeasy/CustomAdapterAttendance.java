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
        TextView hourTextView=(TextView)view.findViewById(R.id.attendanceHourTextView);
        TextView minTextView=(TextView)view.findViewById(R.id.attendanceMinTextView);
        String name=cursor.getString(cursor.getColumnIndex("subname"));
        String hour=cursor.getString(cursor.getColumnIndex("hour"));
        String minute=cursor.getString(cursor.getColumnIndex("minute"));

        subName.setText(name);

        String temp1="0"+hour;
        String temp2="0" + minute;

        if(hour.length()==1)
            hourTextView.setText(temp1);
        else
            hourTextView.setText(hour);
        if(minute.length()==1)
            minTextView.setText(temp2);
        else
            minTextView.setText(minute);
    }
}
