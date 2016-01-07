package akashagarwal45.bunkeasy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;

public class SubjectDetails extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        Log.d("Akash","In caldroid activity");
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args=new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH,cal.get(Calendar.MONTH)+1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        android.support.v4.app.FragmentTransaction t= getSupportFragmentManager().beginTransaction();
        t.replace(R.id.cal, caldroidFragment);
        t.commit();

    }
}
