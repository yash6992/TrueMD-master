package info.truemd.android.activity;

/**
 * Created by Yashvardhan on 29/07/15.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.android.R;
import info.truemd.android.helper.SnappingHorizontalScrollView;
import io.paperdb.Paper;


public class HomeFragment extends Fragment {

    private static String LOG_TAG = "HomeFragmentViewActivity";
    private ImageButton cameraButtonHome;
    private ImageButton imageButtonDrawer, imageNotificationButton;
    private RelativeLayout searchRelativeLayout, rel4, rel5;
    private TextView tv14,tv15,tv24,tv25,tv34,tv35,tv44,tv45,tv46,tv54,tv55,tv56,ss,ser,offer,kmore, tv_truemd, tv_truemdst;
    private RelativeLayout ll1,ll2,ll3;
    private SnappingHorizontalScrollView scrollView;
    JSONObject getUserObject;
    String patient_name;
    ArrayList<String> offersUrls;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    ArrayList<String>  offersActive;
    ArrayList<String> offersImageUrls;
    int offer_pos;

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
        View rootView = inflater.inflate(R.layout.fragment_home_cardview1, container, false);

        MainActivity.chatInitializerTrueMDCode = "I44IQI";
        MainActivity.chatInitializerTrueMDName = "CROCIN 500MG TABLET";
        Paper.book("nav").write("selected","0");

        getUserObject=new JSONObject();
        offersActive = new ArrayList<>();
        offersUrls = new ArrayList<>();
        offersImageUrls = new ArrayList<>();
        offer_pos=0;


        Log.e("HomeFragmentUser: "," "+getUserObject.toString());

        //Typeface tf_r= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-ExtraBold.ttf");
        Typeface tf_l= Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");
        //Typeface tf_roboto= Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        Typeface tf_pacifico = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Pacifico.ttf");

        Typeface tf_hi = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CircularStd-Bold.otf");


        tv_truemd = (TextView) rootView.findViewById(R.id.hf_title);
        tv_truemdst = (TextView) rootView.findViewById(R.id.hf_hi);
        tv14 = (TextView) rootView.findViewById(R.id.hf_textView14);
        tv15 = (TextView) rootView.findViewById(R.id.hf_textView15);
        tv24 = (TextView) rootView.findViewById(R.id.hf_textView24);
        tv25 = (TextView) rootView.findViewById(R.id.hf_textView25);
        tv34 = (TextView) rootView.findViewById(R.id.hf_textView34);
        tv35 = (TextView) rootView.findViewById(R.id.hf_textView35);
        tv44 = (TextView) rootView.findViewById(R.id.hf_textView44);
        tv45 = (TextView) rootView.findViewById(R.id.hf_textView45);
        tv54 = (TextView) rootView.findViewById(R.id.hf_textView54);
        tv55 = (TextView) rootView.findViewById(R.id.hf_textView55);
        tv46 = (TextView) rootView.findViewById(R.id.hf_textView46);
        tv56 = (TextView) rootView.findViewById(R.id.hf_textView56);
        ss = (TextView) rootView.findViewById(R.id.hf_search);
       RelativeLayout hf8 = (RelativeLayout) rootView.findViewById(R.id.hf_8);
        ser = (TextView) rootView.findViewById(R.id.hf_services_h);
        offer = (TextView) rootView.findViewById(R.id.hf_offers_h);
        kmore = (TextView) rootView.findViewById(R.id.hf_knowmore_h);

        rel4 = (RelativeLayout) rootView.findViewById(R.id.home_relative_layout4);
        rel5 = (RelativeLayout) rootView.findViewById(R.id.home_relative_layout5);

        tv15.setTypeface(tf_l);
        tv25.setTypeface(tf_l);
        tv35.setTypeface(tf_l);
        tv14.setTypeface(tf_l);
        tv24.setTypeface(tf_l);
        tv34.setTypeface(tf_l);
        tv44.setTypeface(tf_l);
        tv45.setTypeface(tf_l);
        tv55.setTypeface(tf_l);
        tv54.setTypeface(tf_l);
        tv46.setTypeface(tf_l);
        tv56.setTypeface(tf_l);
        ss.setTypeface(tf_l);
        ser.setTypeface(tf_l);
        offer.setTypeface(tf_l);
        kmore.setTypeface(tf_l);
        tv_truemd.setTypeface(tf_pacifico); tv_truemdst.setTypeface(tf_hi);
        tv_truemdst.setTextColor(Color.parseColor("#C2185B"));

        patient_name=""+Paper.book("user").read("name");
        tv_truemdst.setText("Hi, "+patient_name+".");

        if(MainActivity.gotObject){

            try {
                //getUserObject = MainActivity.getUserObject;
                String patient = MainActivity.nameFromGetUser;
                //patient_name = patient.optString("patient_name").trim();
                JSONArray offers = MainActivity.getUserObject.optJSONArray("offers");

                Log.e("HomeFragmentUser: "," "+MainActivity.getUserObject.toString());
                Log.e("HomeFragmentOffers: "," "+offers.toString());
                //Log.e("HomeFragmentPatient: "," "+patient.toString());
                tv_truemdst.setText("Hi, "+patient+".");


                for(int i=0;i<offers.length();i++){

                    JSONObject offer = offers.getJSONObject(i);
                    Log.e("HomeFragmentOffer: ",i+": "+offers.toString());
                    offersActive.add(""+offer.optBoolean("active"));
                    offersImageUrls.add(offer.optString("img_url"));
                    offersUrls.add(offer.optString("url"));
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("HomeGotObject: ",""+e.getMessage());

                //patient_name = "";
                offersActive = new ArrayList<>();
                offersUrls = new ArrayList<>();
                offersImageUrls = new ArrayList<>();
            }



        }

        cameraButtonHome =  (ImageButton) rootView.findViewById(R.id.imageCameraOnSearchBar);



//        cameraButtonHome.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
////                Intent intent_main_search = new Intent(getActivity(), SearchActivity.class);
////                (getActivity()).startActivityForResult(intent_main_search, 0);
//                Toast.makeText(getActivity(), "camera action", Toast.LENGTH_SHORT).show();
//                Intent intent_main_search = new Intent(getActivity(), MedicineDetailsActivity2.class);
//                intent_main_search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent_main_search.putExtra("truemdCode", "6zV41V" + ":" + "LEMOLATE TABLET");
//                (getActivity()).startActivityForResult(intent_main_search, 0);
//            }
//        });
        imageButtonDrawer = (ImageButton)rootView.findViewById(R.id.image_button_drawer_home);
        imageButtonDrawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                ((MainActivity)getActivity()).open();
            }
        });
        imageNotificationButton = (ImageButton)rootView.findViewById(R.id.image_notification_home);
        imageNotificationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent_main_search = new Intent(getActivity(), AllNotificationActivity.class);
                (getActivity()).startActivityForResult(intent_main_search, 0);
            }
        });
        searchRelativeLayout = (RelativeLayout)rootView.findViewById(R.id.hf_22);
        searchRelativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent_main_search = new Intent(getActivity(), SearchActivity.class);
                (getActivity()).startActivityForResult(intent_main_search, 0);
            }
        });

        ll1 = (RelativeLayout)rootView.findViewById(R.id.hf_relative_layout1);
        ll1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent  = new Intent(getActivity(), OrderMedicineActivity.class);
                startActivity(intent);
            }
        });

        ll2 = (RelativeLayout)rootView.findViewById(R.id.hf_relative_layout2);
        ll2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent  = new Intent(getActivity(), ReminderActivity.class);
                startActivity(intent);
            }
        });

        ll3 = (RelativeLayout)rootView.findViewById(R.id.hf_relative_layout3);
        ll3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MainActivity.fromThankYou = true;
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
        rel4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                //Toast.makeText(getActivity(), "camera action", Toast.LENGTH_SHORT).show();
                Intent intent_main_search = new Intent(getActivity(), MedicineDetailsActivity2.class);
                intent_main_search.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent_main_search.putExtra("truemdCode", MainActivity.latestMedTrueMDCode + ":" + MainActivity.latestMedName);
                (getActivity()).startActivityForResult(intent_main_search, 0);
            }
        });
        rel5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MainActivity.fromHomeToChat = true;
                MainActivity.chatInitializerTrueMDCode = MainActivity.latestMedTrueMDCode;
                Intent toMainTrueMDSiriFragment = new Intent(getActivity(), MainActivity.class);
                startActivity(toMainTrueMDSiriFragment);

            }
        });
        rel4.setVisibility(View.GONE);
        rel5.setVisibility(View.GONE);
       // ll3.setVisibility(View.GONE);


        if(MainActivity.chatGhostOn&&MainActivity.prescriptionGhostOn) {
            rel4.setVisibility(View.VISIBLE);
            hf8.setVisibility(View.VISIBLE);
            rel5.setVisibility(View.VISIBLE);
            tv44.setText("Get details for "+MainActivity.latestMedName);
            tv54.setText("Ask about "+MainActivity.latestMedName);
        }
        else {
            rel4.setVisibility(View.GONE);
            hf8.setVisibility(View.GONE);
            rel5.setVisibility(View.GONE);
        }

        scrollView = (SnappingHorizontalScrollView) rootView.findViewById(R.id.scroll);
        inflateLayoutsToHorizontalView(scrollView);

        scrollView.setOnScreenSwitchListener(onScreenSwitchListener());

        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("offer_pos: ",""+offer_pos);
              Intent offer=  new Intent(getActivity(),OfferDisplayActivity.class);
                offer.putExtra("offerurl", offersUrls.get(offer_pos) );
                startActivity(offer);
            }
        });

        return rootView;
    }


    /**
     * on horizontal scrollview page changed
     */
    private SnappingHorizontalScrollView.OnScreenSwitchListener onScreenSwitchListener() {
        return new SnappingHorizontalScrollView.OnScreenSwitchListener() {

            @Override
            public void onScreenSwitched(int screen) {
                Log.e("offer_pos: ",""+offer_pos);
                offer_pos = screen;
//                //Toast.makeText(getActivity(), "Page Switched!",
//                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    @SuppressLint("InflateParams")
    public void inflateLayoutsToHorizontalView(ViewGroup layout) {



            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            scrollView.removeAllViews(); // remove old views first

            if(MainActivity.gotObject){
                for (int i = 0; i < offersUrls.size(); i++) {
                    // Inflate layout from xml
                    View view = inflater.inflate(R.layout.list_item_snap_home, null, false);
                    ImageView image = (ImageView) view
                            .findViewById(R.id.hf_offer_image);

                    // set data to view
                    Log.e("Offers: ",i+": "+offersImageUrls.get(i));
                    //new ImageLoadTask(offersImageUrls.get(i), image).execute();

                    Glide
                            .with(this)
                            .load(offersImageUrls.get(i))
                            .centerCrop()
                            .placeholder(R.drawable.offerdummypage)
                            .crossFade()
                            .into(image);

                    scrollView.addView(view);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("offer_pos: ",""+offer_pos);
                            Intent offer=  new Intent(getActivity(),OfferDisplayActivity.class);
                            offer.putExtra("offerurl", offersUrls.get(offer_pos) );
                            startActivity(offer);
                        }
                    });
                }

            }
            else
                {

                        View view = inflater.inflate(R.layout.list_item_snap_home, null, false);
                        ImageView image = (ImageView) view
                                .findViewById(R.id.hf_offer_image);
                        // set data to view
                        Log.e("Offers: ",": no object");
                        scrollView.addView(view);

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



    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

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
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=info.truemd.android"));
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
