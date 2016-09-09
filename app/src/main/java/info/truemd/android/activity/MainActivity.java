package info.truemd.android.activity;

import android.Manifest;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.logentries.logger.AndroidLogger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.Requester;
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
import com.google.android.gms.drive.Drive;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.tramsun.libs.prefcompat.Pref;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.android.BuildConfig;
import info.truemd.android.R;
import info.truemd.android.helper.SessionManager;
import info.truemd.android.helper.TrueMDJSONUtils;
import info.truemd.android.receiver.AlarmReceiver;
import info.truemd.android.service.ConnectionDetector;
import info.truemd.android.service.FCMMessageService;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    static boolean onBackAtNoInternet;
    public static String chatInitializerTrueMDCode = "I44IQI";
    public static String userPinCode = "";
    public static String chatInitializerTrueMDName = "CROCIN 500MG TABLET";
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
    public static String latestMedTrueMDCode = "";
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
    public static String discountMsg, tosUrl,faqsUrl,whyPrescriptionUrl, inviteMsg, about_us, referralMsg="";
    Dialog mBottomSheetDialog;
    public AndroidLogger logger;
    DilatingDotsProgressBar wizProgress;
    ImageView male, female; int sel; EditText email_alt;
    RelativeLayout rl;
    LinearLayout ll;
    int reminderIDOld;
    public static String profileName, profileEmailAlt, profileGender="";

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        Log.e("introduction:: ", ""+Paper.book("introduction").read("intro"));
        if(Paper.book("introduction").read("intro").equals("0")) {
            Intent i = new Intent(MainActivity.this, Intro.class);
            startActivity(i);
        }


