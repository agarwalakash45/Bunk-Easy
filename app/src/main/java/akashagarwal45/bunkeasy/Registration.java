package akashagarwal45.bunkeasy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends AppCompatActivity{

    EditText name;
    EditText username;
    EditText password;
    EditText email;
    UserDBHandler db;
    TextView outText;
    //Button reg_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Toolbar myToolbar=(Toolbar)findViewById(R.id.toolbar);
        myToolbar.setTitle("Registration");
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=(EditText)findViewById(R.id.name);
        username=(EditText)findViewById(R.id.userName);
        password=(EditText)findViewById(R.id.passWord);
        email=(EditText)findViewById(R.id.email);


        db=new UserDBHandler(this,null,null,1);
    }

    //method registerButtonClicked to register new user when register button is clicked
    public void registerButtonClicked(View view){
        Users newUser=new Users();

        if(name.getText().toString().isEmpty()||username.getText().toString().isEmpty()||password
                .getText().toString().isEmpty()){

            //create toast to display message
            Context context=getApplicationContext();
            CharSequence text="Fields can't be empty!!";
            int duration= Toast.LENGTH_SHORT;

            Toast toast=Toast.makeText(context,text,duration);
            toast.show();

        }
        else if(!db.searchUser(username.getText().toString())){
            newUser.setName(name.getText().toString());
            newUser.setUserName(username.getText().toString());
            newUser.setPassWord(password.getText().toString());
            newUser.setEmail(email.getText().toString());

            db.addUser(newUser);

            name.setText(null);
            username.setText(null);
            password.setText(null);
            email.setText(null);

            Intent intent=new Intent(this,NewUser.class);

            intent.putExtra("Name", name.getText().toString());

            startActivity(intent);
        }
        else{
            Toast toast=Toast.makeText(getApplicationContext(),"Username already exists!!",Toast.LENGTH_SHORT);
            toast.show();
            username.setText(null);
        }
    }

    public void registerTextClicked(View view){
        Intent intent=new Intent(this,LogIn.class);
        startActivity(intent);

    }

}
