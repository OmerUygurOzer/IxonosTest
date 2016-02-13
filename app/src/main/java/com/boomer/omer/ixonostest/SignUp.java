package com.boomer.omer.ixonostest;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUp.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUp#} factory method to
 * create an instance of this fragment.
 */
public class SignUp extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private FragmentNotificationListener mNotificationListener;
    private NavigationController mNavigationController;

    private SessionManager sessionManager;

    private EditText editTextEmail;
    private EditText editTextFirstName;
    private EditText editTextLastName;

    private Button buttonGo;

    public SignUp() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNotificationListener = (FragmentNotificationListener)getActivity();
        mListener = (OnFragmentInteractionListener)getActivity();
        mNavigationController = (NavigationController)getActivity();
        sessionManager = SessionManager.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        editTextEmail = (EditText)v.findViewById(R.id.editTextEmail);
        editTextFirstName = (EditText)v.findViewById(R.id.editTextFistName);
        editTextLastName = (EditText)v.findViewById(R.id.editTextLastName);

        buttonGo = (Button)v.findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(this);


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonGo){
            if(!isValidEmail(editTextEmail.getText().toString())){
                onInvalidEmail();
            }else{
                String email = editTextEmail.getText().toString();
                String fName = editTextFirstName.getText().toString();
                String lName = editTextLastName.getText().toString();
                sessionManager.signUp(email,fName,lName);
                mNavigationController.navigateTo(NavigationController.HOME);
            }
        }


        mListener.onFragmentInteraction(v.getId());
    }

    private void onInvalidEmail(){
        mNotificationListener.createNotification("Email entered is invalid");
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int viewID);
    }

    private static boolean isValidEmail(CharSequence email){
        if (email==null){
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
