package info.truemd.android.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.android.R;
import info.truemd.android.adapter.CustomAndroidGridViewAdapter;
import info.truemd.android.helper.SessionManager;
import io.paperdb.Paper;

public class PreLoginActivity extends AppCompatActivity {
    
    EditText preMobile; TextView truemdTitle, truemdsubTitle; String mobileNo; DilatingDotsProgressBar preloginProgress;
        Button next; public static String mobileNoOnET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        Log.e("introduction:: ", ""+ Paper.book("introduction").read("intro"));
        if(Paper.book("introduction").read("intro").equals("0")) {
            Intent i = new Intent(PreLoginActivity.this, Intro.class);
            startActivity(i);
        }

        Typeface tf_pacifico = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        Typeface tf_l = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        next=(Button) findViewById(R.id.btn_prelogin);

        truemdTitle = (TextView) findViewById(R.id.truemdtitle_tv);
        truemdsubTitle = (TextView) findViewById(R.id.truemdsubtitle_tv);
        preMobile=(EditText) findViewById(R.id.preinput_email);
        preloginProgress=(DilatingDotsProgressBar) findViewById(R.id.prelogin_progress);

        preMobile.setTypeface(tf_l);
        truemdTitle.setTypeface(tf_pacifico);
        truemdsubTitle.setTypeface(tf_l);
        next.setTypeface(tf_l);

