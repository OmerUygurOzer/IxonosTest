package com.boomer.omer.ixonostest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
            boolean cellularConnected = false;

            ConnectivityManager connectivityManager = (ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo ni : networkInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        wifiConnected = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        cellularConnected = true;
            }
            Log.d("Web Services:", wifiConnected? "Wifi Active" : "Wifi InActive");
            Log.d("Web Services:", cellularConnected? "Cellular Active" : "Cellular InActive");
            return wifiConnected || cellularConnected;
    }

}
