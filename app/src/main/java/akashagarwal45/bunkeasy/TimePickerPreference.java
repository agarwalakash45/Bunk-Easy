package akashagarwal45.bunkeasy;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

//Class to create Time Picker to pick time in preference activity
public class TimePickerPreference extends DialogPreference {

    private int lastHour=0;
    private int lastMinute=0;
    TimePicker picker=null;

    //Method to return hour from string of type hh:mm
    public static int getHour(String time){
        String [] pieces=time.split(":");       //returns array of strings computer by splitting this string around matches of ":"

        return Integer.parseInt(pieces[0]);
    }

    //Method to return minute value from time of format hh:mm
    public static int getMinute(String time){
        String [] pieces=time.split(":");

        return Integer.parseInt(pieces[1]);
    }

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText("SET");
        setNegativeButtonText("CANCEL");
    }

    @Override
    protected View onCreateDialogView() {
        picker=new TimePicker(getContext());
        return picker;
    }


    //Method to bind picker and set current hour and minute
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult){                     //returns true if user changed the value
            lastHour=picker.getCurrentHour();
            lastMinute=picker.getCurrentMinute();

           String hour=String.valueOf(lastHour);
            String minute=String.valueOf(lastMinute);

            String time=hour+":"+minute;

            if(callChangeListener(time))        //returns TRUE if user value should be set as the preference value
                persistString(time);            //returns TRUE if preference is persistent
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);

        String time=null;

        if(restorePersistedValue){                  //If this is true, we need to restore the persisted value
            if(defaultValue==null)                  //If dafault value is null
                time=getPersistedString("20:00");
            else
                time=getPersistedString(defaultValue.toString());       //The value from the SharedPreferences or the default value
        }
        else
            time=defaultValue.toString();           //else set the default value

        lastHour=getHour(time);
        lastMinute=getMinute(time);
    }
}