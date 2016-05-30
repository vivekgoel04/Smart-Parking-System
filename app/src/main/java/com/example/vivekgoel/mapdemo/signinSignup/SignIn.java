package com.example.vivekgoel.mapdemo.signinSignup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vivekgoel.mapdemo.MapsActivity;
import com.example.vivekgoel.mapdemo.R;

public class SignIn extends AppCompatActivity implements View.OnClickListener{


    Button blogin;
    EditText etemail, etPassword;
    TextView tvregisterLink;
    UserLocalStore userLocalStore;
    SharedPreferences walkBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Log.d("************", "Inside Sign In");


        etemail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.etPassword);
        blogin = (Button) findViewById(R.id.bLogin);
        tvregisterLink = (TextView) findViewById(R.id.tvRegisterLink);

        blogin.setOnClickListener(this);
        tvregisterLink.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                String email = etemail.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(email, password);
                authenticate(user);
                walkBack = getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = walkBack.edit();
                editor.clear();
                editor.putString("Log Out", "0");

                // Commit the edits!
                editor.apply();
                break;
            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Signup.class));
                break;
        }
    }

    private void authenticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {//after fetch backgnd iscompleted the done parameter is recalled --(if u seein server request cmd-userCallback.done(returnedUser)
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                    Log.i("L-Auth", returnedUser.toString());
                }
            }
        });
    }
    private void showErrorMessage(){
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(SignIn.this);
        dialogueBuilder.setMessage("Incorrect User Details");
        dialogueBuilder.setPositiveButton("OK",null);//null is kept to say that no action to be taken just error msg disappears after click of ok button
        dialogueBuilder.show();
    }
    private void logUserIn(User returnedUser) {
        //Log.i("Loguserin", returnedUser.name);
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this,MapsActivity.class));

    }
}