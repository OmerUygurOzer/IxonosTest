package com.boomer.omer.ixonostest;



import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Omer on 2/11/2016.
 */

/**
 * This class handles all location data through GoogleAPiClient. GoogleAPIClient connects when this class is started and disconnects when stopped
 */
public class GeoServices implements LocationListener ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback{


    /**
     * Current application context
     */
    private Context mContext;


    /**
     * GoogleAPiClient instance
     */
    private GoogleApiClient mGoogleApiClient;


    /**
     * The most recent GeoPoint that is created
     */
    private GeoPoint mostRecentPoint = new GeoPoint();

    /**
     *GeoCoder is required to generate the address from lat and long
     */
    private Geocoder geocoder;

    /**
     * Whether {@Link GeoServices} is running
     */
    private boolean running = false;

    /**
     * List of all class instances that listen the GeoServices
     */
    private List<OnReceiveLocationUpdate> updateListeners = new ArrayList<>();


    /**
     * Constructor that creates the Google API client
     * @param context current application context that is required by the GeoServices
     */
    public GeoServices(Context context) {
        this.mContext = context;
        this.geocoder = new Geocoder(context, Locale.getDefault());
        this.mostRecentPoint.reset();


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    /**
     * Starts the GeoServices. Updates are now being listened to.
     */
    public void start(){
        mGoogleApiClient.connect();
        running = true;
    }

    /**
     * Stops the GeoServices
     */
    public void stop(){
        mGoogleApiClient.disconnect();
        running = false;
    }


 /**
  *Called when the location of the user changes
  */
    @Override
    public void onLocationChanged(Location location) {
       processLocation(location);
    }

    /**
     *
     * @return returns the most recent point that is updated by the GeoServices
     */
    public GeoPoint getMostRecentPoint(){return mostRecentPoint;}

    /**
     * Call back for when the connection actually happens
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        requestLocationServices();
        running = true;
        Log.d("GeoServices:", "Connection success");
    }


    /**
     * Requests location services from the user.
     * @return returns true if permissions are granted
     */
    public boolean requestLocationServices(){
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){return false;}
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setSmallestDisplacement(1f);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        processLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
        return true;
    }

    /**
     * Process a location data and generates a {@Link GeoPoint}
     * @param location {@link Location}  needed to generate a GeoPoint
     */
    private void processLocation(Location location){
        if(location==null){return;}
        double latitude;
        double longitute;
        latitude = location.getLatitude();
        longitute = location.getLongitude();
        List<Address> geoAddresses = null;
        try {
            geoAddresses = geocoder.getFromLocation(latitude,longitute,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(geoAddresses!=null){
            mostRecentPoint.reset();
            mostRecentPoint.latitude = latitude;
            mostRecentPoint.longitute = longitute;
            String adr = "";
            for(Address address:geoAddresses){
                int maxAdr = address.getMaxAddressLineIndex();
                for(int i = 0 ; i< maxAdr; i++){
                    adr +=address.getAddressLine(i) + " ";
                }
                mostRecentPoint.addresses.add(adr);
            }

            Log.d("GeoServices:", mostRecentPoint.addresses.size() + " addresses processed");

        }
        Log.d("GeoServices:", getMostRecentPoint().latitude + "," + getMostRecentPoint().longitute);
        notifyListeners(mostRecentPoint);
        Log.d("GeoServices:", "Location processed");
    }

    /**
     * Call back for when the connection is suspended
     * @param i data from GoogleApiClient
     */
    @Override
    public void onConnectionSuspended(int i) {
        running = false;
        Log.d("GeoServices:", "Connection suspended");
    }

    /**
     * Callback for when the connection fails
     * @param connectionResult result of the connection in data
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GeoServices:", "Connection failed");
        running = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    /**
     * Notifies all the listeners by sending them the most recent {@link GeoPoint}
     * @param geoPoint
     */
    public void notifyListeners(GeoPoint geoPoint){
        for(OnReceiveLocationUpdate listener:updateListeners){
            listener.onReceiveLocationUpdate(geoPoint);
        }
    }

    /**
     * Adds a {@link com.boomer.omer.ixonostest.GeoServices.OnReceiveLocationUpdate} to the GeoServices
     * @param onReceiveLocationUpdate
     */
    public void addLocationUpdateListener(OnReceiveLocationUpdate onReceiveLocationUpdate){
        updateListeners.add(onReceiveLocationUpdate);
    }

    /**
     * Checks whether the location settings are enables
      * @return returns whether the location settings are enables
     */
    public boolean checkForLocationSettings(){
        LocationManager locationManager = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
           return false;
        }
        return true;
    }


    /**
     * The interface for update listeners to GeoServices
     */
    public interface OnReceiveLocationUpdate{
        public void onReceiveLocationUpdate(GeoPoint geoPoint);
    }


}
