package info.truemd.android.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.wang.avi.AVLoadingIndicatorView;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.android.R;
import info.truemd.android.helper.SessionManager;
import info.truemd.android.helper.TrueMDJSONUtils;
import info.truemd.android.service.ConnectionDetector;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    TextView output;
    String loginURL = MainActivity.app_url+"/users/sign_in.json";
    String data = ""; String mobileL, authentication_tokenL;
    EditText email_login;
    EditText password_login, phone_number;
    Button login_button;
    TextView signup_link;
    TextView forgot_pwd_link;
    ImageButton imageViewLogin;
    ScrollView sv_login; Dialog mBottomSheetDialog;

    Context context;
    DilatingDotsProgressBar loginProgress;
    JsonObjectRequester mRequester;

    SessionManager session;

    RequestQueue requestQueue;

   // private ProgressDialog pDialog;

    private AVLoadingIndicatorView avpDialog;

    private TextView txtResponse,truemdTitle, truemdsubTitle;

    // temporary string to show the parsed response
    private String jsonResponse;

    private ContentValues paramsForPostRequest;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("introduction:: ", ""+ Paper.book("introduction").read("intro"));
        if(Paper.book("introduction").read("intro").equals("0")) {
            Intent i = new Intent(LoginActivity.this, Intro.class);
            startActivity(i);
        }


        Log.d("LoginActivity: ", "after onCreate");

        session=new SessionManager(getApplicationContext());
       // pDialog = new ProgressDialog(LoginActivity.this,
              //  R.style.AppTheme_Dark_Dialog);

        avpDialog = (AVLoadingIndicatorView) findViewById(R.id.avloadingIndicatorViewLogin);
        sv_login = (ScrollView) findViewById(R.id.sv_login);
        txtResponse = (TextView) findViewById(R.id.txtResponse);
        txtResponse.setVisibility(View.GONE);
        email_login= (EditText) findViewById(R.id.input_email);
        password_login= (EditText) findViewById(R.id.input_password);
        login_button= (Button) findViewById(R.id.btn_login);
        signup_link= (TextView) findViewById(R.id.link_signup);
        forgot_pwd_link= (TextView) findViewById(R.id.link_forgot_pwd);
        truemdTitle = (TextView) findViewById(R.id.truemdtitle_tv);
        truemdsubTitle = (TextView) findViewById(R.id.truemdsubtitle_tv);
        imageViewLogin = (ImageButton) findViewById(R.id.imageButtonToPreLogin);
        imageViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loginProgress = (DilatingDotsProgressBar) findViewById(R.id.login_progress);

        Typeface tf_pacifico = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        Typeface tf_l = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        truemdTitle.setTypeface(tf_pacifico);
        forgot_pwd_link.setTypeface(tf_l);
        signup_link.setTypeface(tf_l);
        login_button.setTypeface(tf_l);
        truemdsubTitle.setTypeface(tf_l);

        context = LoginActivity.this;

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(email_login.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm2.hideSoftInputFromWindow(password_login.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        email_login.setText(getIntent().getStringExtra("mobile"));
        mobileL = getIntent().getStringExtra("mobile");
        email_login.setFocusable(false);
        email_login.setEnabled(false);
        email_login.setCursorVisible(false);
        email_login.setKeyListener(null);
        email_login.setBackgroundColor(Color.TRANSPARENT);


        paramsForPostRequest= new ContentValues();

        login_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(email_login.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(password_login.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                login();

                //onLoginSuccess();//erase this code and un-comment above line to enable authentication
            }
        });

        signup_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        forgot_pwd_link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the ForgetPwdActivity activity
                forgetPassword(email_login.getText().toString().trim());
           // openBottomSheet("Forgot Password?","");
            }
        });



    }

    public void openBottomSheet( String heading,  String link) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_forget_password, null);

        TextView txth = (TextView)view.findViewById( R.id.tv2);
      phone_number = (EditText)view.findViewById( R.id.input_phone_number_password);
        Button submit = (Button) view.findViewById( R.id.btn_submit_number_password);

        txth.setTypeface(tf_r);
        phone_number.setTypeface(tf_r);
        submit.setTypeface(tf_r);

        txth.setText(heading);



         mBottomSheetDialog = new Dialog (LoginActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phone_number.getText().toString().trim();
                forgetPassword(phoneNumber);
            }
        });




    }

    void forgetPassword(String mobile){




        if (mobile.isEmpty() || mobile.length()!=10) {
            phone_number.setError("Enter the 10 digit mobile number for which the password is needed");
        } else {
            String regEx = "^[0-9]{10}$";
            if(mobile.matches(regEx));
            {
               // phone_number.setError(null);
                //forgetPassword(phoneNumber);

                boolean valid = true;

                Log.e("Login:: ", "forgot password Link clicked"+mobile);



                if(valid) {

                    String pwdUrl= MainActivity.app_url+"/users/password/new?mobile="+mobile;
                    requestPassword(pwdUrl);
                }



            }
        }



    }

    public String loginWithParams(String mobileNo, String password){

        String autheticationToken=new String();


        return autheticationToken;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("Please fill the details correctly.");
            return;
        }


        login_button.setEnabled(false);

        String email = email_login.getText().toString();
        String password = password_login.getText().toString();

        new LoginActivity().checkInternetNotLoggedIn(this);


            loginRequest(email,password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        // onLoginFailed();
                        Log.d(TAG, "nothing");
                        login_button.setEnabled(true);
                        //pDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        startActivity(new Intent(LoginActivity.this, PreLoginActivity.class));
    }

    public void onLoginSuccess() {
        Log.e(TAG, "LOGIN SUCCESS");
        session.createLoginSession(mobileL, authentication_tokenL, password_login.getText().toString(), "chat" + (int) Math.ceil(Math.random() * 100000000));
        login_button.setEnabled(true);
        hidepDialog();
        finish();
        Intent login = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(login);

    }

    public void onLoginFailed(String message) {

        if(validate())
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();



        login_button.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = email_login.getText().toString();
        String password = password_login.getText().toString();


        if (email.isEmpty() || email.length()!=10) {
            email_login.setError("enter a valid mobile number");
            valid = false;
        } else {
            String regEx = "^[0-9]{10}$";
            valid =  email.matches(regEx);
            email_login.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 20) {
            password_login.setError("between 8 and 20 alphanumeric characters");
            valid = false;
        } else {
            password_login.setError(null);
        }

        return valid;
    }

    /**
     * Method to make json object request where json response starts wtih {
     */

    private void showpDialog() {
//        if (loginProgress.getVisibility()==View.GONE || loginProgress.getVisibility()==View.INVISIBLE)
//        {loginProgress.setVisibility(View.VISIBLE);}
        loginProgress.showNow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private void hidepDialog() {
//        if (loginProgress.getVisibility()==View.VISIBLE)
//        {loginProgress.setVisibility(View.INVISIBLE);}

        loginProgress.hideNow();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private static JSONObject getJsonObjectFromContentValues(String key_tag, ContentValues params) throws JSONException {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        //Iterator iter = params.entrySet().iterator();

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

    public static void checkInternetNotLoggedIn(final Context context){
        ConnectionDetector cd = new ConnectionDetector(context);

        Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
        if (!isInternetPresent)
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...No Internet Connection!")
                    .setContentText("internet connection required . Please try again")
                    .show();
    }

    public void loginRequest(String mobile, String password){

        ContentValues codecodepair = new ContentValues();
        codecodepair.put("mobile", mobile);
        codecodepair.put("password", password);

        Log.e("LoginRequest: " , mobile+":"+password);


        try {
            JSONObject couponJsonObject = getJsonObjectFromContentValues("user", codecodepair);
            HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(context)
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
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")

                    //.addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToLogin()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.POST, MainActivity.app_url+"/users/sign_in.json", couponJsonObject);

            Log.e("LoginRequest: " , "created");
            Log.e("LoginRequest: " , couponJsonObject.toString());

        } catch (Exception e) {
            Log.e("LoginError:: ", e.getMessage());
            e.printStackTrace();
            onLoginFailed("There might be some network issue.");
        }


    }


    class JsonObjectListenerToLogin extends com.alirezaafkar.json.requester.interfaces.Response.SimpleObjectResponse {

        String authentication_token_new,sex_new, name_new,status_new, user_status_new, mobile_new, password_new;
        boolean success=false;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {

                Log.e("LoginRequest: ", TrueMDJSONUtils.goThroughNullCheck(jsonObject.toString()));

                if (jsonObject.toString().length()>5) {

                    Log.e("LoginRequest resp: ", jsonObject.toString());
                    success = true;
                    JSONObject obj = jsonObject;
                    authentication_token_new = TrueMDJSONUtils.goThroughNullCheck(obj.getString("authentication_token"));
                    JSONObject userobj=obj.getJSONObject("user");

                    name_new= userobj.optString("name");
                    sex_new=userobj.optString("sex");

                    MainActivity.nameFromGetUser=name_new;
                    MainActivity.profileGender=sex_new;

                    Paper.book("user").write("name",name_new);


                    status_new= obj.optString("status");
                    user_status_new= userobj.optString("status");
                    mobile_new=userobj.optString("mobile");
                    //mobileL = mobile_new;

                    if(status_new.equalsIgnoreCase("ok"))
                    {
                        Log.e("LoginRequest: ","status ok");
                        if(user_status_new.equalsIgnoreCase("verified") && authentication_token_new.length()>6){
                           // mobileL = mobile_new;
                            authentication_tokenL = authentication_token_new;
                            onLoginSuccess();
                        }
                        else
                        {
                            Log.e("LoginRequest: ","status ok : unverified : "+authentication_token_new);
                            onLoginFailed("The mobile no. is not verified. Proceed to verification");
                            reverifyUserWithOTP(context, mobileL);
                        }
                    }
                    else
                    {
                        Log.e("LoginRequest: ","status not ok");
                        onLoginFailed("Uh-oh… The mobile number and password don't match");
                    }
                }

                else
                {
                   Log.e("LoginRequest: ","status not ok");
                    onLoginFailed("Uh-oh… The mobile number and password don't match");
                }


                Log.e("LoginRequest: ", ""+success);



            } catch (Exception e) {
                e.printStackTrace();
                String message = e.getMessage();
                Log.e("LoginError:: ",  message );
                onLoginFailed("Uh-oh… The mobile number and password don't match");
            }finally {

                if(!success)
                {
                    Log.e("LoginRequest: ","status not ok");
                    onLoginFailed("Uh-oh… The mobile number and password don't match");
                }

            }
        }


        @Override
        public void onRequestStart(int requestCode) {
            Log.e("LoginRequest:: ",  "start" );
            checkInternetNotLoggedIn(LoginActivity.this);
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
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());
            onLoginFailed("Uh-oh… The mobile number and password don't match");

            hidepDialog();

        }

        @Override
        public void onRequestFinish(int requestCode) {

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            String json = null;
            Log.e ("onFinishResponse: ", requestCode +" : "+message+" : "+volleyError.getMessage());
            onLoginFailed("Uh-oh… Something looks fishy !!!");
            NetworkResponse response = volleyError.networkResponse;
            hidepDialog();
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


    public void requestPassword(String url){




        try {
             JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(context)
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
            onLoginFailed("There might be some network issue.");
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
            checkInternetNotLoggedIn(LoginActivity.this);
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
            onLoginFailed("Uh-oh… There might be a network issue.");

            hidepDialog();

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            hidepDialog();

        }

    }

    public void reverifyUserWithOTP(final Context context, String mobile){

        Log.e("LoginActivity: ","in reverifyUserWithOTP()");

        HashMap<String, String> user = session.getUserDetails();



        try {


            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(LoginActivity.this)
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
                    Toast.makeText(LoginActivity.this,  "We couldn't find the number. Please Signup. " , Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity: ", "We couldn't find the number. Please Signup. " + verification);
                    hidepDialog();
                }
                else if(verification.equalsIgnoreCase("already verified")) {
                    Toast.makeText(LoginActivity.this, mobileL+ " is already verified. Click forget password to set new password.", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity: ", mobileL+ " is already verified. Click forget password to set new password." + verification);
                    hidepDialog();
                }
                else if(verification.equalsIgnoreCase("otp resent")) {

                    hidepDialog();
                    Toast.makeText(LoginActivity.this, "OTP Resent on : "+ mobileL, Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity: ", "verifyUser() verification:: "+mobileL+" " + verification);
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
            showpDialog();
            checkInternetNotLoggedIn(LoginActivity.this);


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
            onLoginFailed("Uh-oh… There might be a network issue.");

            hidepDialog();

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            hidepDialog();

        }
    }




}