        preMobile.setError(null);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );



    next.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validate()) {
                //onLoginFailed("Please fill the details correctly.");
                checkUser();
            }
        }
    });



    }

    public boolean validate() {
        boolean valid = true;

        String email = preMobile.getText().toString();
        String regEx = "^[0-9]{10}$";
        if (email.isEmpty() || email.length()!=10||!email.matches(regEx)) {
            preMobile.setError("enter a valid mobile number");
            valid = false;
        }

       
        return valid;
    }
    
    public void checkUser(){
            String mobile_no = preMobile.getText().toString().trim();
        Log.e("mobile: ",mobile_no);
            mobileNo = mobile_no;
        mobileNoOnET = mobile_no;
            
        try {
            SessionManager session = new SessionManager(PreLoginActivity.this);
            HashMap<String, String> user = session.getUserDetails();
            JsonObjectRequester mRequester = new RequestBuilder(PreLoginActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(true) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(20000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("Content-Type", "application/json")
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToCheckUser()); //or .buildArrayRequester(listener);
                //http://truemd-carebook.rhcloud.com/checkUser?mobile=9581649079
            mRequester.request(Methods.GET, MainActivity.app_url+"/checkUser?mobile="+mobile_no);

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class JsonObjectListenerToCheckUser extends Response.SimpleObjectResponse {


        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                SessionManager session = new SessionManager(PreLoginActivity.this);
                if (jsonObject.toString().length() > 5) {


                    Log.e("PreLogin resp: ", ""+jsonObject);
                    try {

                        String status = jsonObject.getString("status");

                        if(status.equalsIgnoreCase("verified"))
                        {
                            Intent toLogin = new Intent(PreLoginActivity.this, LoginActivity.class);
                            toLogin.putExtra("mobile",mobileNo.trim() );
                            startActivity(toLogin);
                        }
                        else if(status.equalsIgnoreCase("unverified"))
                        {
//                            Intent toLogin = new Intent(PreLoginActivity.this, LoginActivity.class);
//                            toLogin.putExtra("mobile",mobileNo.trim() );
//                            startActivity(toLogin);
                            reverifyUserWithOTP(getApplicationContext(), mobileNo.trim());
                        }
                        else if(status.equalsIgnoreCase("new"))
                        {
//                            Intent toLogin = new Intent(PreLoginActivity.this, LoginActivity.class);
//                            toLogin.putExtra("mobile",mobileNo.trim() );
//                            startActivity(toLogin);

                            forgetPassword(mobileNo.trim());
                        }
                        else if(status.equalsIgnoreCase("unregistered"))
                        {
                            Intent toLogin = new Intent(PreLoginActivity.this, SignupActivity.class);
                            toLogin.putExtra("mobile",mobileNo.trim() );
                            startActivity(toLogin);
                        }
                        else{

                        }

                        preloginProgress.hideNow();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("PreLogin err: ", "in else for length=0");
                    }

                    //lv.setAdapter(new CustomUpcomingOrderAdapter(getApplicationContext(),dateArray,orderNoArray,nameArray, statusArray, dotsArray, gTotalArray, deliveryTimeArray,aljoPreLogins  ));
                }



                else {
                    preloginProgress.hideNow();
                }
            } catch (Exception e) {

                Log.e("PreLoginWeberr: ", e.getMessage());

                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());
            

            preloginProgress.hideNow();
        }



        @Override
        public void onRequestStart(int requestCode) {
            //placeholder.setVisibility(View.INVISIBLE);
            Log.e("PreLogin resp: ", "onRequestStarted()");
            preloginProgress.showNow();
        }




        @Override
        public void onRequestFinish(int requestCode) {
            Log.e("PreLogin resp: ", "onRequestFinish()");



        }
        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());
            if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
            {
                new SweetAlertDialog(PreLoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("The server is taking too long to respond or there might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                PreLoginActivity.this.finish();
                                System.exit(0);

                            }
                        })

                        .show();
            }
            preloginProgress.hideNow();

        }



    }

    void forgetPassword(String mobile){




        if (mobile.isEmpty() || mobile.length()!=10) {
            preMobile.setError("Enter the 10 digit mobile number for which the password is needed");
        } else {
            String regEx = "^[0-9]{10}$";
            if(mobile.matches(regEx));
            {
                preMobile.setError(null);
                //forgetPassword(phoneNumber);

                boolean valid = true;

                Log.e("Login:: ", "forgot password Link clicked"+mobile);

                preloginProgress.showNow();
                if(valid) {

                    String pwdUrl= MainActivity.app_url+"/users/password/new?mobile="+mobile;
                    requestPassword(pwdUrl);
                }



            }
        }



    }
    public void requestPassword(String url){




        try {
            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(PreLoginActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(true) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(50000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    //.addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("Content-Type", "application/json")
                    //.addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToPassword()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, url);




        } catch (Exception e) {
            Log.e("LoginError:: ", e.getMessage());
            e.printStackTrace();

        }


    }


    class JsonObjectListenerToPassword extends com.alirezaafkar.json.requester.interfaces.Response.SimpleObjectResponse {



        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length()>3) {

                    String resp="";String pwdToken="";

                    try {
                        //URL js = new URL(MainActivity.app_url+"/users/password/new?mobile="+mobile);
                        Log.e("Login_FPwd: ", "" + jsonObject.toString());
                        resp = jsonObject.getString("reset_password");
                        if(resp.equalsIgnoreCase("password sent"))
                            pwdToken = jsonObject.getString("reset_password_token");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    if(resp.equalsIgnoreCase("password sent") && pwdToken.length()!=0)
                    {
                        Intent intent = new Intent(getApplicationContext(), SmsForgotPwdActivity.class);
                        intent.putExtra("password-token",pwdToken );
                        startActivityForResult(intent, 0);
                    }
                    else if(resp.equalsIgnoreCase("user not found"))
                    {
                        Toast.makeText(getApplicationContext(),"User Not Found", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), PreLoginActivity.class);
                        startActivityForResult(intent, 0);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                String message = e.getMessage();
                Log.e("PwdError:: ",  message );

            }
        }


        @Override
        public void onRequestStart(int requestCode) {

           preloginProgress.showNow();

        }
        public String trimMessage(String json, String key){
            String trimmedString = null;

            try{
                JSONObject obj = new JSONObject(json);
                trimmedString = obj.getString(key);
            } catch(JSONException e){
                e.printStackTrace();
                return null;
            }

            return trimmedString;
        }

        @Override
        public void onRequestFinish(int requestCode) {

        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());

            preloginProgress.hideNow();

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
            {
                new SweetAlertDialog(PreLoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("The server is taking too long to respond or there might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                PreLoginActivity.this.finish();
                                System.exit(0);

                            }
                        })

                        .show();
            }
           preloginProgress.hideNow();

        }

    }

    public void reverifyUserWithOTP(final Context context, String mobile){

        Log.e("LoginActivity: ","in reverifyUserWithOTP()");




        try {


            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(PreLoginActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(true) //Show error with toast on Network or Server error
                    .shouldCache(false)
                    .timeOut(50000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    //.addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("Content-Type", "application/json")
                    //.addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToReVerifyUser()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, MainActivity.app_url+"/re_verify_user.json?key="+MainActivity.dev_key+"&mobile="+mobile);

            Log.e("verify_user_url: ", MainActivity.app_url+"/re_verify_user.json?key="+MainActivity.dev_key+"&mobile="+mobile);
        } catch (Exception e) {
            Log.e("LoginActivity: ","in reverifyUserWithOTP(): "+ e.getMessage());
            e.printStackTrace();
        }

    }

    class JsonObjectListenerToReVerifyUser extends com.alirezaafkar.json.requester.interfaces.Response.SimpleObjectResponse {


        String verification="";

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {

                    Log.e("verifyUser() resp: ", jsonObject.toString());

                    if (jsonObject.has("verification"))
                    {
                        verification=jsonObject.getString("verification");
                    }

                }

                if(verification.equalsIgnoreCase("no user")) {
                    Toast.makeText(PreLoginActivity.this,  "We couldn't find the number. Please Signup. " , Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity: ", "We couldn't find the number. Please Signup. " + verification);
                }
                else if(verification.equalsIgnoreCase("already verified")) {
                    Toast.makeText(PreLoginActivity.this, mobileNo+ " is already verified. Click forget password to set new password.", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity: ", mobileNo+ " is already verified. Click forget password to set new password." + verification);
                }
                else if(verification.equalsIgnoreCase("otp resent")) {
                    Toast.makeText(PreLoginActivity.this, "OTP Resent on : "+ mobileNo, Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity: ", "verifyUser() verification:: "+mobileNo+" " + verification);
                    String otpToken="";

                    setResult(RESULT_OK, null);
                    if(jsonObject.has("otp_token")) {
                        otpToken = jsonObject.getString("otp_token");
                    }

                    Intent signUpToSms = new Intent(getApplicationContext(), SmsActivity.class);
                    signUpToSms.putExtra("otp_token", otpToken);
                    startActivityForResult(signUpToSms, 0);

                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.e("LoginActivity: ", "in verifyUser(): " + e.getMessage());

            }
        }

        @Override
        public void onRequestStart(int requestCode) {
            preloginProgress.showNow();
            //checkInternetNotLoggedIn(LoginActivity.this);


        }
        public String trimMessage(String json, String key){
            String trimmedString = null;

            try{
                JSONObject obj = new JSONObject(json);
                trimmedString = obj.getString(key);
            } catch(JSONException e){
                e.printStackTrace();
                return null;
            }

            return trimmedString;
        }

        @Override
        public void onRequestFinish(int requestCode) {

        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());
            //onLoginFailed("Uh-oh… There might be a network issue.");

          preloginProgress.hideNow();

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            preloginProgress.hideNow();
            if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
            {
                new SweetAlertDialog(PreLoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("The server is taking too long to respond or there might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                PreLoginActivity.this.finish();
                                System.exit(0);

                            }
                        })

                        .show();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreLoginActivity.this.finish();
        //System.exit(0);
        this.finishAffinity();
    }


}
