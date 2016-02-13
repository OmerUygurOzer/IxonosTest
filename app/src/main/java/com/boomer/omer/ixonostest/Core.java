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

public class Core extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,
        SignUp.OnFragmentInteractionListener,FragmentNotificationListener,NavigationController,ActionbarHolder{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    private SessionManager mSessionManager;

    TextView mTitleHolder;

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

    @Override
    public void onFragmentInteraction(int viewID) {}

    private void selectedHome(){
        Home home = new Home();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentholder, home, "home");
        fragmentTransaction.commit();
    }

    private void selectedAbout(){
        About about = new About();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentholder, about, "about");
        fragmentTransaction.commit();
    }

    private void launchSignUp(){
        SignUp signUp = new SignUp();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentholder, signUp, "signup");
        fragmentTransaction.commit();

    }

    private void selectedLogout(){
        mSessionManager.logoutCurrent();
        setTitle("");
        Intent i = new Intent(this,Splash.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


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

    @Override
    public void setTitle(String title) {
        mTitleHolder.setText(title);
    }
}
