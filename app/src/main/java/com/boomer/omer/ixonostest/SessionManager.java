package com.boomer.omer.ixonostest;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Omer on 2/12/2016.
 */
public class SessionManager {

    private static SessionManager sessionManager;
    private static Context sContext;
    private static boolean isInitialized = false;

    public static void initialize(Context context){
        if(sContext==null){
        sContext = context;
        sessionManager = new SessionManager();}
        isInitialized = true;
    }
    public static SessionManager getInstance(){
        if(!isInitialized){return null;}
        return sessionManager;};

    private SharedPreferences sharedPreferences;

    private static final String env = "ixonos";
    private static final int MODE_PRIVATE = 0;

    private User user = new User();

    private boolean userActive = false;

    private SessionManager(){
        this.sharedPreferences = sContext.getSharedPreferences(env,MODE_PRIVATE);
    }

    public void signUp(String email,String fName,String lName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(User.EMAIL_ID,email);
        editor.putString(User.FIRST_NAME_ID,fName);
        editor.putString(User.LAST_NAME_ID,lName);
        editor.commit();

        user.email = email;
        user.firstName = fName;
        user.lastName = lName;

        userActive = true;

    }

    public void logoutCurrent(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.remove(User.EMAIL_ID);
        editor.remove(User.FIRST_NAME_ID);
        editor.remove(User.LAST_NAME_ID);
        editor.commit();
        userActive = false;
    }

    public User getUser(){
        if(!userActive){return null;}
        return user;
    }

}
