package com.boomer.omer.ixonostest;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
public class GeoServices implements LocationListener ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback{


    private Context mContext;
    private LocationManager locationManager;

    private GoogleApiClient mGoogleApiClient;

    private GeoPoint mostRecentPoint = new GeoPoint();
    private Geocoder geocoder;

    private boolean running = false;

    private List<OnReceiveLocationUpdate> updateListeners = new ArrayList<>();

    public GeoServices(Context context) {
        this.mContext = context;
        this.geocoder = new Geocoder(context, Locale.getDefault());
        this.mostRecentPoint.reset();

        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    public void start(){
        mGoogleApiClient.connect();
        running = true;
    }

    public void stop(){
        mGoogleApiClient.disconnect();
        running = false;
    }



    @Override
    public void onLocationChanged(Location location) {
       processLocation(location);
    }


    public GeoPoint getMostRecentPoint(){return mostRecentPoint;}

    @Override
    public void onConnected(Bundle bundle) {
        requestLocationServices();
        running = true;
        Log.d("GeoServices:", "Connection success");
    }

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

    private void processLocation(Location location){
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
            for(Address address:geoAddresses){
                mostRecentPoint.addresses.add(address.getAddressLine(0));
            }

            Log.d("GeoServices:", mostRecentPoint.addresses.size() + " addresses processed");

        }
        Log.d("GeoServices:", getMostRecentPoint().latitude + "," + getMostRecentPoint().longitute);
        notifyListeners(mostRecentPoint);
        Log.d("GeoServices:", "Location processed");
    }

    @Override
    public void onConnectionSuspended(int i) {
        running = false;
        Log.d("GeoServices:", "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GeoServices:", "Connection failed");
        running = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    public void notifyListeners(GeoPoint geoPoint){
        for(OnReceiveLocationUpdate listener:updateListeners){
            listener.onReceiveLocationUpdate(geoPoint);
        }
    }

    public void addLocationUpdateListener(OnReceiveLocationUpdate onReceiveLocationUpdate){
        updateListeners.add(onReceiveLocationUpdate);
    }

    public interface OnReceiveLocationUpdate{
        public void onReceiveLocationUpdate(GeoPoint geoPoint);
    }


}
