package akashagarwal45.bunkeasy;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SubjectSettings extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Akash","I am here!!");
        addPreferencesFromResource(R.xml.preferences);

    }

}