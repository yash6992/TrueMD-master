package com.truemdhq.projectx.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.helper.SessionManager;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 02/03/16.
 */
public class ForgetPwdActivity extends AppCompatActivity {


    EditText changedPwd, confirmChangedPwd;
    TextView otpTextView, truemdTitle, truemdsubTitle;
    String pwd, confirmpwd, otp, resetPwdToken;
    Button confirmPwdChange; private ProgressDialog pDialog;
    ScrollView ll_forgetpwd; DilatingDotsProgressBar passwordProgress;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);

        changedPwd = (EditText) findViewById(R.id.input_change_password);
        confirmChangedPwd = (EditText) findViewById(R.id.input_confirm_password);
        confirmPwdChange = (Button) findViewById(R.id.btn_change_password);
        otpTextView = (TextView) findViewById(R.id.otpTextView);
        ll_forgetpwd = (ScrollView) findViewById(R.id.sv_forgetpwd);
        passwordProgress = (DilatingDotsProgressBar) findViewById(R.id.password_progress);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        //Typeface tf_r = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Montserrat-Bold.ttf");
        //otpTextView.setTypeface(tf_r);
        truemdTitle = (TextView) findViewById(R.id.truemdtitle_tv);
        truemdsubTitle = (TextView) findViewById(R.id.truemdsubtitle_tv);

        Typeface tf_pacifico = Typeface.createFromAsset(getAssets(), "MarkOffcPro-Bold.ttf");
        Typeface tf_l = Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");

        truemdTitle.setTypeface(tf_pacifico);truemdsubTitle.setTypeface(tf_l);
        confirmChangedPwd.setTypeface(tf_l);


        pDialog = new ProgressDialog(ForgetPwdActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Changing Password...");


        Intent fromSmsForgotActivity = getIntent();
        otp = fromSmsForgotActivity.getStringExtra("otp");
        resetPwdToken = fromSmsForgotActivity.getStringExtra("password-token");

        otpTextView.setText("OTP: " + otp);

        confirmPwdChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ForgetPwdActivity activity
                boolean valid = true;

                String password = changedPwd.getText().toString();
                String confirmPassword = confirmChangedPwd.getText().toString();

                if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                    changedPwd.setError("between 4 and 10 alphanumeric characters");
                    valid = false;
                } else {
                    changedPwd.setError(null);

                    if (confirmPassword.equals(password)) {
                        changePassword();
                    } else {
                        confirmChangedPwd.setError("passwords not matching");
                        valid = false;

                    }
                }


            }
        });
    }

    private void showpDialog() {
        passwordProgress.showNow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hidepDialog() {
        passwordProgress.hideNow();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    void changePassword() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(changedPwd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(confirmChangedPwd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


        String pwdChangeUrl = MainActivity.app_url+"/reset_password.json?reset_password_token=" + resetPwdToken + "&password=" + confirmChangedPwd.getText().toString() + "&password_confirmation=" + changedPwd.getText().toString() + "&otp=" + otp + "&key=" + MainActivity.dev_key;

        requestChangePassword(pwdChangeUrl);

    }
    @Override
    public void onBackPressed(){


        Intent signUpToLogin = new Intent(getApplicationContext(), PreLoginActivity.class);

        startActivityForResult(signUpToLogin, 0);

    }

    public void passwordNotChanged(){
        Toast.makeText(getApplicationContext(),"Uh-oh… There might be a network issue.", Toast.LENGTH_SHORT).show();

        hidepDialog();

        Intent passwordNotChanged = new Intent(ForgetPwdActivity.this, PreLoginActivity.class);
        startActivity(passwordNotChanged);
    }




    public void requestChangePassword(String url){




        try {
            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(getApplicationContext())
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
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
            Toast.makeText(getApplicationContext(), "Some error occured, try again please.", Toast.LENGTH_SHORT).show();

        }


    }


    class JsonObjectListenerToPassword extends com.alirezaafkar.json.requester.interfaces.Response.SimpleObjectResponse {



        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length()>3) {

                    try {

                        Log.e("ForgetPwdActivity: ", jsonObject.toString());
                        String reset_password = jsonObject.getString("reset_password");

                                if (reset_password.equalsIgnoreCase("invalid password token"))
                                {
                                    Log.e("FPwdActivity_verifyOtp", "" + jsonObject.toString());
                                    Toast.makeText(getApplicationContext(), "Some error occured. Going back to Login Screen", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent smsToSignup = new Intent(getApplicationContext(), PreLoginActivity.class);
                                    smsToSignup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    hidepDialog();
                                    startActivity(smsToSignup);
                                }
                                else if (reset_password.equalsIgnoreCase("invalid otp"))
                                {
                                    Log.e("FPwdActivity_verifyOtp", "" + jsonObject.toString());
                                    Toast.makeText(getApplicationContext(), "Some error occured. Going back to Login Screen", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent smsToSignup = new Intent(getApplicationContext(), PreLoginActivity.class);
                                    smsToSignup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    hidepDialog();
                                    startActivity(smsToSignup);
                                }
                                else if (reset_password.equalsIgnoreCase("otp expired"))
                                {
                                    Log.e("FPwdActivity_verifyOtp", "" + jsonObject.toString());
                                    Toast.makeText(getApplicationContext(), "Some error occured. Going back to Login Screen", Toast.LENGTH_SHORT).show();
                                    Intent smsToSignup = new Intent(getApplicationContext(), SmsForgotPwdActivity.class);
                                    finish();
                                    smsToSignup.putExtra("password-token", resetPwdToken);
                                    smsToSignup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    hidepDialog();
                                    startActivity(smsToSignup);
                                }
                                else if (reset_password.equalsIgnoreCase("changed"))
                                {
                                    Log.e("FPwdActivity_verifyOtp", "" + jsonObject.toString());
                                    Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent smsToSignup = new Intent(getApplicationContext(), MainActivity.class);
                                    smsToSignup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    hidepDialog();
                                    //startActivity(smsToSignup);

                                    SessionManager session = new SessionManager(ForgetPwdActivity.this);
                                    session.createLoginSession(PreLoginActivity.mobileNoOnET, "sfihfuier239429038e2i",changedPwd.getText().toString(), "chat"+(int) Math.ceil(Math.random() * 100000000));

                                    startActivity(smsToSignup);
                                }
                                else
                                {
                                    Log.e("FPwdActivity_verifyOtp", "" + jsonObject.toString());
                                    Toast.makeText(getApplicationContext(), "Some error occured. Going back to Login Screen", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent smsToSignup = new Intent(getApplicationContext(), PreLoginActivity.class);
                                    smsToSignup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    hidepDialog();
                                    startActivity(smsToSignup);
                                }




                        }
                     catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
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
            //PreLoginActivity.checkInternetNotLoggedIn(ForgetPwdActivity.this);
            showpDialog();

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
            passwordNotChanged();


        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            String json = null;


            passwordNotChanged();
            NetworkResponse response = volleyError.networkResponse;
            if(response != null && response.data != null){
                switch(response.statusCode){
                    case 400:
                        json = new String(response.data);
                        json = trimMessage(json, "message");
                        if(json != null)  Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
//
                        break;
                    case 401:
                        json = new String(response.data);
                        json = trimMessage(json, "message");
                        if(json != null)  Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
//
                        break;
                    case 500:
                        json = new String(response.data);
                        json = trimMessage(json, "message");
                        if(json != null)  Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT).show();
//
                        break;

                }
                //Additional cases
            }

        }

    }


}
