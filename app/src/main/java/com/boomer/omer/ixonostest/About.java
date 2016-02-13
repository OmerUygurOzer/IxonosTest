package com.boomer.omer.ixonostest;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.IccOpenLogicalChannelResponse;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * The Fragment that shows the user the Application version and
 * the name of the Author
 */
public class About extends Fragment {

    public About() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_about, container, false);
        /**
         * The view that holds the title "About"
         */
        TextView aboutTitle = (TextView)v.findViewById(R.id.aboutTitle);

        /**
         * The view that holds the application version
         */
        TextView applicationVersion = (TextView)v.findViewById(R.id.applicationVersion);

        /**
         * /The view that holds the name of the author
         */
        TextView authorName = (TextView)v.findViewById(R.id.authorName);

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            applicationVersion.setText("Application version <"+version+">");
            authorName.setText("Author <"+ Ixonos.Author + ">");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setMarginAndLayout(aboutTitle, applicationVersion, authorName);

        return v;
    }

    /**
     * This method dynamically adjusts the location of the {@link TextView} s
     * @param aboutTitle
     * @param applicationVersion
     * @param authorName
     */
    private void setMarginAndLayout(TextView aboutTitle,TextView applicationVersion,TextView authorName){

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int deviceHeight = displayMetrics.heightPixels;

        LinearLayout.LayoutParams parammsComponent = (LinearLayout.LayoutParams) aboutTitle.getLayoutParams();
        parammsComponent.topMargin = (int)(deviceHeight * 0.08f);
        aboutTitle.setLayoutParams(parammsComponent);

        parammsComponent = (LinearLayout.LayoutParams) applicationVersion.getLayoutParams();
        parammsComponent.topMargin = (int)(deviceHeight * 0.06f);
        applicationVersion.setLayoutParams(parammsComponent);

        parammsComponent = (LinearLayout.LayoutParams) authorName.getLayoutParams();
        parammsComponent.topMargin = (int)(deviceHeight * 0.03f);
        authorName.setLayoutParams(parammsComponent);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
