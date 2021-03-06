package com.boomer.omer.ixonostest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * The splash screen for the application. Lasts 3 seconds and starts the {@link Core} activity in the end
 */
public class Splash extends AppCompatActivity {

    Tracker mTracker;

    int splashWait = 0;

    /**
     * {@link CountDownTimer} for the splash screen duration
     */
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ixonos ixonos = (Ixonos)getApplication();
        mTracker = ixonos.getDefaultTracker();

        setContentView(R.layout.activity_splash);
        //Determine application entry method
        Uri data = getIntent().getData();
        if(data !=null){
            if(data.getScheme().equals("http")){
                entryViaLink();
            }
        }else{
            Log.d("Entry Point:","Regular Launch");
        }

        initializeSplash();

    }

    /**
     * Get's called if the app entry was via an http link
     */
    private void entryViaLink(){
        mTracker.setScreenName("Entry via http link");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        Log.d("Entry Point:","Via http link");
    }

    /**
     * Splash screen time is initialized
     */
    private void initializeSplash() {
        splashWait = 3 *1000;  //3 Seconds default wait time

        CountDownTimer countDownTimer = new CountDownTimer(splashWait, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                toCore();
            }
        };
        timer = countDownTimer;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    /**
     * Starts the {@link Core} Activity
     */
    private void toCore() {
        Intent i = new Intent(this, Core.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }




}
