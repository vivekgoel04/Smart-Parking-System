package com.example.vivekgoel.mapdemo.signinSignup;

/**
 * Created by Anusha on 3/31/2016.
 */
public class User {
    String firstName, lastName, email,password;
    int phnum;
    int checkbox;

//    public User(String firstName,String lastName, String email,String password,int phnum,int checkbox){
//        this.firstName=firstName;
//        this.lastName=lastName;
//        this.email = email;
//        this.password = password;
//        this.phnum=phnum;
//        this.checkbox=checkbox;
//
//    }
public User(String firstName,String lastName, String email,String password,int phnum){
    this.firstName=firstName;
    this.lastName=lastName;
    this.email = email;
    this.password = password;
    this.phnum=phnum;

}

    public User(String email, String password){
        this.firstName="";
        this.lastName="";
        this.email = email;
        this.password = password;
        this.phnum=-1;
//        this.checkbox=-1;

    }
}
