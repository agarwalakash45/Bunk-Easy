package akashagarwal45.bunkeasy;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class CustomAdapter extends CursorAdapter{

    public CustomAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    //To inflate a new view and return it. No data is bind at this point.
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.subject_list,parent,false);
    }

    @Override
    //used to bind data to a given view
    public void bindView(View view, Context context, Cursor cursor) {
        TextView subjectNameTextView=(TextView) view.findViewById(R.id.subjectNameTextView);
        TextView percentTextView=(TextView)view.findViewById(R.id.percentTextView);
        TextView presentTextView=(TextView)view.findViewById(R.id.presentTextView);
        TextView absentTextView=(TextView)view.findViewById(R.id.absentTextView);
        TextView totalTextView=(TextView)view.findViewById(R.id.totalTextView);

        String name=cursor.getString(cursor.getColumnIndex("subname"));
        int miss=cursor.getInt(cursor.getColumnIndex("missed"));
        int total=cursor.getInt(cursor.getColumnIndex("total"));
        int present=total-miss;

        int percent=(present*100)/(total!=0?total:1);


        subjectNameTextView.setText(name);
        percentTextView.setText(String.valueOf(percent)+"%");
        presentTextView.setText("Present: "+String.valueOf(present));
        absentTextView.setText("Absent: "+String.valueOf(miss));
        totalTextView.setText("Total: "+String.valueOf(total));
    }
}
