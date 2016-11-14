package com.truemdhq.projectx.activity;

/**
 * Created by Yashvardhan on 29/07/15.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.truemdhq.projectx.R;
import io.paperdb.Paper;


public class HomeFragment extends Fragment {

    private static String LOG_TAG = "HomeFragmentViewActivity";

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private Unbinder unbinder;

    @BindView(R.id.image_button_drawer_home) ImageView drawerButton;
    @BindView(R.id.add_transaction) RelativeLayout addTransaction;
    @BindView(R.id.btn_bottom) RelativeLayout checkInvoices;
    @OnClick(R.id.image_button_drawer_home)
    public void onClick1(ImageView button){
        ((MainActivity)getActivity()).open();
    }
    @OnClick(R.id.add_transaction)
    public void onClick2(RelativeLayout button){

        Intent nextActivity = new Intent(getActivity(), AddClientActivity.class);
        startActivity(nextActivity);
        //push from bottom to top
        //getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        //slide from right to left
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
    @OnClick(R.id.btn_bottom)
    public void onClick3(RelativeLayout button){

        Intent nextActivity = new Intent(getActivity(), InvoiceActivity.class);
        startActivity(nextActivity);
        //push from bottom to top
        getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        //slide from right to left
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }




    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_projectx, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        MainActivity.chatInitializerProjectXCode = "I44IQI";
        MainActivity.chatInitializerProjectXName = "CROCIN 500MG TABLET";




        return rootView;
    }
    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




    @Override
    public void onResume(){
        super.onResume();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Create Remote Config Setting to enable developer mode.
        // Fetching configs from the server is normally limited to 5 requests per hour.
        // Enabling developer mode allows many more requests to be made per hour, so developers
        // can test different config values during development.
        // [START enable_dev_mode]
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        // [END enable_dev_mode]

        // Set default Remote Config values. In general you should have in app defaults for all
        // values that you may configure using Remote Config later on. The idea is that you
        // use the in app defaults and when you need to adjust those defaults, you set an updated
        // value in the App Manager console. Then the next time you application fetches from the
        // server, the updated value will be used. You can set defaults via an xml file like done
        // here or you can set defaults inline by using one of the other setDefaults methods.S
        // [START set_default_values]
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_details);
        // [END set_default_values]


        // put your code here...
        // mPriceTextView.setText(mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY));
        String versionName="";
        Log.e("RemoteConfig: ",""+mFirebaseRemoteConfig.getString("mandatory_update"));
        try {
            versionName = getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("RemoteConfigErr: ",e.getMessage());
            e.printStackTrace();
        }

        Log.e("RemoteConfig:Ver: ",""+versionName);

        long cacheExpiration = 10; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 10;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
        // fetched and cached config would be considered expired because it would have been fetched
        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
        // throttling is in progress. The default expiration duration is 43200 (12 hours).

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e("HomeRemoteConfig: " , "Fetch Succeeded");
                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Log.e("HomeRemoteConfig: ", "Fetch failed");
                        }
                        //displayPrice();
                        Log.e("RemoteConfig: ",""+mFirebaseRemoteConfig.getString("mandatory_update"));

                    }
                });
        Log.e("RemoteConfig: ",""+mFirebaseRemoteConfig.getString("mandatory_update"));
        // [END fetch_config_with_callback]

        if(mFirebaseRemoteConfig.getString("mandatory_update").equalsIgnoreCase("true")){
            SweetAlertDialog sdialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("A new version is available.")
                    .setContentText("Please update to the latest version as the current version is no longer supported.")
                    .setConfirmText("UPDATE")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            getActivity().finish();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.truemdhq.projectx"));
                            //finish();
                            startActivity(browserIntent);

                        }
                    });

                    sdialog.show();

            sdialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        getActivity().finish();

                        System.exit(0);
                    }
                    return true;
                }
            });
        }

    }




}
