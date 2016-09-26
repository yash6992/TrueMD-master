package info.truemd.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.android.R;
import info.truemd.android.helper.ExceptionHandler;
import info.truemd.android.helper.SessionManager;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 20/04/16.
 */
public class OrderMedicineActivity extends AppCompatActivity {

    ImageView idealPres;
    ImageButton backImageButtonOM, info, help; Dialog mBottomSheetDialog1, mBottomSheetDialog4;
    TextView titleOM, t1, t2, pincodecity ; EditText pincodeet; DilatingDotsProgressBar wizProgress;
    Button uploadPrescription,refillOrder;
    DilatingDotsProgressBar orderProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_order_medicine);

        Typeface tf_l=Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface tf_pacifico=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");

        idealPres=(ImageView)findViewById(R.id.idealPrescriptionImageViewOM);
        backImageButtonOM=(ImageButton) findViewById(R.id.backImageButtonOM);
        info=(ImageButton) findViewById(R.id.infoimageButton);
        titleOM=(TextView) findViewById(R.id.titleOM);
        t1=(TextView) findViewById(R.id.textView4OM);
        t2=(TextView) findViewById(R.id.textView5OM);
        uploadPrescription=(Button) findViewById(R.id.uploadPrescriptionButtonOM);
        refillOrder=(Button) findViewById(R.id.refillButtonOM);
        orderProgress = (DilatingDotsProgressBar) findViewById(R.id.order_progress);
        //wizProgress = (DilatingDotsProgressBar) findViewById(R.id.pro_progress);

        help = (ImageButton) findViewById(R.id.helpimageButton);
        mBottomSheetDialog1= new Dialog(OrderMedicineActivity.this,
                R.style.MaterialDialogSheet);

        titleOM.setTypeface(tf_pacifico);

        t2.setVisibility(View.GONE);

        try {
            String cityname =Paper.book("user").read("pincode_city");

            t2.setText(cityname.substring(0,cityname.indexOf(','))+": \u20B9 99 delivery charge applicable on all orders.");
        } catch (Exception e) {
            Log.e("Err:cityname:"," "+e.getMessage());
            //openBottomSheetForAskPincode();
            e.printStackTrace();
        }

        t1.setTypeface(tf_l);t2.setTypeface(tf_l);uploadPrescription.setTypeface(tf_l);refillOrder.setTypeface(tf_l);

        String askpin = Paper.book("introduction").read("askpin");

        if(askpin.equalsIgnoreCase("0"))
            openBottomSheetForAskPincode();

        uploadPrescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String check =Paper.book("introduction").read("preshelp","0");

                if(check.equalsIgnoreCase("0"))
                    openBottomSheetForPrescription();
                else
                {
                    moveToChoosePrescription();
                }

            }
        });
        backImageButtonOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openBottomSheetForPharmacyRules();
            }
        });
        refillOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onrefillPressed();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetForPrescriptionHelp();
            }
        });



    }
    public void onrefillPressed(){

        Intent toPreviousOrderDetails = new Intent(OrderMedicineActivity.this, OrderDetailsActivity.class);
        toPreviousOrderDetails.putExtra("to",1);
        startActivity(toPreviousOrderDetails);
    }

    void moveToChoosePrescription(){

        if(mBottomSheetDialog1.isShowing())
        mBottomSheetDialog1.dismiss();
        orderProgress.showNow();

        Paper.book("introduction").write("preshelp","1");

        Config config = new Config();
        config.setSelectionMin(1);
        config.setSelectionLimit(10);
        config.setToolbarTitleRes(R.string.tooltextImages);
        config.setSelectedBottomHeight(R.dimen.selected_image_height);
        config.setCameraHeight(R.dimen.camera_height);

        ImagePickerActivity.setConfig(config);
        Intent intent = new Intent(OrderMedicineActivity.this, ImagePickerActivity.class);

        orderProgress.hideNow();
        startActivityForResult(intent, 13);
    }


    @Override
    public void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);
        Log.e("onActivityresult","bs opened: " +requestCode+":"+resuleCode);

        if(resuleCode==-1)
        {
            Intent intent1 = new Intent(OrderMedicineActivity.this, HowToConfirmOrerActivity.class);
            intent1.putExtra("images", intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS));
            startActivity(intent1);
        }
        else
        {

        }
    }


    public void openBottomSheetForPrescription( ) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ideal_prescription, null);

        TextView txth = (TextView)view.findViewById( R.id.tv2);
        //phone_number = (EditText)view.findViewById( R.id.input_phone_number_password);
        Button submit = (Button) view.findViewById( R.id.btn_next);

        txth.setTypeface(tf_r);
        //phone_number.setTypeface(tf_r);
        submit.setTypeface(tf_r);

        //txth.setText(heading);



        mBottomSheetDialog1= new Dialog(OrderMedicineActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog1.setContentView (view);
        mBottomSheetDialog1.setCancelable(true);
        mBottomSheetDialog1.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog1.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog1.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToChoosePrescription();
            }
        });

    }

    public void openBottomSheetForPrescriptionHelp( ) {

        Paper.book("introduction").write("preshelp","0");

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ideal_prescription, null);

        TextView txth = (TextView)view.findViewById( R.id.tv2);
        //phone_number = (EditText)view.findViewById( R.id.input_phone_number_password);
        Button submit = (Button) view.findViewById( R.id.btn_next);
        submit.setVisibility(View.GONE);

        txth.setTypeface(tf_r);
        //phone_number.setTypeface(tf_r);
        submit.setTypeface(tf_r);

        //txth.setText(heading);



        mBottomSheetDialog1= new Dialog(OrderMedicineActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog1.setContentView (view);
        mBottomSheetDialog1.setCancelable(true);
        mBottomSheetDialog1.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog1.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog1.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToChoosePrescription();
            }
        });

    }

    public void openBottomSheetForPharmacyRules() {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pharmacy_rules, null);

        TextView txth = (TextView)view.findViewById( R.id.tv2);

        WebView wv = (WebView) view.findViewById(R.id.webViewwhy);
        wv.loadUrl(""+MainActivity.whyPrescriptionUrl);

        txth.setTypeface(tf_r);

        Dialog mBottomSheetDialog = new Dialog (OrderMedicineActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog.show();

    }



    @Override
    public void onBackPressed() {

        finish();

        MainActivity.fromMedicineDetailsChat=false;
        MainActivity.fromHomeToChat=false;


            Intent intent_main = new Intent(getApplicationContext(),MainActivity.class);
            intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getApplicationContext().startActivity(intent_main);




        super.onBackPressed();

    }
    boolean isPincodeValid(String pincode){

        if (pincode.length()==6){
            try {
                double d = Double.parseDouble(pincode);
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;
        }
        else
        {
            wizProgress.hideNow();
            pincodeet.setError("Please enter a valid pincode.");
            return false;

        }


    }
    public void openBottomSheetForAskPincode( ) {

        // Paper.book("introduction").write("askpin","1");

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_ask_pincode, null);

        pincodecity = (TextView)view.findViewById( R.id.pro_pincode);
        pincodeet = (EditText)view.findViewById( R.id.pro_pincode_ET);
        wizProgress = (DilatingDotsProgressBar) view.findViewById(R.id.pro_progress);
        Button submit = (Button) view.findViewById( R.id.btn_pro_submit);
        Button back = (Button) view.findViewById( R.id.btn_pro_back);

        pincodecity.setTypeface(tf_r);
        pincodeet.setTypeface(tf_r);
        submit.setTypeface(tf_r);back.setTypeface(tf_r);


        mBottomSheetDialog4= new Dialog(OrderMedicineActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog4.setContentView (view);
        mBottomSheetDialog4.setCancelable(false);
        mBottomSheetDialog4.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog4.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog4.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePincode();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main = new Intent(getApplicationContext(),MainActivity.class);
                intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(intent_main);
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
    public void updatePincode(){


        try {
            SessionManager session = new SessionManager(OrderMedicineActivity.this);

            HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester = new RequestBuilder(OrderMedicineActivity.this)
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
                    .buildObjectRequester(new JsonObjectListenerToUpdatePincode()); //or .buildArrayRequester(listener);
            //http://truemd-carebook.rhcloud.com/checkUser?mobile=9581649079
            mRequester.request(Methods.GET, MainActivity.app_url+"/check_pincode?pincode=" + pincodeet.getText().toString() );

        }  catch (Exception e) {

            Log.e("updateUser: ",""+e.getMessage());
            e.printStackTrace();

        }

    }


    private class JsonObjectListenerToUpdatePincode extends Response.SimpleObjectResponse {


        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {

                if (jsonObject.toString().length() > 5) {


                    if (jsonObject.optBoolean("status") == false) {

                        Paper.book("user").write("pincode", isPincodeValid(pincodeet.getText().toString())  );
                        Paper.book("user").write("pincode_city", jsonObject.optString("message")  );

                        wizProgress.hideNow();
                        pincodecity.setText(Paper.book("user").read("pincode_city").toString());

                        //mBottomSheetDialog4.dismiss();


                    } else {


                        Log.e("PreLogin resp: ", "" + jsonObject);
                        try {

                            Paper.book("user").write("pincode",  isPincodeValid(pincodeet.getText().toString())?pincodeet.getText().toString():"" );
                            Paper.book("user").write("pincode_city", jsonObject.optString("city")+", "+ jsonObject.optString("state")+"\n\nWe are delivering in your pincode area."   );
                            wizProgress.hideNow();

                            pincodecity.setText(Paper.book("user").read("pincode_city").toString());
                            mBottomSheetDialog4.dismiss();
                            String cityname =Paper.book("user").read("pincode_city").toString();

                            t2.setText(cityname.substring(0,cityname.indexOf(','))+": "+MainActivity.shippingMsg);
                             Paper.book("introduction").write("askpin","1");


                            ContentValues codecodepair = new ContentValues();

                            codecodepair.put("pincode", isPincodeValid(pincodeet.getText().toString())?pincodeet.getText().toString():"");
                            try {
                                JSONObject orderJsonObject = getJsonObjectFromContentValues("user", codecodepair);

                                if(isPincodeValid(pincodeet.getText().toString()))
                                    updateUser(orderJsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("PreLogin err: ", "" + e.getMessage());

                        }

                        //lv.setAdapter(new CustomUpcomingOrderAdapter(getApplicationContext(),dateArray,orderNoArray,nameArray, statusArray, dotsArray, gTotalArray, deliveryTimeArray,aljoPreLogins  ));
                    }
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
                new SweetAlertDialog(OrderMedicineActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("There might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                OrderMedicineActivity.this.finish();
                                //System.exit(0);

                            }
                        })

                        .show();
            }


        }



    }

    public void updateUser(JSONObject orderJsonObject){

            wizProgress.showNow();
        try {
            SessionManager session = new SessionManager(OrderMedicineActivity.this);

            HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester = new RequestBuilder(OrderMedicineActivity.this)
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


                        Toast.makeText(getApplicationContext(), "Profile Saved.", Toast.LENGTH_SHORT).show();

                        //scroll.fullScroll(View.FOCUS_DOWN);

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
                new SweetAlertDialog(OrderMedicineActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("There might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                OrderMedicineActivity.this.finish();
                                //System.exit(0);

                            }
                        })

                        .show();
            }


        }



    }

    }
