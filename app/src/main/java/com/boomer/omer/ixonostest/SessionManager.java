package com.boomer.omer.ixonostest;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Omer on 2/12/2016.
 */

/**
 * The singleton that manages the user sessions. All user actions
 * such as  and logoutCurrent are done via this class
 */
public class SessionManager {


    /**This is the ony {@link SessionManager} instance that will ever be created
     */
    private static SessionManager sessionManager;

    /**Current Application {@link Context}context;
     */
    private static Context sContext;

    /**Whether the SessionManager is initialized
     */
    private static boolean isInitialized = false;


    /**
    @params context the application context that is required to initialize the Session Manager
     */
    public static void initialize(Context context){
        if(sContext==null){
        sContext = context;
        sessionManager = new SessionManager();}
        isInitialized = true;
    }

    /**
     *
     * @return returns the only instance of the Session Manager singleton
     */
    public static SessionManager getInstance(){
        if(!isInitialized){return null;}
        return sessionManager;};

    /**
     * SharedPreferences class that the SessionManager needs in order to store the user data
     */
    private SharedPreferences sharedPreferences;

    /**
     * Environment tag required by the SharedPreferences class
     */
    private static final String env = "ixonos";

    /**
     * Access mode code for SharedPreferences class
     */
    private static final int MODE_PRIVATE = 0;

    /**
     * The active user instance
     */
    private User user = new User();


    /**
     * Whether there is an active user or not
     */
    private boolean userActive = false;

    /**
     * Private SessionManager constructor that also creates an instance for the SharedPreferences
     */
    private SessionManager(){
        this.sharedPreferences = sContext.getSharedPreferences(env,MODE_PRIVATE);
    }

    /**
     * This method signs up the user by storing it's data into the Shared Preferences
     * @param email user e-mail address
     * @param fName user first name
     * @param lName user last name
     */
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

    /**
     * Logs out the current active user, the user instance can later on be changed again via signing up
     */
    public void logoutCurrent(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.remove(User.EMAIL_ID);
        editor.remove(User.FIRST_NAME_ID);
        editor.remove(User.LAST_NAME_ID);
        editor.commit();
        userActive = false;
    }

    /**
     * @return returns the current user instance .Will return null of no user is active.
     */
    public User getUser(){
        if(!userActive){return null;}
        return user;
    }

}
