package com.example.vivekgoel.mapdemo.signinSignup;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Anusha on 3/31/2016.
 */
public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("firstName", user.firstName);
        spEditor.putString("lastName", user.lastName);
        spEditor.putString("email", user.email);
        spEditor.putString("password", user.password);
        spEditor.putInt("phnum", user.phnum);
        spEditor.commit();
    }


    public User getLoggedInUser(){
        String firstName = userLocalDatabase.getString("firstName", "");
        String lastName = userLocalDatabase.getString("lastName", "");
        String email = userLocalDatabase.getString("email", "");
        String password = userLocalDatabase.getString("password", "");
        int phnum = userLocalDatabase.getInt("phnum", -1);
//        int checkbox=userLocalDatabase.getInt("checkbox",-1);

        User storedUser = new User(firstName,lastName, email, password,phnum);

//        User storedUser = new User(firstName,lastName, email, password,phnum,checkbox);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("LoggedIn", false)== true){
            Log.i("getUserLogIn", "true");
            return true;
        }else{
            Log.i("getUserLogIn", "false");
            return false;
        }
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
