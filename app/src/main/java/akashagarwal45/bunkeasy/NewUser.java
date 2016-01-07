package akashagarwal45.bunkeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class NewUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        Bundle regActivity=getIntent().getExtras();

        String name=regActivity.getString("Name");

        TextView welcomeName=(TextView)findViewById(R.id.welcomeName);

        welcomeName.setText(name);

    }

    public void nextButtonClicked(View view){
        Intent intent=new Intent(NewUser.this,AddSubjects.class);
        startActivity(intent);

    }

}
