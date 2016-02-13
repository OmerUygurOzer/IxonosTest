package com.boomer.omer.ixonostest;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Random;


/**
 * This Fragment class prompts the user all required text fields to collect data
 * to create a {@link User}
 */
public class SignUp extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    /**
     * This fragment can fire notifications through it's {@link FragmentNotificationListener}
     */
    private FragmentNotificationListener mNotificationListener;

    /**
     * This fragment can change the navigation of the user through it's {@link NavigationController}
     */
    private NavigationController mNavigationController;
    private Tracker mTracker;

    private SessionManager mSessionManager;

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

        mSessionManager = SessionManager.getInstance();

        Ixonos ixonos = (Ixonos)getActivity().getApplication();
        mTracker = ixonos.getDefaultTracker();

        mTracker.setScreenName("Sign-Up Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        editTextEmail = (EditText)v.findViewById(R.id.editTextEmail);
        editTextFirstName = (EditText)v.findViewById(R.id.editTextFirstName);
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
                mSessionManager.signUp(email,fName,lName);
                mNavigationController.navigateTo(NavigationController.HOME);
            }
        }


        mListener.onFragmentInteraction(v.getId());
    }

    /**
     * Called when the entered e-mail is not a valid type.
     */
    private void onInvalidEmail(){
        mNotificationListener.createNotification("Email entered is invalid");
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int viewID);
    }

    /**
     * Fires a notification when the entered email is not valid
     */
    private static boolean isValidEmail(CharSequence email){
        if (email==null){
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
