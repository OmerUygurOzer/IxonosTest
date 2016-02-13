package com.boomer.omer.ixonostest;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Omer on 2/12/2016.
 */
public class Ixonos extends Application{
    public static final String Author = "Omer Ozer";


    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.initialize(this);
        WebServices.initialize(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */


    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.app_tracker);
        }
        return mTracker;
    }
}
