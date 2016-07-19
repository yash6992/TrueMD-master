package info.truemd.android.activity;

import android.content.ContentValues;
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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.android.R;
import info.truemd.android.helper.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    TextView title, spam, nametitle, settingtitle;
    ImageButton back;
    ImageView malei, femalei;
    String gender, name, email_alt;
    EditText nameet, email_altet;
    Button btn_update; int sel;
    DilatingDotsProgressBar wizProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        Log.e("in Profile: ", ""+ MainActivity.profileName +" "+MainActivity.profileGender+" "+ MainActivity.profileEmailAlt);

        title = (TextView) findViewById(R.id.pro_title_tv);
        spam = (TextView) findViewById(R.id.nospam);
        nametitle = (TextView) findViewById(R.id.pro_name_tv2);
        settingtitle = (TextView) findViewById(R.id.tv2);

        title.setTypeface(tf_r);
        spam.setTypeface(tf_r);
        nametitle.setTypeface(tf_r);
        settingtitle.setTypeface(tf_r);

        wizProgress = (DilatingDotsProgressBar) findViewById(R.id.pro_progress);

        back = (ImageButton) findViewById(R.id.pro_backImageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        malei = (ImageView) findViewById(R.id.male);
        femalei =(ImageView) findViewById(R.id.female);

        gender = MainActivity.profileGender;

        if(gender.equalsIgnoreCase("male"))
            sel=1;
        else if(gender.equalsIgnoreCase("female"))
            sel=2;
        else
            sel=0;

        name = MainActivity.profileName;
        email_alt = MainActivity.profileEmailAlt;

        nameet = (EditText) findViewById(R.id.pro_name_ET);
        email_altet = (EditText) findViewById(R.id.pro_email_alt_ET);

        nameet.setText(MainActivity.profileName);

        email_altet.setText(MainActivity.profileEmailAlt);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        if(sel==1)
        {
            malei.setBackground(getResources().getDrawable(R.drawable.shape_solid_circle_dark));
            femalei.setBackground(getResources().getDrawable(R.drawable.no_bg));
        }
        else if(sel==2)
        {
            malei.setBackground(getResources().getDrawable(R.drawable.no_bg));
            femalei.setBackground(getResources().getDrawable(R.drawable.shape_solid_circle_dark));
        }
        else
        {
            malei.setBackground(getResources().getDrawable(R.drawable.no_bg));
            femalei.setBackground(getResources().getDrawable(R.drawable.no_bg));
        }

        malei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                malei.setBackground(getResources().getDrawable(R.drawable.shape_solid_circle_dark));
                femalei.setBackground(getResources().getDrawable(R.drawable.no_bg));
                sel=1;
            }
        });
        femalei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                malei.setBackground(getResources().getDrawable(R.drawable.no_bg));
                femalei.setBackground(getResources().getDrawable(R.drawable.shape_solid_circle_dark));
                sel=2;
            }
        });

        btn_update = (Button) findViewById(R.id.btn_pro_submit);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_altS = email_altet.getText().toString().trim();
                String gender = "";
                String nameS = nameet.getText().toString().trim();

                if (sel != 0&&nameS.length()!=0){

                    if(email_altS.length()>0) {

                        if(validateEmail(email_altS)){

                            if (sel == 1)
                                gender = "male";
                            else if (sel == 2)
                                gender = "female";

                            ContentValues codecodepair = new ContentValues();

                            codecodepair.put("sex",gender);
                            codecodepair.put("name", nameS );
                            codecodepair.put("email_alt",email_altS);
                            try {
                                JSONObject orderJsonObject = getJsonObjectFromContentValues( "user",codecodepair);

                                updateUser(orderJsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            email_altet.setError("Please enter a valid Email Id.");
                        }

                    }
                    else{
                        ContentValues codecodepair = new ContentValues();

                        codecodepair.put("sex",gender);
                        codecodepair.put("name", nameS );
                        try {
                            JSONObject orderJsonObject = getJsonObjectFromContentValues("user", codecodepair);

                            updateUser(orderJsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                else{
                    Toast.makeText(getApplicationContext(), "There might be some incomplete details.", Toast.LENGTH_SHORT).show();
                }

            }
        });






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

    /**
     * Validate hex with regular expression
     *
     * @param hex
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validateEmail(final String hex) {

         Pattern pattern;
         Matcher matcher;

          final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";



        pattern = Pattern.compile(EMAIL_PATTERN);

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }



    public void updateUser(JSONObject orderJsonObject){


        try {
            SessionManager session = new SessionManager(ProfileActivity.this);

            HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester = new RequestBuilder(ProfileActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(true) //Show error with toast on Network or Server error
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
            mRequester.request(Methods.PUT, MainActivity.app_url+"/updateUser.json", orderJsonObject);

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


                        new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Awesome..!!")
                                .setContentText("")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                        //System.exit(0);

                                    }
                                })
                                .show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("PreLogin err: ", ""+e.getMessage());

                    }

                    //lv.setAdapter(new CustomUpcomingOrderAdapter(getApplicationContext(),dateArray,orderNoArray,nameArray, statusArray, dotsArray, gTotalArray, deliveryTimeArray,aljoPreLogins  ));
                }



                else {
                    wizProgress.hideNow();

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

            Toast.makeText(getApplicationContext()," There might be some network issue. Please try again."
            ,Toast.LENGTH_SHORT).show();
            wizProgress.hideNow();

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




        }
        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            wizProgress.hideNow();
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());
            if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
            {
                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("The server is taking too long to respond or there might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                ProfileActivity.this.finish();
                                //System.exit(0);

                            }
                        })

                        .show();
            }


        }



    }



}
