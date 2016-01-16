package akashagarwal45.bunkeasy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroduction extends AppIntro{

    @Override
    public void init(Bundle savedInstanceState) {

        String [] title=new String[6];

        title[0]="Bunk Easy";

        title[1]="Schedule";

        title[2]="Record";

        title[3]="Daily Notifications";

        String [] description=new String[6];

        description[0]="Manage your attendance with bunkeasy\nClick on the  plus button and start adding your subjects";

        description[1]="No need to worry about class timings\nLong press on the subject name and add it to schedule";

        description[2]="Blue- All classes attended\nRed- No classes attended\nPink- Some classes attended";

        description[3]="Enable notifications and get notified at the time specified";

        addSlide(AppIntroFragment.newInstance(title[0],description[0],R.drawable.slide1,Color.parseColor("#489360")));

        addSlide(AppIntroFragment.newInstance(title[1], description[1], R.drawable.slide2,Color.parseColor("#489360")));

        addSlide(AppIntroFragment.newInstance(title[2],description[2],R.drawable.slide3,Color.parseColor("#489360")));

        addSlide(AppIntroFragment.newInstance(title[3],description[3],R.drawable.slide4,Color.parseColor("#489360")));

        showSkipButton(true);

        setProgressButtonEnabled(true);

//        setBarColor(Color.parseColor("#3F51B5"));
  //      setSeparatorColor(Color.parseColor("#2196F3"));

    }

    @Override
    public void onSkipPressed() {
        Intent intent=new Intent(this,AddSubjects.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        Intent intent=new Intent(this,AddSubjects.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onSlideChanged() {

    }
}
