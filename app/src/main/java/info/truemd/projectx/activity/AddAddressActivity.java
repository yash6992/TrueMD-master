package com.truemdhq.projectx.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.vision.text.Line;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.helper.SessionManager;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 20/04/16.
 */
public class AddAddressActivity extends AppCompatActivity {

    EditText typeET, nameET, line1ET, line2ET, landmarkET, cityET, pinET; TextView chhose, titleTVAA; Button saveAA;
    ImageButton backIBAA; DilatingDotsProgressBar mDilatingDotsProgressBar;
    String type,name,line1,line2,landmark,city,pin;
    JSONObject addressJsonObject; TextInputLayout typetil;
    SessionManager session; RelativeLayout typeLL;  Dialog mBottomSheetDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        session = new SessionManager(AddAddressActivity.this);

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        typeLL = (RelativeLayout) findViewById(R.id.address_typeLL);
        typetil = (TextInputLayout) findViewById(R.id.typetil);

        typeET = (EditText) findViewById(R.id.address_typeET);
        typeET.setText("Home");



        nameET = (EditText) findViewById(R.id.nameET);
        line1ET = (EditText) findViewById(R.id.address1ET);
        line2ET = (EditText) findViewById(R.id.address2ET);
        landmarkET = (EditText) findViewById(R.id.landmarkET);
        cityET = (EditText) findViewById(R.id.cityET);

        cityET.setText("");
        pinET = (EditText) findViewById(R.id.pincodeET);

        pinET.setText(Paper.book("user").read("pincode").toString());

        titleTVAA = (TextView) findViewById(R.id.titleAA);
        saveAA = (Button) findViewById(R.id.save_tv_aa);
        chhose = (TextView) findViewById(R.id.choosetype);

        titleTVAA.setTypeface(tf_r);
        chhose.setTypeface(tf_r);
        saveAA.setTypeface(tf_r);

        backIBAA = (ImageButton) findViewById(R.id.backImageButtonAA);

        mDilatingDotsProgressBar= (DilatingDotsProgressBar) findViewById(R.id.progress_aa);

