package akashagarwal45.bunkeasy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver{
    private int NOTIF_ID=31273;
    AttendanceDBHandler db;
    @Override
    public void onReceive(Context context, Intent intent) {

        db=new AttendanceDBHandler(context,null,null,1);

        Calendar cal=Calendar.getInstance();

        int year=cal.get(Calendar.YEAR);

        String month,day;

        if(cal.get(Calendar.MONTH)>=9)
            month=String.valueOf(cal.get(Calendar.MONTH)+1);
        else
            month="0"+String.valueOf(cal.get(Calendar.MONTH)+1);

        if(cal.get(Calendar.DAY_OF_MONTH)>9)
            day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        else
            day="0"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH));         //To get date in the format yyyyMMdd


        String date=String.valueOf(year)+month+day;         //Today's date

        Cursor cursor=db.classesOnGivenDate(date);      //Returns the classes for current day

        if(cursor.getCount()>0) {               //If total classes are more than or equal to 1

            long when = System.currentTimeMillis();

            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent resultIntent = new Intent(context, MarkAttendance.class);

            //Pending Intent that will start a new activity
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Building notification
            NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Mark your attendance")
                    .setContentText("You have " + cursor.getCount() + " classes today")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(when);


            notifManager.notify(NOTIF_ID, notifBuilder.build());

        }
    }
}
