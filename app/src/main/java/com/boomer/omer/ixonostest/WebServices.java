package com.boomer.omer.ixonostest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Omer on 2/12/2016.
 */
public class WebServices {

    private static WebServices sWebServices;
    private static Context sContext;

    public static void initialize(Context context){
        sContext = context;
        sWebServices = new WebServices();
    }
    public static WebServices getInstance(){return sWebServices;}

    private WebServices(){}

    public boolean checkForConnection(){
            boolean wifiConnected = false;
            boolean cellularConntected = false;

            ConnectivityManager connectivityManager = (ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        wifiConnected = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        cellularConntected = true;
            }
            return wifiConnected || cellularConntected;
    }

}
