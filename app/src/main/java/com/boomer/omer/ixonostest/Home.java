package com.boomer.omer.ixonostest;


import android.app.Fragment;
import android.content.Context;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements GeoServices.OnReceiveLocationUpdate,GoogleMap.OnMarkerClickListener {


    private MapView mMapView;
    private GoogleMap googleMap;
    private GeoServices geoServices;

    private OnFragmentInteractionListener mListener;
    private FragmentNotificationListener mNotificationListener;
    private NavigationController mNavigationController;

    TextView textViewAddress;
    SessionManager sessionManager;

    public Home() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotificationListener = (FragmentNotificationListener)getActivity();
        geoServices = new GeoServices(getActivity());
        geoServices.addLocationUpdateListener(this);
        sessionManager = SessionManager.getInstance();
        mNavigationController = (NavigationController)getActivity();
        if(sessionManager.getUser()==null){
            mNavigationController.navigateTo(NavigationController.SIGN_UP);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        geoServices.stop();
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
        googleMap = mMapView.getMap();
        googleMap.setOnMarkerClickListener(this);
        geoServices.start();
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }



    public void onNoInternet(){
        mNotificationListener.createNotification("No internet connection");
    }

    @Override
    public void onReceiveLocationUpdate(GeoPoint geoPoint) {
        googleMap.clear();
        MarkerOptions marker = new MarkerOptions().position(new LatLng(geoPoint.latitude, geoPoint.longitute)).title(geoPoint.addresses.get(1));
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.locationpin));
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(geoPoint.latitude, geoPoint.longitute)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
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


