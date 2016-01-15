package akashagarwal45.bunkeasy;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroduction extends AppIntro{

    @Override
    public void init(Bundle savedInstanceState) {

        String title="";
        String description="";

        addSlide(AppIntroFragment.newInstance(title,description,R.drawable.slide1,R.color.caldroid_light_red));

        showSkipButton(false);

        setProgressButtonEnabled(false);

    }

    @Override
    public void onSkipPressed() {

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}
