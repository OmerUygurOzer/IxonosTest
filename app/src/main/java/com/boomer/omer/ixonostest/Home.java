package com.boomer.omer.ixonostest;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Home extends Fragment implements GeoServices.OnReceiveLocationUpdate,GoogleMap.OnMarkerClickListener {


    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private GeoServices mGeoServices;
    private WebServices mWebServices;

    private SessionManager mSessionManager;

    private ActionbarHolder mActionbarHolder;


    private OnFragmentInteractionListener mListener;
    private FragmentNotificationListener mNotificationListener;
    private NavigationController mNavigationController;

    TextView textViewAddress;

    private User mUser;


    public Home() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotificationListener = (FragmentNotificationListener)getActivity();
        mGeoServices = new GeoServices(getActivity());
        mGeoServices.addLocationUpdateListener(this);
        mWebServices = WebServices.getInstance();
        mSessionManager = SessionManager.getInstance();
        mNavigationController = (NavigationController)getActivity();
        mActionbarHolder = (ActionbarHolder)getActivity();

        if(mSessionManager.getUser()==null){
           noUserDataAvailable();
        }
        if(!mWebServices.checkForConnection()){
            onNoInternet();
        }




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mGeoServices.start();
        if(!mGeoServices.checkForLocationSettings()){
            Intent activateGPS = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(activateGPS);
        }

        mUser = mSessionManager.getUser();
        if(mUser!=null)mActionbarHolder.setTitle(mUser.firstName + " " + mUser.lastName);

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        mGeoServices.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        textViewAddress = (TextView)v.findViewById(R.id.textviewAddress);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGoogleMap = mMapView.getMap();
        mGoogleMap.setOnMarkerClickListener(this);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }


    public void onNoInternet(){
        mNotificationListener.createNotification("No internet connection");
    }

    public void noUserDataAvailable(){
        mNavigationController.navigateTo(NavigationController.SIGN_UP);
    }

    @Override
    public void onReceiveLocationUpdate(GeoPoint geoPoint) {
        mGoogleMap.clear();
        MarkerOptions marker = new MarkerOptions().position(new LatLng(geoPoint.latitude, geoPoint.longitute)).title(geoPoint.addresses.get(1));
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationpin));
        mGoogleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(geoPoint.latitude, geoPoint.longitute)).zoom(12).build();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        textViewAddress.setText(geoPoint.addresses.get(1));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



}


