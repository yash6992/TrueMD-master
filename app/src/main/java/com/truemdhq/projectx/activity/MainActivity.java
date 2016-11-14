package com.truemdhq.projectx.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.logentries.logger.AndroidLogger;

import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.truemdhq.projectx.model.Client;
import com.truemdhq.projectx.model.Invoice;
import com.truemdhq.projectx.model.InvoiceChild;
import com.truemdhq.projectx.model.InvoiceParent;
import com.truemdhq.projectx.model.Item;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.helper.SessionManager;
import com.truemdhq.projectx.helper.ProjectXJSONUtils;
import com.truemdhq.projectx.service.ConnectionDetector;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    static boolean onBackAtNoInternet;
    public static String chatInitializerProjectXCode = "I44IQI";
    public static String userPinCode = "";
    public static String userPinCodeCity = "";
    public static String chatInitializerProjectXName = "CROCIN 500MG TABLET";
    public static boolean fromMedicineDetailsChat = false;
    public static boolean fromHomeToChat = false;
    public static boolean fromThankYou = false;
    public static String userName_mobile = "Guest           ";
    public FragmentDrawer drawerFragment;
    public HomeFragment homeFragment;
    public static String nameFromGetUser = "";
    public final static String dev_key = "68e8d8e774be34514a6fac97b8844b60";
    public final static String app_url = "http://production-truemdhq.rhcloud.com";
    private DrawerLayout mDrawer; public LinearLayout blocker;
    public static JSONObject getUserObject;
    public static boolean reminderOn;
    public static String latestMedName = "";
    public static String latestMedProjectXCode = "";
    public static boolean showRateUs = false;
    public static boolean chatGhostOn = false;
    public static boolean prescriptionGhostOn = false;
    public static boolean gotObject = false;
    private GoogleApiClient mGoogleApiClient;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    public static String FCMToken;
    DilatingDotsProgressBar homeProgress;
    private Handler mHandler;
    static int s_position;
    public static String discountMsg,shippingMsg, tosUrl,faqsUrl,whyPrescriptionUrl, inviteMsg, about_us, referralMsg="";
    Dialog mBottomSheetDialog;
    public AndroidLogger logger;
    DilatingDotsProgressBar wizProgress;
    ImageView male, female; int sel; EditText email_alt;
    RelativeLayout rl;
    LinearLayout ll;
    int reminderIDOld;
    public static String profileName, profileEmailAlt, profileGender="";
    //public static List<ParentListItem> listOfDummyInvoices;

    private static final String STATE_RESOLVING_ERROR = "resolving_error";


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }



    // Session Manager Class
    public SessionManager session;

    int position_Main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("introduction0:: ", ""+Paper.book("introduction").read("intro"));

        super.onCreate(savedInstanceState);
        Log.e("introduction1:: ", ""+Paper.book("introduction").read("intro"));

        Log.e("introduction2:: ", ""+Paper.book("introduction").read("intro"));

        setContentView(R.layout.activity_main);

        
        Log.e("introduction3:: ", ""+Paper.book("introduction").read("intro"));

        if(Paper.book("introduction").read("intro").equals("0")) {
            Intent i = new Intent(MainActivity.this, Intro.class);
            startActivity(i);
        }




        mHandler = new Handler();
        discountMsg="";shippingMsg="";
        onBackAtNoInternet=false; getUserObject = new JSONObject();

        Log.d("MainActivity: ", "before intent");

        position_Main=0;

        // Session class instance
        session = new SessionManager(getApplicationContext());

        session.checkLogin();


        logger = null;
        try {
            logger =  AndroidLogger.createInstance(getApplicationContext(), true, false, false, null, 0, "68228ceb-ad8f-428c-846a-eb5942f6a2fa", true);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("AndroidLogger",e.getMessage());
        }

        HashMap<String, String> user = session.getUserDetails();

        // name
        String mobile = user.get(SessionManager.KEY_MOBILE_UM);

        String password = user.get(SessionManager.KEY_PASSWORD_UM);

        // email
        String authentication = user.get(SessionManager.KEY_AUTHENTICATION_UM);

        String chatId = user.get(SessionManager.KEY_CHAT_ID_UM);

        userName_mobile = mobile;

        try {
          //  reminderIDOld = Integer.parseInt(""+Paper.book("reminder_id").read("id"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }



        Log.e("Shared Pref: ", ""+ "User: "+mobile+" :: " +authentication+" :: ");


        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        drawerFragment.setDrawerListener(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        homeProgress = (DilatingDotsProgressBar) findViewById(R.id.home_progress);

        blocker = (LinearLayout) findViewById(R.id.blocker);



        //wizProgress = (DilatingDotsProgressBar) findViewById(R.id.wiz_progress);

        // display the first navigation drawer view on app launch

        if(fromMedicineDetailsChat||fromThankYou||fromHomeToChat)
        {displayView(3); }
        else
        {displayView(0);}

//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED
//                ) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        0);
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        0);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }

        if(authentication.length()<6) {
            //Toast.makeText(MainActivity.this, "session expired. you need to login again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), PreLoginActivity.class);
            startActivityForResult(intent, 0);
        }

        List<String> allKeysFCM = Paper.book("FCM").getAllKeys();
        if(allKeysFCM.contains("refreshedToken"))
        {
            FCMToken = Paper.book("FCM").read("refreshedToken");

        }
        else
        FCMToken = "";

        Log.e("MainReminder:", "FCMToken:: "+FCMToken);


        if(session.isLoggedIn())
            checkAuthentication();

        //set reminder initialization on paper.
        List<String> allKeys = Paper.book("reminder").getAllKeys();
        if(allKeys.contains("reminderOn"))
        {
            String reminderOnS = Paper.book("reminder").read("reminderOn");
            if(reminderOnS.equalsIgnoreCase("true"))
                reminderOn=true;
            else
                reminderOn=false;
        }
        else
        {

            Paper.book("reminder").write("reminderOn", "true");
            reminderOn=true;

        }
        Log.e("MainReminder:", ""+reminderOn);

        //set notification initialization on paper.
        List<String> allKeysnoti = Paper.book("notification").getAllKeys();
        if(allKeysnoti.contains(""+user.get(SessionManager.KEY_MOBILE_UM)))
        {
            String notificationJarray = Paper.book("notification").read(""+""+user.get(SessionManager.KEY_MOBILE_UM));
            Log.e("MainNotification:", notificationJarray);

        }
        else
        {
            Paper.book("notification").write(""+user.get(SessionManager.KEY_MOBILE_UM) , new JSONArray().toString());
            Log.e("MainNotification:", "");
        }
        Log.e("MainNotification:", ""+Paper.book("notification").read(""+user.get(SessionManager.KEY_MOBILE_UM)));

        String check =Paper.book("introduction").read("preshelp","0");

        if(check.equalsIgnoreCase("0")){
            Log.e("Main:", "performing o pac ");


        }
        else
        {

        }


    }


    @Override
    public void onDrawerItemSelected(View view, int position) {



      s_position=position;


        new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                    displayView(s_position);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }


    public void displayView(int position) {
        Fragment fragment = null;
        String title="ProjectX";// = getString(R.string.app_name);
        switch (position) {
            case 0:
                Paper.book("nav").write("selected","0");
                fragment = new HomeFragment();
                homeFragment = (HomeFragment) fragment;
                title = "Home";
                position_Main=0;
                break;
            case 1:
                Paper.book("nav").write("selected","1");
                Intent goToPresDetails = new Intent(MainActivity.this, ClientListActivity.class);
                startActivity(goToPresDetails);
                break;
            case 2:
                Paper.book("nav").write("selected","2");
                Intent goToOrderDetails = new Intent(MainActivity.this, OrderDetailsActivity.class);
                goToOrderDetails.putExtra("to",0);
                startActivity(goToOrderDetails);
                break;
            case 3:
                Paper.book("nav").write("selected","3");
                fragment = new ProjectXSiriFragment();
                position_Main=3;
                break;
            case 4:
                Paper.book("nav").write("selected","4");
                fragment = new InviteFriendsFragment();
                position_Main=4;
                break;
            case 5:
                Paper.book("nav").write("selected","5");
                fragment = new HelpFragment();
                position_Main=5;
                break;
            case 6:
                Paper.book("nav").write("selected","6");
                fragment = new AboutUsFragment();
                position_Main=6;
                break;
            case 7:

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MainActivity.tosUrl));
                //finish();
                startActivity(browserIntent);
                break;
            case 8:
                //Paper.book("nav").write("selected","0");
                finish();
                startActivity(new Intent(MainActivity.this, PreLoginActivity.class));
                logout();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            //fragmentTransaction.commitAllowingStateLoss();


        }
    }

    public void open()
    {
        mDrawer.openDrawer(GravityCompat.START);

    }

    /**
     * Logging out user
     * will clear all user shared preferences and navigate to
     * sms activation screen
     */
    private void logout() {
        session.logoutUser();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

            if (position_Main != 0) {

                finish();

                if(fromMedicineDetailsChat) {
                    fromMedicineDetailsChat = false;
                    Intent intent_main = new Intent(getApplicationContext(), MedicineDetailsActivity2.class);
                    intent_main.putExtra("truemdCode",chatInitializerProjectXCode+":"+chatInitializerProjectXName);
                    intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getApplicationContext().startActivity(intent_main);

                }
                else if(fromHomeToChat) {
                    fromHomeToChat = false;
                    Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                    intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getApplicationContext().startActivity(intent_main);

                }
                else
                {
                    Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                    intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getApplicationContext().startActivity(intent_main);

                }


            } else {
                int count = getFragmentManager().getBackStackEntryCount();
                Log.e("countStackBack: ",""+count);
              //  this.doubleBackToExitPressedOnce = true;
                //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                if (count == 0) {


                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("This will exit the application!")
                                .setCancelText("No,cancel pls!")
                                .setConfirmText("Yes,exit!")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {


                                        MainActivity.this.finishAffinity();
                                       // System.exit(0);

                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                    }
                                })
                                .show();

                        //additional code
                }



                    return;
                }

                this.doubleBackToExitPressedOnce = true;
               // Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }



    public void checkInternet(final Context context){
        ConnectionDetector cd = new ConnectionDetector(context);

        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
        if (!isInternetPresent) {

            SweetAlertDialog sdialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                    sdialog.setTitleText("Oops...No Internet Connection!");
                    sdialog.setContentText("This service needs an internet connection. Please try again");

                    sdialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
//                            sDialog
//                                    .setTitleText("Deleted!")
//                                    .setContentText("Your imaginary file has been deleted!")
//                                    .setConfirmText("OK")
//                                    .setConfirmClickListener(null)
//                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);

//                            FragmentManager fragmentManager = getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            fragmentTransaction.replace(R.id.container_body, new HomeFragment());
//                            //fragmentTransaction.commit();
//                            fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commitAllowingStateLoss();
//
//                            // set the toolbar title
//                            getSupportActionBar().setTitle("ProjectX");

                            finish();

                            Intent intent_main = new Intent(context, MainActivity.class);
                            intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            if (context.getClass().equals(MainActivity.class))
                                context.startActivity(intent_main);
                            else
                                context.startActivity(intent_main);
                        }
                    });

                    sdialog.show();

            sdialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        finish();

                        Intent intent_main = new Intent(context, MainActivity.class);
                        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        if (context.getClass().equals(MainActivity.class))
                            context.startActivity(intent_main);
                        else
                            context.startActivity(intent_main);
                    }
                    return true;
                }
            });
        }
    }



    public void checkAuthentication() {

        Log.e("MainActivity: ","in checkAuthentication()");

        HashMap<String, String> user = session.getUserDetails();

            setBlockUser();

        try {


            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(MainActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(false)
                    .timeOut(20000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                            //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("Content-Type", "application/json")
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToGetUser()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, app_url+"/getUser.json?fcm_key="+FCMToken);


        } catch (Exception e) {
            Log.e("MainActivity: ","in checkAuthentication(): "+ e.getMessage());
            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops..!!")
                    .setContentText("Something doesn't seem right. Please reopen the app.")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            //MainActivity.this.finish();
                            MainActivity.this.finishAffinity();
                            //System.exit(0);

                        }
                    })

                    .show();
            e.printStackTrace();
        }


        }

    class JsonObjectListenerToGetUser extends Response.SimpleObjectResponse {


        boolean success=false;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {

                    Log.e("getUser() resp: ", jsonObject.toString());
                    success = true;
                    JSONObject obj = jsonObject;
                    getUserObject = obj;

                    try {
                        String email_alt = obj.optString("email_alt");
                        String name = obj.optString("name");
                        String sex = ProjectXJSONUtils.goThroughNullCheck(obj.optString("sex"));
                        String pincode = obj.optString("pincode");

                        profileEmailAlt = email_alt;
                        profileGender = sex;
                        profileName = name;
                        userPinCode = pincode;

                        nameFromGetUser=obj.getString("name");
                        discountMsg=obj.optString("discount_msg");
                        shippingMsg=obj.optString("shipping_msg");

                        //discountMsg = discountMsg.replace("300", "\u20B9 "+"300");

                        Paper.book("user").write("name",nameFromGetUser);
                        Paper.book("user").write("gender",profileGender);
                        Paper.book("user").write("pincode",userPinCode);
                        //Paper.book("user").write("pincode_city", userPinCodeCity);

                       String rateus =  Paper.book("introduction").read("rateus","0").toString();

                        String counterss = Paper.book("introduction").read("ratecounter").toString();

                        Log.e("RATING:", " "+rateus+" "+counterss+" "+showRateUs);

                        //int ratecounter = 1;
                        int ratecounter =  Integer.parseInt(""+counterss);

                        ratecounter++;

                        Log.e("RATING:", " "+rateus+" "+ratecounter+" "+showRateUs);


                        if(rateus.equalsIgnoreCase("0") && ratecounter%5==0)
                            showRateUs=true;
                        else if(rateus.equalsIgnoreCase("1"))
                            showRateUs=false;
                        else
                            showRateUs=false;



                        Paper.book("introduction").write("ratecounter",""+ratecounter);

                        Log.e("GenderEmail:",nameFromGetUser+" :e: "+email_alt+" s: "+sex);

                        if(sex.isEmpty()||sex.length()==0)
                        {

                        }
                    } catch (Exception e) {
                        Log.e("GenderErr:",""+e.getMessage());
                        e.printStackTrace();
                    }


                    String mobile = obj.getString("mobile");

//                    "static": {
//                        "tos_url": url+"/static/tos.json",
//                                "whyprescription_url": url+"/static/whyprescription.json",
//                                "faqs_url": url+"/static/faqs.json",
//                                "invite_msg": "Download ProjectX app from http://truemd.in",
//                                "referral_msg": "Download ProjectX app from http://truemd.in"
//                    }

                    JSONObject staticStrings= obj.optJSONObject("static");

                    tosUrl=staticStrings.optString("tos_url");
                    whyPrescriptionUrl=staticStrings.optString("whyprescription_url");
                    faqsUrl=staticStrings.optString("faqs_url");
                    inviteMsg=staticStrings.optString("invite_msg");
                    referralMsg=staticStrings.optString("referral_msg");
                    about_us=staticStrings.optString("about_us");

                    userName_mobile = mobile;
                    Log.e("in getUser", ""+ profileName +" "+profileGender+" "+ profileEmailAlt);

                    ContentValues codecodepair = new ContentValues();
                    codecodepair.put("mobile", mobile);
                    codecodepair.put("name", nameFromGetUser);

                        JSONObject miniUser = getJsonObjectFromContentValues("mini_user", codecodepair);

                    logger.log(""+miniUser);
                    logger.log("Welcome: "+mobile);

                    Log.e("in getUser", "2");
                    //save in paper
                    Paper.book("user").write("object", getUserObject.toString());

                    if(obj.getJSONObject("patient").has("prescriptions"))
                    {
                        Log.e("in getUser presIf", "1");
                        JSONArray pres = obj.getJSONObject("patient").getJSONArray("prescriptions");
                        Log.e("presArrayLength:: ", ""+pres.length());
                      if(pres.length()>=1){
                          chatGhostOn=true;
                          prescriptionGhostOn=true;
                          Log.e("presArrayLength:: ", "pres found");

                            latestMedName = pres.getJSONObject(0).optJSONArray("meds").optJSONObject(0).optString("med_name");
                            latestMedProjectXCode = pres.getJSONObject(0).optJSONArray("meds").optJSONObject(0).optString("truemdCode");
                            Log.e("latestMed:: ", ""+latestMedName);
                            Log.e("latestTMC:: ", ""+latestMedProjectXCode);

                        }
                        else {
                            chatGhostOn=false;
                            prescriptionGhostOn=false;
                        }
                    }
                    else {
                        Log.e("presArrayLength:: ", "no pres");

                        chatGhostOn=false;
                        prescriptionGhostOn=false;
                    }


                }

                if (success)
                {
                    //Toast.makeText(MainActivity.this, nameFromGetUser, Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity: ","checkAuthentication() success:: "+ getUserObject.toString().length());

                    //setRemindersFromGetUser(getUserObject);
                    gotObject=true;
                    Log.e("chatGhostOn:: ", ""+chatGhostOn);
                    Log.e("gotObject:: ", ""+gotObject);
                    Log.e("presGhostOn:: ", ""+prescriptionGhostOn);

                    if(fromMedicineDetailsChat||fromThankYou||fromHomeToChat)
                    {displayView(3); }
                    else
                    {displayView(0);}
                    drawerFragment = (FragmentDrawer)
                            getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
                    drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
                    drawerFragment.setDrawerListener(MainActivity.this);
                    cancelBlockUser();

                }
                else {
                    reauthenticate(MainActivity.this);

                    //logout();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MainActivity: ","in checkAuthentication(): "+e.getMessage());
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("Something doesn't seem right. Please reopen the app.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
//MainActivity.this.finish();
                                MainActivity.this.finishAffinity();
                                //System.exit(0);

                            }
                        })

                        .show();
                //reauthenticate(MainActivity.this);
                //logout();
            }
        }



        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());
            reauthenticate(MainActivity.this);
            //logout();
        }

        @Override
        public void onRequestStart(int requestCode) {

            Log.e("getUser resp: ", "onRequestStarted()");

        }




        @Override
        public void onRequestFinish(int requestCode) {
            Log.e("getUser resp: ", "onRequestFinish()");


        }
        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());

            if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
            {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("There might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                //MainActivity.this.finish();
                                MainActivity.this.finishAffinity();
                                //System.exit(0);
                            }
                        })

                        .show();
            }


        }


    }




    public void reauthenticate(final Context context){


        Log.e("MainActivity: ","in reauthenticate()");

        HashMap<String, String> user = session.getUserDetails();

        String mobileL = user.get(SessionManager.KEY_MOBILE_UM);
        String passwordL = user.get(SessionManager.KEY_PASSWORD_UM);

        loginAgain(mobileL, passwordL);



    }


    public void loginAgain(String mobile, String password){

        ContentValues codecodepair = new ContentValues();
        codecodepair.put("mobile", mobile);
        codecodepair.put("password", password);
        Log.e("MainActivity: ","in loginAgain() "+ codecodepair.toString());

        try {
            JSONObject couponJsonObject = getJsonObjectFromContentValues("user", codecodepair);

            HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(MainActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(false)
                    .timeOut(20000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("Content-Type", "application/json")
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToLoginAgain()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.POST, app_url+"/users/sign_in.json", couponJsonObject);




        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MainActivity: ","in loginAgain(): "+e.getMessage());
            //Toast.makeText(MainActivity.this, "session expired. you need to login again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), PreLoginActivity.class);
            startActivityForResult(intent, 0);
        }catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity: ","in loginAgain(): "+e.getMessage());
            //Toast.makeText(MainActivity.this, "session expired. you need to login again", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), PreLoginActivity.class);
            startActivityForResult(intent, 0);
        }


    }

        class JsonObjectListenerToLoginAgain extends Response.SimpleObjectResponse {

            String authentication_token_new;
            boolean success;

            @Override
            public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
                //Ok
                try {
                    if (jsonObject.getString("status").equalsIgnoreCase("ok")) {

                        Log.e("loginAgain() resp: ", jsonObject.toString());
                        success = true;
                        JSONObject obj = jsonObject;
                        authentication_token_new = obj.getString("authentication_token");
                        JSONObject userobj = obj.getJSONObject("user");

                        nameFromGetUser = userobj.getString("name");
                        String mobile = userobj.getString("mobile");

                        userName_mobile = mobile;

                    }


                    HashMap<String, String> user = session.getUserDetails();

                    String mobileL = user.get(SessionManager.KEY_MOBILE_UM);
                    String passwordL = user.get(SessionManager.KEY_PASSWORD_UM);
                    String chatIdL = user.get(SessionManager.KEY_CHAT_ID_UM);
                    //String authenticationL = user.get(SessionManager.KEY_AUTHENTICATION_UM);

                    if (success && authentication_token_new.length()>6) {
                        session.createLoginSession(mobileL, authentication_token_new, passwordL, chatIdL);
                        //
                        // Toast.makeText(MainActivity.this, nameFromGetUser, Toast.LENGTH_SHORT).show();


                        checkAuthentication();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), PreLoginActivity.class);
                        startActivityForResult(intent, 0);
                        //Toast.makeText(MainActivity.this, "session expired. you need to login again", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(getApplicationContext(), PreLoginActivity.class);
                    startActivityForResult(intent, 0);
                }
            }

            @Override
            public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
                //Error (Not server or network error)
                Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());

                Intent intent = new Intent(getApplicationContext(), PreLoginActivity.class);
                startActivityForResult(intent, 0);
            }


            @Override
            public void onRequestStart(int requestCode) {

                Log.e("LoginAgain resp: ", "onRequestStarted()");

            }




            @Override
            public void onRequestFinish(int requestCode) {
                Log.e("LoginAgain resp: ", "onRequestFinish()");


            }
            @Override
            public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
                //Network or Server error
                Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());

                if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
                {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Oops..!!")
                            .setContentText("There might be an issue with your internet connection.\n Try after some time.")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    //MainActivity.this.finish();
                                    MainActivity.this.finishAffinity();
                                    //System.exit(0);
                                }
                            })

                            .show();
                }


            }

        }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);

            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity) getActivity()).onDialogDismissed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;

    }

    private static JSONObject getJsonObjectFromContentValues(String key_tag, ContentValues params) throws JSONException {

        //Stores JSON
        JSONObject holder = new JSONObject();


        //object for storing Json
        JSONObject data = new JSONObject();


        for (Map.Entry<String, Object> entry : params.valueSet()) {
            String key = entry.getKey(); // name
            String value = entry.getValue().toString(); // value
            data.put(key, value);
        }

        //puts email and 'foo@bar.com'  together in map
        holder.put(key_tag, data);

        return holder;
    }




    public void setBlockUser()
    {
        homeProgress.showNow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        blocker.setVisibility(View.VISIBLE);

    }
    public void cancelBlockUser ()
    {
        homeProgress.hideNow();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        blocker.setVisibility(View.GONE);

    }






}