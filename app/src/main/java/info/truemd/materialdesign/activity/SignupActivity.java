package info.truemd.materialdesign.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


import info.truemd.materialdesign.R;
import io.paperdb.Paper;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText nameText;
    EditText emailText;
    EditText passwordText; ImageButton imageViewLogin;
    Button signupButton; CheckBox checkbox;
    TextView loginLink,termstv, truemdTitle, truemdsubTitle;
    public static String passwordSignup="";
    //String otpToken;
    DilatingDotsProgressBar signupProgress;
    Context context; ScrollView sv_register;


    RequestQueue requestQueue;

    private ProgressDialog pDialog;

    private TextView txtResponse;

    // temporary string to show the parsed response
    private String jsonResponse;

    String signupURL = "http://truemd-carebook.rhcloud.com/users.json";
    private ContentValues paramsForPostRequest;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        truemdTitle = (TextView) findViewById(R.id.truemdtitle_tv);
        truemdsubTitle = (TextView) findViewById(R.id.truemdsubtitle_tv);
        checkbox = (CheckBox) findViewById(R.id.checkBox);
        termstv = (TextView) findViewById(R.id.termstv);
        sv_register = (ScrollView) findViewById(R.id.sv_register);
        signupProgress = (DilatingDotsProgressBar) findViewById(R.id.signup_progress);

        Typeface tf_pacifico = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        Typeface tf_l = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        truemdTitle.setTypeface(tf_pacifico);
        truemdsubTitle.setTypeface(tf_l);checkbox.setTypeface(tf_l);termstv.setTypeface(tf_l);

        context= getApplicationContext();


        paramsForPostRequest= new ContentValues();

        txtResponse= (TextView) findViewById(R.id.txtResponse_signup);
        txtResponse.setVisibility(View.GONE);
        nameText= (EditText)findViewById(R.id.input_name);
        emailText= (EditText)findViewById(R.id.input_email);
        passwordText= (EditText)findViewById(R.id.input_password);
        signupButton= (Button)findViewById(R.id.btn_signup);
        loginLink=(TextView)findViewById(R.id.link_login);



        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        loginLink.setTypeface(tf_l);
        signupButton.setTypeface(tf_l);

        emailText.setText(getIntent().getStringExtra("mobile"));
        emailText.setKeyListener(null);

        imageViewLogin = (ImageButton) findViewById(R.id.imageButtonToPreLogin);
        imageViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nameText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(emailText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                InputMethodManager imm3 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm3.hideSoftInputFromWindow(passwordText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent signUpToLogin = new Intent(getApplicationContext(), PreLoginActivity.class);

                startActivityForResult(signUpToLogin, 0);
            }
        });

        termstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet("Terms and Conditions", "");
            }
        });
    }

    public void openBottomSheet( String heading,  String link) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_terms, null);
        WebView wv = (WebView) view.findViewById(R.id.webView);
        TextView txth = (TextView)view.findViewById( R.id.tv2);

        txth.setTypeface(tf_r);

        txth.setText(heading);



        final Dialog mBottomSheetDialog = new Dialog (SignupActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }

    @Override
    public void onBackPressed(){

        finish();


//        Intent signUpToLogin = new Intent(getApplicationContext(), LoginActivity.class);
//
//        startActivityForResult(signUpToLogin, 0);

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            {
                //onSignupFailed("Please enter the details correctly");
                return;
            }




        }

        termstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open bottom sheet with web view.
            }
        });

        signupButton.setEnabled(false);

        pDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Creating Account...");
        //pDialog.show();

        String name = nameText.getText().toString();
        String email =emailText.getText().toString();
        String password = passwordText.getText().toString();





        // TODO: Implement your own signup logic here.

       // LoginActivity.checkInternetNotLoggedIn(this);

      registerRequest(name, email, password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        //onSignupSuccess();
                        // onSignupFailed();
                        //pDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess(String otpToken) {

        signupButton.setEnabled(true);
        hidepDialog();
        Paper.book("RegisterPassword").write("password",passwordText.getText().toString());
        Toast.makeText(getApplicationContext(), "Sign Up successful, Verifying mobile number now", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK, null);

        Intent signUpToSms = new Intent(getApplicationContext(), SmsActivity.class);
        signUpToSms.putExtra("otp_token", otpToken);
        startActivityForResult(signUpToSms, 0);
    }

    public void onSignupFailed(String messages) {
        signupButton.setEnabled(true);
        hidepDialog();
        //Toast.makeText(getApplicationContext(), "Sign Up failed, Try Again !!", Toast.LENGTH_SHORT).show();
        if (messages.contains("Email is")) {
            Toast.makeText(getApplicationContext(), "Uh-oh… Looks like this Mobile Number is already registered!", Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_SHORT).show();


//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);


    }
    public void onHasAccountAlready(String name) {
        Toast.makeText(getApplicationContext(), name+ " already has account. Try Logging in.", Toast.LENGTH_SHORT).show();

        signupButton.setEnabled(true);

        hidepDialog();

        setResult(RESULT_OK, null);

        Intent signUpToLogin = new Intent(getApplicationContext(), LoginActivity.class);
        signUpToLogin.putExtra("mobile", emailText.getText().toString().trim());
        startActivityForResult(signUpToLogin, 0);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();



        if (email.length()!=10) {
            emailText.setError("enter a valid mobile number");
            valid = false;
        } else {
            String regEx = "^[0-9]{10}$";
            valid =  email.matches(regEx);
            emailText.setError(null);
        }


        if (password.length() < 8 || password.length() > 20) {
            passwordText.setError("Password should be a minimum of 8 chars.");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (name.length() < 3) {
            nameText.setError("name should be at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (checkbox.isChecked()==false) {
            onSignupFailed("Please agree to the terms and conditions.");
            valid = false;
        } else {

        }

        return valid;
    }


    /**
     * Method to make json object request where json response starts wtih {
     */

    private void showpDialog() {
        signupProgress.showNow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hidepDialog() {
       signupProgress.hideNow();
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

    public void registerRequest(String name, String mobile, String password){

        ContentValues codecodepair = new ContentValues();
        codecodepair.put("mobile", mobile);
        codecodepair.put("password", password);
        passwordSignup = password;
        String user_type = "patient";
        codecodepair.put("name", name);
        codecodepair.put("user_type",user_type);


        try {
            JSONObject couponJsonObject = getJsonObjectFromContentValues("user", codecodepair);
            //HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(context)
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
                    .buildObjectRequester(new JsonObjectListenerToRegister()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.POST, signupURL, couponJsonObject);




        } catch (JSONException e) {
            Log.e("LoginError:: ", e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            Log.e("LoginError:: ", e.getMessage());
            e.printStackTrace();
        }


    }


    class JsonObjectListenerToRegister extends com.alirezaafkar.json.requester.interfaces.Response.SimpleObjectResponse {

        String authentication_token_new, name_new,status_new, user_status_new, mobile_new, password_new;
        boolean success;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length()>3) {

                    Log.e("Siri resp: ", jsonObject.toString());
                    success = true;

                    JSONObject state = jsonObject.getJSONObject("state");
                    int code = state.getInt("code");

                    String messages = state.getString("message");

                    //txtResponse.setText(jsonResponse);

                    if(code==1)
                    {
                        Log.e("RegisterCode:: ", ""+code);

                        JSONObject data = jsonObject.getJSONObject("user");
                        String name= data.getString("name");
                        String authentication_token = data.getString("authentication_token");
                        String otp_token = data.getString("otp_token");

                        jsonResponse = "";
                        jsonResponse += "code: " + code + "\n\n";
                        jsonResponse += "messages: " + messages + "\n\n";
                        jsonResponse += "name: " + name + "\n\n";
                        jsonResponse += "token: " + authentication_token + "\n\n";

                        Log.e("RegisterResp:: ", jsonResponse);

                        String status = data.getString("status");
                        if(status.equalsIgnoreCase("unverified"))
                            onSignupSuccess(otp_token);
                        else if (status.equalsIgnoreCase("verified"))
                            onHasAccountAlready(name);

                    }
                    else if(code==0)
                    {
                        jsonResponse = ""+code+messages;
                        Log.e("RegisterCode:: ", ""+code);
                        Log.e("RegisterResp:: ", jsonResponse);
                        onSignupFailed(messages);
                    }
                    else {
                        jsonResponse = ""+code+messages;
                        Log.e("RegisterResp:: ", jsonResponse);
                        onSignupFailed(messages);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                String message = e.getMessage();
                Log.e("LoginError:: ",  message );
                Intent toLogin = new Intent(context, PreLoginActivity.class);
                startActivity(toLogin);
            }
        }


        @Override
        public void onRequestStart(int requestCode) {
            showpDialog();
            //LoginActivity.checkInternetNotLoggedIn(context);


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
            onSignupFailed("Uh-oh… There might be a network issue.");

            hidepDialog();

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            String json = null;

            hidepDialog();
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
//            if (TrueMDJSONUtils.goThroughNullCheck(message).contains("401")||volleyError.getMessage().contains("401"))
//            {
//                Log.e("Signup:: ", "401"+ message + " : "+ volleyError.getMessage());
//                Toast.makeText(context, "You might have entered the wrong password.", Toast.LENGTH_SHORT).show();
//
//            }
//            else
//            {
//                Log.e("Signup:: ", "401");
//                Toast.makeText(context, message+" : "+ volleyError.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }

        }

    }

}