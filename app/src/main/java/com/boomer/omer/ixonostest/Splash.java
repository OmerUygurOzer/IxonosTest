package com.boomer.omer.ixonostest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class Splash extends AppCompatActivity {

    int splashWait = 0;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager.initialize(getApplicationContext());

        setContentView(R.layout.activity_splash);
        //Determine application entry method
        Uri data = getIntent().getData();
        if(data !=null){
            if(data.getScheme().equals("ixonos.test")){
                entryViaLink();
            }
        }else{
            regularEntry();
        }



        initializeSplash();

    }

    private void entryViaLink(){

    }

    private void regularEntry(){

    }

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

    private void toCore() {
        Intent i = new Intent(this, Core.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }




}