        backIBAA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        saveAA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAddress();

            }
        });

        chhose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTypeBottomSheet();
            }
        });



    }

    public void openTypeBottomSheet(){

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_address_type, null);

        LinearLayout cd1, cd2, cd3; TextView tv1, t1, t2, t3;

        cd1= (LinearLayout) view.findViewById(R.id.cardview1);
        cd2= (LinearLayout) view.findViewById(R.id.cardview2);
        cd3= (LinearLayout) view.findViewById(R.id.cardview3);

        tv1 = (TextView) view.findViewById(R.id.tv2);
        t1 = (TextView) view.findViewById(R.id.home_list);
        t2 = (TextView) view.findViewById(R.id.office_list);
        t3 = (TextView) view.findViewById(R.id.other_list);

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        tv1.setTypeface(tf_r);t1.setTypeface(tf_r);t2.setTypeface(tf_r);t3.setTypeface(tf_r);

        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeET.setText("Home");
                typeET.setFocusable(false);
                typeET.setEnabled(false);
                typeET.setCursorVisible(false);
                typeET.setKeyListener(null);
                typeET.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        cd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeET.setText("Office");
                typeET.setFocusable(false);
                typeET.setEnabled(false);
                typeET.setCursorVisible(false);
                typeET.setKeyListener(null);
                typeET.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        cd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeET.setText("Other");
                typeET.setFocusable(true);
                typeET.setEnabled(true);
                typeET.setCursorVisible(true);
                typeET.setKeyListener((KeyListener) typeET.getTag());
                typeET.setBackgroundColor(Color.WHITE);
            }
        });



        mBottomSheetDialog = new Dialog (this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeET.setText("Home");

                mBottomSheetDialog.dismiss();
            }
        });

        cd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeET.setText("Office");

                mBottomSheetDialog.dismiss();
            }
        });
        cd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeET.setText("Other");

                mBottomSheetDialog.dismiss();
            }
        });


    }


    public void saveAddress(){
        Log.d("Address: ", "saveAddress clicked");

        if (!validate()) {
            return;
        }


        submitAdd();


    }

    public void submitAdd(){

        mDilatingDotsProgressBar.showNow();

        Log.e("in submitAdd():: ", "add submission started");

        HashMap<String, String> user = session.getUserDetails();

            try {
                ContentValues geopair = new ContentValues();
                geopair.put("lat", "123");
                geopair.put("lon", "456");
                JSONObject geojobj = getJsonObjectFromContentValuesNoTag(geopair);


                ContentValues codecodepair = new ContentValues();
                codecodepair.put("type", typeET.getText().toString());
                codecodepair.put("name",  nameET.getText().toString());
                codecodepair.put("landmark", landmarkET.getText().toString());
                codecodepair.put("city", cityET.getText().toString());
                codecodepair.put("line1", line1ET.getText().toString());
                codecodepair.put("line2", line2ET.getText().toString());
                codecodepair.put("pincode",  pinET.getText().toString());
                //codecodepair.put("name",MainActivity.nameFromGetUser);


                Log.e("in submitAdd():: ", "inside try");

                addressJsonObject = getJsonObjectFromContentValues("address", codecodepair, geojobj);
                JsonObjectRequester mRequester;
                mRequester = new RequestBuilder(AddAddressActivity.this)
                        //.requestCode(REQUEST_CODE)
                        .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                        .showError(false) //Show error with toast on Network or Server error
                        .shouldCache(true)
                        .priority(Request.Priority.NORMAL)
                        .timeOut(50000)
                        .allowNullResponse(true)
                                //.tag(REQUEST_TAG)
                        .addToHeader("X-User-Token",user.get(SessionManager.KEY_AUTHENTICATION_UM))
                        .addToHeader("Content-Type","application/json")
                        .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                        .buildObjectRequester(new JsonObjectListenerToAdd()); //or .buildArrayRequester(listener);

                Log.e("in submitOrder():: ", addressJsonObject.toString());


                mRequester.request(Methods.POST, MainActivity.app_url+"/addresses.json", addressJsonObject);




            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }





    }

    private class JsonObjectListenerToAdd extends Response.SimpleObjectResponse {

        String  situation, reason;
        JSONObject obj;
        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {

                if (jsonObject.toString().length() > 5) {


                    Log.e("Siri resp: ", jsonObject.toString());

                    obj = jsonObject;

                   situation ="ok";


                    try {
                        String type=addressJsonObject.getJSONObject("address").getString("type");
                        String value="";
                        String name=addressJsonObject.getJSONObject("address").getString("name");
                        String line1=addressJsonObject.getJSONObject("address").getString("line1");
                        String line2=addressJsonObject.getJSONObject("address").getString("line2");
                        String landmark=addressJsonObject.getJSONObject("address").getString("landmark");
                        String city=addressJsonObject.getJSONObject("address").getString("city");
                        String pincode=addressJsonObject.getJSONObject("address").getString("pincode");

                        JSONObject geoObj=addressJsonObject.getJSONObject("address").getJSONObject("geolocation");
                        String lat=geoObj.getString("lat");
                        String lon=geoObj.getString("lon");

                        value= name+", "+line1+", "+line2+", "+landmark+", "+city+"- "+pincode;

                        ConfirmOrderActivity.deliveryAddressFinal=value;
                        ConfirmOrderActivity.selectedAddressjobj= addressJsonObject.getJSONObject("address");

                    } catch (Exception e) {
                        Log.e("Address1: ",e.getMessage());
                        e.printStackTrace();
                    }


                    Toast.makeText(AddAddressActivity.this, "User address added.", Toast.LENGTH_SHORT).show();
                    mDilatingDotsProgressBar.hideNow();
                    finish();

                    try {
                        ConfirmOrderActivity.deliveryTVCO.setText(ConfirmOrderActivity.deliveryAddressFinal);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Address2: ",e.getMessage());
                    }







                } else {
                    situation = "fail";
                    reason = "Order can't be submitted due to network issues. Try Again";
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Address4: ",e.getMessage());
            }
        }

        @Override
        public void onRequestStart(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()!=View.VISIBLE)
               // mDilatingDotsProgressBar.showNow();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }




        @Override
        public void onRequestFinish(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()==View.VISIBLE)
                //mDilatingDotsProgressBar.hideNow();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



//            if(ConfirmOrderActivity.mBottomSheetDialog.isShowing())
//                ConfirmOrderActivity.mBottomSheetDialog.dismiss();


        }
    }


        private  JSONObject getJsonObjectFromContentValuesNoTag(ContentValues params) throws JSONException {

        //object for storing Json
        JSONObject data = new JSONObject();


        for (Map.Entry<String, Object> entry : params.valueSet()) {
            String key = entry.getKey(); // name
            String value = entry.getValue().toString(); // value
            data.put(key, value);
        }



        return data;
    }
    private  JSONObject getJsonObjectFromContentValues(String key_tag, ContentValues params, JSONObject jobj) throws JSONException {

        //Stores JSON
        JSONObject holder = new JSONObject();


        //object for storing Json
        JSONObject data = new JSONObject();


        for (Map.Entry<String, Object> entry : params.valueSet()) {
            String key = entry.getKey(); // name
            String value = entry.getValue().toString(); // value
            data.put(key, value);
        }

        data.put("geolocation",jobj);

        //puts email and 'foo@bar.com'  together in map
        holder.put(key_tag, data);

        return holder;
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String type = typeET.getText().toString();
        String name = nameET.getText().toString();
        String line1 = line1ET.getText().toString();
        String city = cityET.getText().toString();
        String pin = pinET.getText().toString();


        if (type.isEmpty()) {
            typeET.setError("Please type if it is your Home or Office Address");
            valid = false;
        } else {

            typeET.setError(null);
        }

        if (name.isEmpty()) {
            nameET.setError("Type in the name To whom shall we deliver?");
            valid = false;
        } else {

            nameET.setError(null);
        }
        if (line1.isEmpty()) {
            line1ET.setError("Please type in the House/ Flat No.");
            valid = false;
        } else {

            line1ET.setError(null);
        }
        if (city.isEmpty()) {
            cityET.setError("What city is it?");
            valid = false;
        } else {

            cityET.setError(null);
        }
        if (pin.isEmpty()||!pin.startsWith("")) {
            pinET.setError("Please type in a valid Indore Pin Code.");
            valid = false;
        } else {

            pinET.setError(null);
        }

        return valid;
    }


    }
