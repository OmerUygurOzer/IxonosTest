package com.boomer.omer.ixonostest;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.TextView;


import java.util.Random;


/**
 * This the core class that handles any and all fragment navigation ,notification generation, {@link Toolbar} title management
 */
public class Core extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,
        SignUp.OnFragmentInteractionListener,FragmentNotificationListener,NavigationController,ActionbarHolder{

    /**
     * The layout for the navigation drawer
     */
    DrawerLayout drawerLayout;

    /**
     * Drawer Listener for the navigation drawer
     */
    ActionBarDrawerToggle toggle;


    /**
     * Current active SessionManager
     */
    private SessionManager mSessionManager;

    /**
     * The TextView that holds the title of the toolbar
     */
    TextView mTitleHolder;


    /**
     * In onCreate of Core class, an instance for the Toolbar title is taken and given to the mTitleHolder {@link TextView}
     * The {@link SessionManager} is instanced here. Navigation Drawer is set up here entirely.
     * @param savedInstanceState currently save activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT, Gravity.CENTER);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_title_bar);
        getSupportActionBar().getCustomView().setLayoutParams(params);


        mTitleHolder =(TextView) getSupportActionBar().getCustomView().findViewById(R.id.title_text_holder);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSessionManager = SessionManager.getInstance();
        if(mSessionManager.getUser()==null){
            launchSignUp();
        }else{
            selectedHome();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * This method manages menu selections of the Navigation Drawer
     * @param item the selected menu item
     * @return whether the menu selection was succesfully processed
     */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_back) {
            //Do nothing}
        } else if (id == R.id.home) {
            selectedHome();
        } else if (id == R.id.about) {
            selectedAbout();
        } else if (id == R.id.logout) {
            selectedLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Obsolete
     * @param viewID
     */
    @Override
    public void onFragmentInteraction(int viewID) {}

    /**
     * This is the method that handles the selection of {@link Home}
     * The user is taken to the Home screen
     */
    private void selectedHome(){
        Home home = new Home();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentholder, home, "home");
        fragmentTransaction.commit();
    }

    /**
     * This is the method that handles the selection of {@link About}
     * The user is taken to the About screen
     */
    private void selectedAbout(){
        setTitle("");
        About about = new About();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentholder, about, "about");
        fragmentTransaction.commit();
    }

    /**
     * This is the method that handles the selection of {@link SignUp}
     * The user is taken to the SignUp screen
     */
    private void launchSignUp(){
        SignUp signUp = new SignUp();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentholder, signUp, "signup");
        fragmentTransaction.commit();

    }


    /**
     * This is the method that handles the selection of Logout
     * The user is taken to the Splash screen and the current user data is erased
     */
    private void selectedLogout(){
        mSessionManager.logoutCurrent();
        setTitle("");
        Intent i = new Intent(this,Splash.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    /**
     * The method the creates heads-up notifications with the incoming message
     * @param message The message that the {@link FragmentNotificationListener} will generate the notification for.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void createNotification(String message) {
        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int i = new Random().nextInt();
        Notification notification = nBuilder
                .setSmallIcon(R.drawable.erroricon)
                .setAutoCancel(true)
                .setVisibility(3)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build();
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.ixonosnotification);
        remoteViews.setTextViewText(R.id.message,message);
        notification.contentView = remoteViews;
        notification.headsUpContentView = remoteViews;
        notificationManager.notify(i, notification);
    }


    /**
     * This is the method that handles navigation orders
     * @param destination the destination screen
     */
    @Override
    public void navigateTo(int destination) {
        switch (destination){
            case NavigationController.HOME:
                selectedHome();
                break;
            case NavigationController.ABOUT:
                selectedAbout();
                break;
            case NavigationController.SIGN_UP:
               launchSignUp();
                break;
            default:
                break;
        }
    }

    /**
     * This is the method that changes the Action/Tool bar title
     * @param title The title the action bar will hold
     */
    @Override
    public void setTitle(String title) {
        mTitleHolder.setText(title);
    }
}