//        Pref.init(this);
//        Paper.init(this);
//        //jsonRequester.init
//        Map<String, String> header = new HashMap<>();
//        header.put("charset", "utf-8");
//
//        Requester.Config config = new Requester.Config(getApplicationContext());
//        config.setHeader(header);
//        Requester.init(config);




        mHandler = new Handler();
        discountMsg="";
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

            performActionOPac();
        }
        else
        {

        }


    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        if(id == R.id.action_search){
//            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
//            Intent intent_main_search = new Intent(getApplicationContext(), SearchActivity.class);
//            startActivityForResult(intent_main_search, 0);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
        String title="TrueMD";// = getString(R.string.app_name);
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
                Intent goToPresDetails = new Intent(MainActivity.this, PrescriptionListActivity.class);
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
                fragment = new TrueMDSiriFragment();
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
                    intent_main.putExtra("truemdCode",chatInitializerTrueMDCode+":"+chatInitializerTrueMDName);
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
//                            getSupportActionBar().setTitle("TrueMD");

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
                        String sex = TrueMDJSONUtils.goThroughNullCheck(obj.optString("sex"));
                        String pincode = obj.optString("pincode");

                        profileEmailAlt = email_alt;
                        profileGender = sex;
                        profileName = name;
                        userPinCode = pincode;

                        nameFromGetUser=obj.getString("name");
                        discountMsg=obj.optString("discount_msg");

                        discountMsg = discountMsg.replace("300", "\u20B9 "+"300");

                        Paper.book("user").write("name",nameFromGetUser);
                        Paper.book("user").write("gender",profileGender);
                        Paper.book("user").write("pincode",userPinCode);


                        Log.e("GenderEmail:",nameFromGetUser+" :e: "+email_alt+" s: "+sex);

                        if(sex.isEmpty()||sex.length()==0)
                        {
                            openBottomSheet();
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
//                                "invite_msg": "Download TrueMD app from http://truemd.in",
//                                "referral_msg": "Download TrueMD app from http://truemd.in"
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
                            latestMedTrueMDCode = pres.getJSONObject(0).optJSONArray("meds").optJSONObject(0).optString("truemdCode");
                            Log.e("latestMed:: ", ""+latestMedName);
                            Log.e("latestTMC:: ", ""+latestMedTrueMDCode);

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
                        performActionOPac();

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

    public void openBottomSheet() {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");
         sel=0;

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_gender_email_main, null);



        TextView txth = (TextView)view.findViewById( R.id.tv2);
         male = (ImageView) view.findViewById(R.id.male);
         female = (ImageView) view.findViewById(R.id.female);
        TextView txts = (TextView)view.findViewById( R.id.nospam);
        Button submit = (Button) view.findViewById( R.id.btn_wiz_submit);
        Button skip = (Button) view.findViewById( R.id.btn_wiz_skip);
         email_alt = (EditText) view.findViewById(R.id.wiz_email_alt_ET);

         ll = (LinearLayout) view.findViewById(R.id.wiz_gender_ll);
         rl = (RelativeLayout) view.findViewById(R.id.wiz_email_rl);

        ll.setVisibility(View.VISIBLE);
        rl.setVisibility(View.GONE);

        txth.setTypeface(tf_r);txts.setTypeface(tf_r);

        submit.setTypeface(tf_r);skip.setTypeface(tf_r);

        male.setBackground(getResources().getDrawable(R.drawable.no_bg));
        female.setBackground(getResources().getDrawable(R.drawable.no_bg));

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                male.setBackground(getResources().getDrawable(R.drawable.shape_solid_circle_dark));
                female.setBackground(getResources().getDrawable(R.drawable.no_bg));
                sel=1;
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                male.setBackground(getResources().getDrawable(R.drawable.no_bg));
                female.setBackground(getResources().getDrawable(R.drawable.shape_solid_circle_dark));
                sel=2;
            }
        });

        wizProgress = (DilatingDotsProgressBar) view.findViewById(R.id.wiz_progress);


        mBottomSheetDialog = new Dialog (MainActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog.show();



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_altS = email_alt.getText().toString().trim();
                String gender = "";

                    if (sel != 0 && email_altS.length()>0){



                            if(validateEmail(email_altS)){

                                if (sel == 1)
                                    gender = "male";
                                else if (sel == 2)
                                    gender = "female";

                            ContentValues codecodepair = new ContentValues();

                            codecodepair.put("sex",gender);
                            codecodepair.put("email_alt",email_altS);
                                Paper.book("user").write("gender",gender);

                            try {
                                JSONObject orderJsonObject = getJsonObjectFromContentValues( "user",codecodepair);
                                Log.e("updateUser: ",""+orderJsonObject);
                                updateUser(orderJsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            }
                            else{
                                email_alt.setError("Please enter a valid Email Id.");
                            }




                        }


                else{
                        email_alt.setError("Please enter the email id.");
                    }
            }


        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String email_altS = email_alt.getText().toString().trim();
                String gender = "";

                if (sel != 0) {

                    ContentValues codecodepair = new ContentValues();

                    if (sel == 1)
                        gender = "male";
                    else if (sel == 2)
                        gender = "female";


                    codecodepair.put("sex",gender);
                    Paper.book("user").write("gender",gender);
                    try {
                        JSONObject orderJsonObject = getJsonObjectFromContentValues("user", codecodepair);

                        updateUser(orderJsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Choose one.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



    /**
     * Validate hex with regular expression
     *
     * @param hex
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validateEmail(final String hex) {

        pattern = Pattern.compile(EMAIL_PATTERN);

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }



    public void updateUser(JSONObject orderJsonObject){


        try {
            SessionManager session = new SessionManager(MainActivity.this);

            HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester = new RequestBuilder(MainActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(20000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                     .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("Content-Type","application/json")
                     .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToUpdateUser()); //or .buildArrayRequester(listener);
            //http://truemd-carebook.rhcloud.com/checkUser?mobile=9581649079
            mRequester.request(Methods.PUT, app_url+"/updateUser.json", orderJsonObject);

        }  catch (Exception e) {
            Log.e("updateUser: ",""+e.getMessage());
            e.printStackTrace();
        }

    }


    private class JsonObjectListenerToUpdateUser extends Response.SimpleObjectResponse {


        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {

                if (jsonObject.toString().length() > 5) {


                    Log.e("PreLogin resp: ", ""+jsonObject);
                    try {



                        wizProgress.hideNow();
                        if(mBottomSheetDialog.isShowing())
                            mBottomSheetDialog.dismiss();

                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Awesome..!!")
                                .setContentText("")
                                .setConfirmText("OK")
                                .show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("PreLogin err: ", ""+e.getMessage());
                        if(mBottomSheetDialog.isShowing())
                            mBottomSheetDialog.dismiss();
                    }

                    //lv.setAdapter(new CustomUpcomingOrderAdapter(getApplicationContext(),dateArray,orderNoArray,nameArray, statusArray, dotsArray, gTotalArray, deliveryTimeArray,aljoPreLogins  ));
                }



                else {
                    wizProgress.hideNow();
                    if(mBottomSheetDialog.isShowing())
                        mBottomSheetDialog.dismiss();
                }
            } catch (Exception e) {

                Log.e("PreLoginWeberr: ", e.getMessage());

                e.printStackTrace();
            }
        }




        @Override
        public void onRequestStart(int requestCode) {
            //placeholder.setVisibility(View.INVISIBLE);
            Log.e("PreLogin resp: ", "onRequestStarted()");
            wizProgress.showNow();

        }




        @Override
        public void onRequestFinish(int requestCode) {
            Log.e("PreLogin resp: ", "onRequestFinish()");
            if(mBottomSheetDialog.isShowing())
                mBottomSheetDialog.dismiss();



        }
        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());


            wizProgress.hideNow();

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            wizProgress.hideNow();
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


    private void performActionOPac() {

        Log.e("Noti:::: ", "performActionOPac");

//        Intent intent = new Intent(this, PrescriptionListActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(messageBody[0])
//                .setContentText(messageBody[4])
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        //scheduleAlarm(30000,1);
        //scheduleAlarm(60000,2);
//        setAlarmForTime("2016-07-10T12:22:00.001Z", 1);
//        setAlarmForTime("2016-07-10T12:23:00.001Z",2);
//        setAlarmForTime("2016-07-10T12:24:00.001Z",3);
//        setAlarmForTime("2016-07-10T12:25:00.001Z",4);
//        setAlarmForTime("2016-07-10T12:26:00.001Z",5);
//        setAlarmForTime("2016-07-10T12:27:00.001Z",6);
//        setAlarmForTime("2016-07-10T12:28:00.001Z",7);
//        setAlarmForTime("2016-07-10T12:29:00.001Z",8);
//        setAlarmForTime("2016-07-10T12:30:00.001Z",9);
//        setAlarmForTime("2016-07-10T12:31:00.001Z", 10);
        getPouches();


    }

    void setAlarmForTime(String forTime, int i){

        long longtime=0;

        cancelAllAlarms(MainActivity.this, 10);

        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                    .withLocale(Locale.ROOT)
                    .withChronology(ISOChronology.getInstanceUTC());

            DateTime dateA = formatter.parseDateTime(forTime);
            DateTime current = DateTime.now();
            Log.e("setReminderPouch: ", "datetime: "+dateA+" current: "+current);

            if(dateA.compareTo(current)>0) {
                //pouchesToShow.put(pouch);
                //Log.e("setReminderPouch: ", "datetime: "+dateA+" current: "+current);
                long getTimeToAlarmMilliSec = dateA.getMillis()-current.getMillis();
                Log.e("setReminderPouch: ", ""+getTimeToAlarmMilliSec);
                scheduleAlarm(getTimeToAlarmMilliSec,i);

            }


        } catch (Exception e) {

            Log.e("setReminderPouch: ", e.getMessage());
        }


    }

    public boolean setRemindersFromPouches(JSONArray pouches) throws JSONException
    {

        boolean success = false; int id=0;

        for (int i=0; i<pouches.length();i++)
        {
            JSONObject pouch = pouches.getJSONObject(i);
            if(pouch.has("datetime"))
            {
                String datetime = pouch.getString("datetime");
                Log.e("setReminderPouch: ", "datetime string: "+datetime);
                DateTime current = DateTime.now();
                DateTime dateA = new DateTime();

                try {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                            .withLocale(Locale.ROOT)
                            .withChronology(ISOChronology.getInstanceUTC());

                    dateA = formatter.parseDateTime(datetime).minusMillis(19800000);

                    if(dateA.compareTo(current)>0) {
                        //pouchesToShow.put(pouch);
                        Log.e("setReminderPouch: ", "datetime: "+dateA+" current: "+current);
                        long getTimeToAlarmMilliSec = dateA.getMillis()-current.getMillis();
                        Log.e("setReminderPouch: ", ""+getTimeToAlarmMilliSec);
                        //String remID= Paper.book("reminder_id").read("id");
                        //int id = Integer.parseInt(remID);

                        scheduleAlarm(getTimeToAlarmMilliSec,id);
                        id++;

                        Paper.book("reminder_id").write("id", ""+id);
                    }


                } catch (Exception e) {
                    //do something
                    success=false;
                    Log.e("setReminderPouch: ", e.getMessage());
                }
            }
            else {

                continue;
            }

            success=true;
        }

        return success;
    }

    public  void cancelAllAlarms(Context context, int id)
    {
        for(int alarm_id=0;alarm_id<id;alarm_id++) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intentAlarm = new Intent(context, AlarmReceiver.class);
            //PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 0, updateServiceIntent, 0);
            PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(this,id,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            // Cancel alarms
            try {
                alarmManager.cancel(pendingUpdateIntent);
            } catch (Exception e) {
                Log.e(TAG, "AlarmManager update was not canceled. " + e.toString());
            }
        }
    }
    public  void scheduleAlarm(long time, int id)
    {
        // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
        // we fetch  the current time in milliseconds and added 1 day time
        // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day

        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers and
        //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        intentAlarm.putExtra("id",""+id);

        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time

        if (Build.VERSION.SDK_INT >= 19) {

            long alarmscheduletime= DateTime.now().getMillis()+time;

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmscheduletime, PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

            DateTime alarm = new DateTime(alarmscheduletime);

            Log.e(""+ id+ " Alarm Scheduled>=19: ",""+alarm+" : "+DateTime.now());

            Log.e(""+ id+ " Alarm Scheduled>=19: ",""+alarm+" : "+DateTime.now());
        }else {
            long alarmscheduletime= DateTime.now().getMillis()+time;

            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmscheduletime, PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

            DateTime alarm = new DateTime(alarmscheduletime);

            Log.e(""+ id+" Alarm Scheduled: ",""+alarm+" : "+DateTime.now());
        }
        //alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,id,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));


    }

    void getPouches(){

        Log.e("MainActivity: ","in checkAuthentication()");
        SessionManager session = new SessionManager(MainActivity.this);
        HashMap<String, String> user = session.getUserDetails();

        try {


            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(MainActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(false)
                    .timeOut(50000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("Content-Type", "application/json")
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToGetPouches()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, app_url+"/pouches.json?after_now=true&mobile="+user.get(SessionManager.KEY_MOBILE_UM));


        } catch (Exception e) {
            Log.e("MainActivity: ","in checkAuthentication(): "+ e.getMessage());
            e.printStackTrace();
        }
    }

    class JsonObjectListenerToGetPouches extends Response.SimpleObjectResponse {


        boolean success=false;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {

            SessionManager sessionManager= new SessionManager(MainActivity.this);
            HashMap<String, String> user = sessionManager.getUserDetails();
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {

                    Log.e("getPouches() resp: ", jsonObject.toString());
                    JSONArray pouches = jsonObject.getJSONArray("pouches");
                    Paper.book("reminder").write(""+user.get(SessionManager.KEY_MOBILE_UM),jsonObject.toString());
                    setRemindersFromPouches(pouches);
                    Log.e("FCMinGetPouches:","Reminders set:"+ Paper.book("reminder_id").read("id"));
                    //cancelAllAlarms(MainActivity.this, reminderIDOld);
                    Log.e("FCMinGetPouches:","Reminders cancelled:"+reminderIDOld);




                }



            } catch (Exception e) {
                e.printStackTrace();
                Log.e("FCMinGetPouches:","Reminders set error:"+ Paper.book("reminder_id").read("id"));
                Log.e("FCMMessageService: ","in getPouches(): "+e.getMessage());

            }
        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("FCMonErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());

        }


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