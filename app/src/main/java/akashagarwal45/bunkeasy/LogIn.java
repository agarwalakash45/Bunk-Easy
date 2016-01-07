package akashagarwal45.bunkeasy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity{

    EditText username;
    EditText password;
    UserDBHandler db;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Toolbar myToolbar=(Toolbar)findViewById(R.id.toolbar);
        myToolbar.setTitle("Log In");
        setSupportActionBar(myToolbar);


        db=new UserDBHandler(this,null,null,1);

        username=(EditText)findViewById(R.id.userName);
        password=(EditText)findViewById(R.id.passWord);

        //create reference for text
        final TextView reg_text=(TextView)findViewById(R.id.register_here);

        reg_text.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent=new Intent(LogIn.this,Registration.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void loginButtonClicked(View view){
        if(db.authUser(username.getText().toString(),password.getText().toString())){
            password.setText(null);
            Intent intent=new Intent(this,AddSubjects.class);
            startActivity(intent);
        }
        else{
            password.setText(null);
            Toast toast=Toast.makeText(getApplicationContext(),"username and password you entered don't match",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
