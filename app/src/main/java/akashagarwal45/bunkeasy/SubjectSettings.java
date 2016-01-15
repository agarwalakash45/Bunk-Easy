package akashagarwal45.bunkeasy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

public class SubjectSettings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    AlarmManager alarmManager;
    int ID=32156;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Akash", "I am here!!");
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Log.d("Akash",""+Subjects.shortPercent);

        preferences.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Calendar cal;

        Intent alarmIntent=new Intent(this,AlarmReceiver.class);

        //Pending Intent that will perform a broadcast
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

        if(key.equals("shortage_percent")){         //Setting shortage percentage
            Subjects.shortPercent= Integer.parseInt(sharedPreferences.getString("shortage_percent","75"));
            Log.d("Akash",""+Subjects.shortPercent);
        }

        if(key.equals("time_notification")){            //User has changed time for daily notifications

            String time=sharedPreferences.getString("time_notification","20:00");       //Retreiving time from sharedpreferences
            Log.d("Akash", time);

            Calendar now=Calendar.getInstance();            //Getting current date and time

            cal=Calendar.getInstance();

            String [] pieces=time.split(":");

            int hour=Integer.parseInt(pieces[0]);           //hour set by user

            int minute=Integer.parseInt(pieces[1]);         //minute set by user

            Log.d("Akash", "" + hour + minute);

            cal.set(Calendar.HOUR_OF_DAY, hour);

            cal.set(Calendar.MINUTE, minute);

            cal.set(Calendar.SECOND, 0);

            if(cal.before(now)) //If user set time is expired for today, then add one to the date else notification will be displayed immediately
                cal.add(Calendar.DATE,1);

            Log.d("Akash", "" + cal.getTime());

            //setting repeating alarm that repeats after every day
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        if(key.equals("daily_notification_enable")){
            boolean check=sharedPreferences.getBoolean("daily_notification_enable", false);

            if(!check){             //If daily notifications are disabled

                Log.d("Akash","Alarm Cancelled");

                alarmManager.cancel(pendingIntent);
            }
            else{                   //User enables daily notification, set previous time as alarm time
                String time=sharedPreferences.getString("time_notification","20:00");
                Log.d("Akash", time);

                Calendar now=Calendar.getInstance();

                cal=Calendar.getInstance();

                String [] pieces=time.split(":");

                int hour=Integer.parseInt(pieces[0]);

                int minute=Integer.parseInt(pieces[1]);

                Log.d("Akash", "" + hour + minute);

                cal.set(Calendar.HOUR_OF_DAY, hour);

                cal.set(Calendar.MINUTE, minute);

                cal.set(Calendar.SECOND,0);

                if(cal.before(now))
                    cal.add(Calendar.DATE,1);

                Log.d("Akash",""+cal.getTime());

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);


            }
        }
    }
}