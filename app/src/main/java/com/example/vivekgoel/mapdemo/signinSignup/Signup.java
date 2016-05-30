package com.example.vivekgoel.mapdemo.signinSignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.vivekgoel.mapdemo.MapsActivity;
import com.example.vivekgoel.mapdemo.R;

public class Signup extends AppCompatActivity implements View.OnClickListener {
    Button bSignup;
    EditText etfirstName,etlastName, emailSignup, pwd1, pwd2, etPhnum;
    CheckBox etCheckbox;
    int checked = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("************","Inside Sign Up");
        setContentView(R.layout.activity_signup);


        etfirstName = (EditText) findViewById(R.id.etfirstName);
        etlastName = (EditText) findViewById(R.id.etlastName);
        emailSignup = (EditText) findViewById(R.id.email);
        etCheckbox = (CheckBox) findViewById(R.id.checkbox);
        etPhnum = (EditText) findViewById(R.id.phnum);
        pwd1 = (EditText) findViewById(R.id.pwd1);
        pwd2 = (EditText) findViewById(R.id.pwd2);
        bSignup = (Button) findViewById(R.id.bSignup);
//        etCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked)
//                {
//                    checked=1;
//                }
//                else
//                {
//                    checked=0;
//                }
//            }
//        });
        bSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bSignup:

                String firstName = etfirstName.getText().toString();
                String lastName = etlastName.getText().toString();
                String email = emailSignup.getText().toString();
                String password = pwd1.getText().toString();
                int phnum = Integer.parseInt(etPhnum.getText().toString());
//                int isChecked = ((int) checked);
                User user = new User(firstName, lastName, email, password, phnum);


//                User user = new User(firstName, lastName, email, password, phnum, isChecked);
                //Log.i("Register", user.name);
                registeredUser(user);

                break;
        }

    }
    private void registeredUser(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(Signup.this, MapsActivity.class));
            }
        });
    }
}
