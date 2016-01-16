package akashagarwal45.bunkeasy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver{
    private int NOTIF_ID=31273;
    ScheduleDBHandler db;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Akash","In alarm receiver");

        db=new ScheduleDBHandler(context,null,null,1);

        Calendar cal=Calendar.getInstance();

        String [] week={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

        int day=cal.get(Calendar.DAY_OF_WEEK);

        Cursor cursor=db.subjectsListGivenDayCursor(week[day-1]);      //Returns the classes for current day

        Log.d("Akash",""+week[day-1]+"\n"+cursor.getCount());

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
