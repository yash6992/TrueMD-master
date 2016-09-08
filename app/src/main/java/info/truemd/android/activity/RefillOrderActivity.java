package info.truemd.android.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonArrayRequester;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.android.R;
import info.truemd.android.adapter.CustomAddressBSAdapter;
import info.truemd.android.adapter.CustomCouponBSAdapter;
import info.truemd.android.adapter.CustomLanguageBSAdapter;
import info.truemd.android.adapter.CustomRefillAddressBSAdapter;
import info.truemd.android.helper.SessionManager;
import info.truemd.android.helper.TrueMDJSONUtils;

public class RefillOrderActivity extends AppCompatActivity {

    JSONObject orderToBeRefilled;


    ImageButton backCOIB;TextView submitCOIB; DilatingDotsProgressBar mDilatingDotsProgressBar;
    ArrayList<Bitmap> confirmBitmapList;static SessionManager session; static int completeUpload;
    ScrollView scrollCO; String orderdollaroid;
    RelativeLayout deliveryCO, pickupCO; String []  language;ArrayList<String> coupon, valid,details,addresstypetosend, addressvaluetosend;
    TextView discount,couponHCO,couponTCO,couponST, couponD, submitcouponTVCO,titleCO,yourOrderCO, pickupHCO, pickupTVCO, pickupD, pickupST, deliveryHCO,deliveryD, deliveryST, languageHCO,languageST, languageD,languageTVCO; CheckBox radioButtonCO;
    EditText couponCOET, commentsCO; ArrayList<JSONObject> documentsUploaded;LinearLayout couponLLCO;
    public static ArrayList<JSONObject> pickupjarray, deliveryjarray;
    public static String deliveryAddressFinal, pickupAddressFinal, couponFinal, languageFinal, orderidFinal, couponDetailsFinal;
    public static Dialog mBottomSheetDialog; public static  JSONObject pickupSelectedjobj, deliverySelectedjobj, selectedAddressjobj;
    public static  TextView deliveryTVCO;
    public static String orderBucket ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_order);
        session = new SessionManager(RefillOrderActivity.this);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        Typeface tf_l=Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_r=Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress_3);
        couponCOET = (EditText) findViewById(R.id.coupon_et); couponCOET.setTypeface(tf_l);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(couponCOET.getWindowToken(), 0);
        commentsCO = (EditText) findViewById(R.id.comments_co); commentsCO.setTypeface(tf_l);
        InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(commentsCO.getWindowToken(), 0);



        deliveryCO = (RelativeLayout) findViewById(R.id.pickup_address_co);
        pickupCO = (RelativeLayout) findViewById(R.id.delivery_address_co);
        scrollCO = (ScrollView) findViewById(R.id.scroll_co);

        backCOIB = (ImageButton) findViewById(R.id.backImageButtonMark_co);
        submitCOIB = (TextView) findViewById(R.id.CheckImageButtonMark_co); submitCOIB.setTypeface(tf_l);

        couponHCO = (TextView) findViewById(R.id.coupon_coTV);couponHCO.setTypeface(tf_l);
        couponTCO = (TextView) findViewById(R.id.coupon_coH);couponTCO.setTypeface(tf_r);
        couponLLCO = (LinearLayout) findViewById(R.id.ll_add_coupon);couponLLCO.setVisibility(View.GONE);
        submitcouponTVCO = (TextView) findViewById(R.id.submit_coupon_tv);submitcouponTVCO.setTypeface(tf_l);
        languageHCO = (TextView) findViewById(R.id.language_coH);languageHCO.setTypeface(tf_r);
        languageTVCO = (TextView) findViewById(R.id.language_coTV);languageTVCO.setTypeface(tf_l);
        pickupHCO = (TextView) findViewById(R.id.delivery_address_coH);pickupHCO.setTypeface(tf_r);
        pickupTVCO = (TextView) findViewById(R.id.delivery_address_coTV);pickupTVCO.setTypeface(tf_l);
        deliveryHCO = (TextView) findViewById(R.id.pickup_address_coH);deliveryHCO.setTypeface(tf_r);
        deliveryTVCO = (TextView) findViewById(R.id.pickup_address_coTV);deliveryTVCO.setTypeface(tf_l);
        titleCO = (TextView) findViewById(R.id.title_co);titleCO.setTypeface(tf_l);
        yourOrderCO = (TextView) findViewById(R.id.your_order_co);yourOrderCO.setTypeface(tf_r);
        discount = (TextView) findViewById(R.id.discount_msg);
        couponST = (TextView) findViewById(R.id.coupon_co_ST);couponST.setTypeface(tf_l);
        couponD = (TextView) findViewById(R.id.coupon_descrip);couponD.setTypeface(tf_r);
        pickupST = (TextView) findViewById(R.id.delivery_address_coST);pickupST.setTypeface(tf_l);
        deliveryST = (TextView) findViewById(R.id.pickup_address_coST);deliveryST.setTypeface(tf_l);
        languageST = (TextView) findViewById(R.id.language_coST);languageST.setTypeface(tf_l);

        radioButtonCO = (CheckBox) findViewById(R.id.addressRadioButton_co);radioButtonCO.setTypeface(tf_l);
        pickupCO.setVisibility(View.GONE);
        radioButtonCO.setChecked(true);

        discount.setText(MainActivity.discountMsg);

        try {
            orderToBeRefilled = new JSONObject(getIntent().getStringExtra("refillJOS"));
            pickupSelectedjobj  = orderToBeRefilled.getJSONObject("delivery_address");
            deliverySelectedjobj = orderToBeRefilled.getJSONObject("delivery_address");
            selectedAddressjobj = orderToBeRefilled.getJSONObject("delivery_address");
            languageFinal=orderToBeRefilled.optString("language");
            commentsCO.setText(""+orderToBeRefilled.optString("notes"));
            JSONObject _id= orderToBeRefilled.getJSONObject("_id");
            orderdollaroid=_id.optString("$oid");
            //orderidFinal= StringUtils.leftPad(String.valueOf((int) Math.ceil(Math.random() * 100000)), 6, "0");
            orderBucket = orderToBeRefilled.getString("order_bucket");


        } catch (JSONException e) {
            Log.e("RefillOrderIni: ", "address not initialized"+e.getMessage());
            e.printStackTrace();
        }


        documentsUploaded = new ArrayList<JSONObject>();
        pickupjarray= new ArrayList<JSONObject>();
        deliveryjarray= new ArrayList<JSONObject>();


        deliveryAddressFinal=addressValueFromJO(deliverySelectedjobj);
        pickupAddressFinal=addressValueFromJO(pickupSelectedjobj);
        couponFinal="";couponDetailsFinal="";

        //completeUpload=0;
        coupon= new ArrayList<String>();
        valid= new ArrayList<String>();
        details= new ArrayList<String>();
        addresstypetosend= new ArrayList<String>();
        addressvaluetosend= new ArrayList<String>();

        yourOrderCO.setText(orderToBeRefilled.optString("patient_name"));


        //EditText couponET = (EditText) findViewById(R.id.coupon_et); couponET.setTypeface(tf_r);
        //TextView cancel = (TextView) findViewById(R.id.submit_coupon_tv); cancel.setTypeface(tf_r);
        couponCOET.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                if (couponCOET.getText().toString().length() == 0) {
                    submitcouponTVCO.setText("Cancel");

                } else {
                    submitcouponTVCO.setText("Apply");

                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (couponCOET.getText().toString().length() == 0) {
                    submitcouponTVCO.setText("Cancel");

                } else {
                    submitcouponTVCO.setText("Apply");

                }

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (couponCOET.getText().toString().length() == 0) {
                    submitcouponTVCO.setText("Cancel");

                } else {
                    submitcouponTVCO.setText("Apply");

                }


            }
        });



        submitcouponTVCO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (couponCOET.getText().toString().length() > 1 && submitcouponTVCO.getText().toString().equalsIgnoreCase("Apply")) {
                    checkCouponCodeValidity(couponCOET.getText().toString());
                }
                else if (submitcouponTVCO.getText().toString().equalsIgnoreCase("Cancel"))
                {
                    couponLLCO.setVisibility(View.GONE);
                    couponTCO.setVisibility(View.VISIBLE);
                }


            }
        });



        Log.e("CO::", "before usercouponoperation execute");
        getUserCoupon();
        getAddress();
        Log.e("CO::", "after usercouponoperation execute");




        language= new String[3];language[0]="English";language[1]="Hindi";language[2]="Marathi";

        HashMap<String, String> user = session.getUserDetails();




        View.OnClickListener clicklistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";

                switch (v.getId()) {

                    case R.id.delivery_address_coTV:
                        if(addresstypetosend.size()==0)
                        {
                            Intent toAddAddress = new Intent(RefillOrderActivity.this, RefillAddAddressActivity.class);

                            startActivity(toAddAddress);
                        }
                        else
                            openBottomSheetAddress("Select Address", false, addresstypetosend, addressvaluetosend);
                        break;

                    case R.id.pickup_address_coTV:
                        if(addresstypetosend.size()==0)
                        {
                            Intent toAddAddress = new Intent(RefillOrderActivity.this, RefillAddAddressActivity.class);

                            startActivity(toAddAddress);
                        }
                        else
                            openBottomSheetAddress("Select Address", true, addresstypetosend, addressvaluetosend);

                        //openBottomSheetAddress("Select Address", false, addresstypetosend, addressvaluetosend);
                        break;
                    case R.id.language_coTV:
                        openBottomSheetLanguage("Select Language", language);
                        break;
                    case R.id.coupon_coH:
                        couponLLCO.setVisibility(View.VISIBLE);
                        couponTCO.setVisibility(View.GONE);
                        scrollCO.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollCO.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                        break;
                    case R.id.coupon_coTV:
                        if(addresstypetosend.size()==0)
                        {
                            Toast.makeText(RefillOrderActivity.this,"You don't have any personal coupon codes.",Toast.LENGTH_SHORT).show();
                        }
                        else
                            openBottomSheetCoupon("Select Coupon", coupon, valid, details);
                        break;
                    case R.id.backImageButtonMark_co:
                        onBackPressed();
                        break;
                    case R.id.CheckImageButtonMark_co:
                        Log.e("RefillOrder:","RefillBtnClicked.");

                        if(TrueMDJSONUtils.isEmpty(selectedAddressjobj)) {
                            Toast.makeText(RefillOrderActivity.this, "Please select the delivery address.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.e("RefillOrder:","RefillBtnClicked.");
                            if(orderdollaroid.length()>5)
                                refillOrder();
                            else
                                Toast.makeText(RefillOrderActivity.this, "There might some network issue. Please try Again.", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    default:
                        break;


                }


            }
        };



        deliveryTVCO.setText(deliveryAddressFinal);
        pickupTVCO.setText(pickupAddressFinal);
        languageTVCO.setText(languageFinal);

        couponTCO.setOnClickListener(clicklistener);
        backCOIB.setOnClickListener(clicklistener);
        submitCOIB.setOnClickListener(clicklistener);
        deliveryTVCO.setOnClickListener(clicklistener);
        pickupTVCO.setOnClickListener(clicklistener);
        languageTVCO.setOnClickListener(clicklistener);
        couponHCO.setOnClickListener(clicklistener);

    }


    public void openBottomSheetAddress ( String heading,boolean delivery, ArrayList<String> addressType, ArrayList<String> addressValue) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_address, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);

        String [] addressTypeArray = addressType.toArray(new String[coupon.size()]);
        String [] addressValueArray = addressValue.toArray( new String[valid.size()]);



        ListView lv=(ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new CustomRefillAddressBSAdapter(view.getContext(), addressTypeArray, addressValueArray));

        TextView addAddress = (TextView) view.findViewById(R.id.add_address_button_bs);


        txth.setTypeface(tf_r);

        txth.setText(heading);

        mBottomSheetDialog = new Dialog (RefillOrderActivity.this,
                R.style.MaterialDialogSheet);

        if(delivery) {
            mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    TextView tv = (TextView) findViewById(R.id.pickup_address_coTV);
                    Log.e("CO::languageBS", "inlistener" + deliveryAddressFinal);
                    deliverySelectedjobj=selectedAddressjobj;
                    pickupSelectedjobj=selectedAddressjobj;


                    tv.setText(deliveryAddressFinal);
                }
            });
        }
        else {
            mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    TextView tv = (TextView) findViewById(R.id.delivery_address_coTV);
                    Log.e("CO::languageBS", "inlistener" + deliveryAddressFinal);
                    //deliverySelectedjobj=selectedAddressjobj;
                    pickupSelectedjobj=selectedAddressjobj;
                    tv.setText(deliveryAddressFinal);
                }
            });

        }
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toAddAddress = new Intent(RefillOrderActivity.this, RefillAddAddressActivity.class);
                if(mBottomSheetDialog.isShowing())
                    mBottomSheetDialog.dismiss();
                startActivity(toAddAddress);
            }
        });


    }
    public void openBottomSheetCoupon ( String heading,ArrayList<String> coupon, ArrayList<String> valid, ArrayList<String> details) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_coupon, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);

        String [] couponarray = coupon.toArray(new String[coupon.size()]);
        String [] validarray = valid.toArray(new String[valid.size()]);
        String [] detailsarray = details.toArray( new String[details.size()]);

        ListView lv=(ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new CustomCouponBSAdapter(view.getContext(), couponarray, validarray, detailsarray));

        txth.setTypeface(tf_r);
        txth.setText(heading);

        mBottomSheetDialog = new Dialog (RefillOrderActivity.this,
                R.style.MaterialDialogSheet);

        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                EditText tv = (EditText) findViewById(R.id.coupon_et);
                Log.e("CO::languageBS", "inlistener" + couponFinal);
                tv.setText(couponFinal);
            }
        });


        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }
    public void openBottomSheetLanguage ( String heading, String [] language) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_language, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);


        ListView lv=(ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new CustomLanguageBSAdapter(view.getContext(), language));

        txth.setTypeface(tf_r);

        txth.setText(heading);

        mBottomSheetDialog = new Dialog (RefillOrderActivity.this,
                R.style.MaterialDialogSheet);



        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                TextView tv = (TextView) findViewById(R.id.language_coTV);
                Log.e("CO::languageBS","inlistener"+languageFinal);
                tv.setText(languageFinal);
            }
        });

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        Log.e("CO::languageBS", "outlistener" + languageFinal);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((CheckBox) view).isChecked();
        //((CheckBox) view).toggle();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.addressRadioButton_co:
                if (checked)
                    pickupCO.setVisibility(View.GONE);
                else
                    pickupCO.setVisibility(View.VISIBLE);
                break;

        }
    }

    public void getUserCoupon(){
        String result = new String ();
        String line="";

        HashMap<String, String> user = session.getUserDetails();

        try {
            JsonArrayRequester mRequester;
            mRequester = new RequestBuilder(RefillOrderActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .priority(Request.Priority.NORMAL)
                    .timeOut(50000)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildArrayRequester(new JsonArrayListenerToGetUserCoupons()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, MainActivity.app_url+"/user_coupons.json");

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getAddress(){
        String result = new String ();
        String line="";

        HashMap<String, String> user = session.getUserDetails();

        try {

            JSONArray jsonArray = MainActivity.getUserObject.getJSONObject("patient").getJSONArray("addresses");
            if (jsonArray.toString().length() > 5) {


                Log.e("Siri resp: ", jsonArray.toString());

                for(int i=0; i< jsonArray.length();i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    deliveryjarray.add(obj);
                    pickupjarray.add(obj);

                    String type=obj.getString("type"); String value=addressValueFromJO(obj);

                    addresstypetosend.add(type);addressvaluetosend.add(value);
                }


            }



        }  catch (Exception e) {
            e.printStackTrace();

            Log.e("exception in add :", e.getMessage());

            try {
                JsonArrayRequester mRequester;
                mRequester = new RequestBuilder(RefillOrderActivity.this)
                        //.requestCode(REQUEST_CODE)
                        .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                        .showError(false) //Show error with toast on Network or Server error
                        .shouldCache(true)
                        .priority(Request.Priority.NORMAL)
                        .timeOut(50000)
                        .allowNullResponse(true)
                        //.tag(REQUEST_TAG)
                        .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                        .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                        .buildArrayRequester(new JsonArrayListenerToGetAddress()); //or .buildArrayRequester(listener);

                mRequester.request(Methods.GET, MainActivity.app_url+"/addresses.json");
            } catch (Exception e1) {
                Log.e("exception in add catch:", e1.getMessage());
                e1.printStackTrace();
            }

        }

    }

    public String addressValueFromJO(JSONObject obj){
        String value= null;
        String name= null;
        String line1= null;
        String line2= null;
        String landmark= null;
        String city= null;
        String pincode= null;
        try {
            String type=obj.getString("type");
            value = "";
            name = obj.getString("name");
            line1 = obj.getString("line1");
            line2 = obj.getString("line2");
            landmark = obj.getString("landmark");
            city = obj.getString("city");
            pincode = obj.getString("pincode");

            JSONObject geoObj=obj.getJSONObject("geolocation");
            String lat=geoObj.optString("lat");
            String lon=geoObj.optString("lon");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        value= name+", "+line1+", "+line2+", "+landmark+", "+city+"- "+pincode;
        return value;
    }



    public boolean checkCouponCodeValidity(String couponCode){

        ContentValues codecodepair = new ContentValues();
        codecodepair.put("code", couponCode);

        boolean couponValidBool=true;

        try {
            JSONObject couponJsonObject = getJsonObjectFromContentValues("coupon", codecodepair);
            HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(RefillOrderActivity.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .priority(Request.Priority.NORMAL)
                    .timeOut(50000)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("Content-Type", "application/json")
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToCheckCoupons()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.POST, MainActivity.app_url+"/check_coupon.json", couponJsonObject);




        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return couponValidBool;

    }

    String orderValid(){

        return "ok";
    }

    public void refillOrder(){

        Log.e("in submitOrder():: ", "order submission started");

        String orderValid= orderValid();

        if(orderValid.equalsIgnoreCase("ok")){

            Log.e("in submitOrder():: ", "order valid ok");

            HashMap<String, String> user = session.getUserDetails();

            ContentValues codecodepair = new ContentValues();
            //codecodepair.put("patient_name", yourOrderCO.getText().toString());
            codecodepair.put("notes", commentsCO.getText().toString());
            //codecodepair.put("order_bucket", orderBucket);
            codecodepair.put("language", languageFinal);
            codecodepair.put("coupon",couponFinal);
            codecodepair.put("contact",user.get(SessionManager.KEY_MOBILE_UM));
            //codecodepair.put("status","OCap");
            //codecodepair.put("name",MainActivity.nameFromGetUser);

            boolean couponValidBool=true;

            try {

                Log.e("in submitOrder():: ", "inside try");

                JSONObject orderJsonObject = getJsonObjectOrder("order", codecodepair, selectedAddressjobj, pickupSelectedjobj, documentsUploaded);
                // HashMap<String, String> user = session.getUserDetails();

                JsonObjectRequester mRequester;
                mRequester = new RequestBuilder(RefillOrderActivity.this)
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
                        .buildObjectRequester(new JsonObjectListenerToOrder()); //or .buildArrayRequester(listener);

                Log.e("in submitOrder():: ", orderJsonObject.toString());


                mRequester.request(Methods.PUT, MainActivity.app_url+"/orders/"+orderdollaroid+"/refill.json", orderJsonObject);




            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        else{

            Toast.makeText(RefillOrderActivity.this,orderValid,Toast.LENGTH_SHORT).show();


        }



    }

    private class JsonArrayListenerToGetUserCoupons extends Response.SimpleArrayResponse {


        @Override
        public void onResponse(int requestCode, @Nullable JSONArray jsonArray) {
            //Ok
            try {
                if (jsonArray.toString().length() > 5) {


                    Log.e("Siri resp: ", jsonArray.toString());

                    for(int i=0; i< jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String couponCode = obj.getString("code");
                        String validCode="valid through "+ obj.getString("validity");
                        String detailsCode="";
                        String discount_type = obj.getString("discount_type");
                        int percent_discount= obj.getInt("percent_discount");
                        int rupee_discount = obj.getInt("rupee_discount");
                        int minimum_order_value = obj.getInt("minimum_order_value");

                        if(discount_type.equalsIgnoreCase("rupee"))
                        {
                            if(minimum_order_value==0)
                            {
                                detailsCode = "Rs. "+rupee_discount +" off";
                            }
                            else
                            {
                                detailsCode = "Rs. "+rupee_discount +" off on orders above Rs. "+minimum_order_value ;
                            }
                        }
                        else if (discount_type.equalsIgnoreCase("percent"))
                        {
                            if(minimum_order_value==0)
                            {
                                detailsCode = percent_discount +" % off";
                            }
                            else
                            {
                                detailsCode = percent_discount +" % off on orders above Rs. "+minimum_order_value ;
                            }
                        }


                        coupon.add(couponCode);valid.add(validCode);details.add(detailsCode);

                    }


                }
            } catch (JSONException e) {
                Log.e("1 err: ",""+e.getMessage());
                e.printStackTrace();
            }
        }


        @Override
        public void onRequestStart(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()!=View.VISIBLE)
                mDilatingDotsProgressBar.showNow();
        }



        @Override
        public void onRequestFinish(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()==View.VISIBLE)
                mDilatingDotsProgressBar.hideNow();
//
//            if(coupon.size()>0)
//                Toast.makeText(RefillOrderActivity.this, "User coupons received.", Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(RefillOrderActivity.this, "No user coupons", Toast.LENGTH_SHORT).show();
        }

    }

    private class JsonArrayListenerToGetAddress extends Response.SimpleArrayResponse {

        @Override
        public void onResponse(int requestCode, @Nullable JSONArray jsonArray) {
            //Ok
            try {
                if (jsonArray.toString().length() > 5) {


                    Log.e("Siri resp: ", jsonArray.toString());

                    for(int i=0; i< jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        deliveryjarray.add(obj);
                        pickupjarray.add(obj);

                        String type=obj.getString("type"); String value="";
                        String name=obj.getString("name");
                        String line1=obj.getString("line1");
                        String line2=obj.getString("line2");
                        String landmark=obj.getString("landmark");
                        String city=obj.getString("city");
                        String pincode=obj.getString("pincode");

                        JSONObject geoObj=obj.getJSONObject("geolocation");
                        String lat=geoObj.optString("lat");
                        String lon=geoObj.optString("lon");

                        value= name+", "+line1+", "+line2+", "+landmark+", "+city+"- "+pincode;

                        addresstypetosend.add(type);addressvaluetosend.add(value);
                    }


                }
            } catch (Exception e) {
                Log.e("2 err: ",""+e.getMessage());
                e.printStackTrace();
            }
        }


        @Override
        public void onRequestStart(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()!=View.VISIBLE)
                mDilatingDotsProgressBar.showNow();
        }




        @Override
        public void onRequestFinish(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()==View.VISIBLE)
                mDilatingDotsProgressBar.hideNow();

            Log.e("ListenerToGetAddress", "before");
            Log.e("deliveryjarray", deliveryjarray.toString());
            Log.e("ListenerToGetAddress", "after");


            //Toast.makeText(RefillOrderActivity.this, "User address received.", Toast.LENGTH_SHORT).show();
        }



        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            mDilatingDotsProgressBar.hideNow();
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());
            if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
            {
                new SweetAlertDialog(RefillOrderActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("There might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                RefillOrderActivity.this.finish();
                                // System.exit(0);

                            }
                        })

                        .show();
            }


        }

    }


    private class JsonObjectListenerToCheckCoupons extends Response.SimpleObjectResponse {

        String code, message; Boolean validity;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {



                    Log.e("Siri resp: ", jsonObject.toString());

                    JSONObject obj = jsonObject;
                    code = obj.getString("code");
                    validity = obj.getBoolean("validity");
                    message = obj.getString("message");
                    if(validity) {
                        couponFinal = code;
                        couponDetailsFinal = message;
                    }
                    else{
                        couponFinal="";
                        couponDetailsFinal="";
                        couponCOET.setText("");
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onRequestStart(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()!=View.VISIBLE)
                mDilatingDotsProgressBar.showNow();

        }



        @Override
        public void onRequestFinish(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()==View.VISIBLE)
                mDilatingDotsProgressBar.hideNow();


            Toast.makeText(RefillOrderActivity.this, message, Toast.LENGTH_SHORT).show();
            submitcouponTVCO.setText("Cancel");

        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());


            mDilatingDotsProgressBar.hideNow();

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            mDilatingDotsProgressBar.hideNow();
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());
            if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
            {
                new SweetAlertDialog(RefillOrderActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("There might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                RefillOrderActivity.this.finish();
                                // System.exit(0);

                            }
                        })

                        .show();
            }


        }

    }

    private class JsonObjectListenerToOrder extends Response.SimpleObjectResponse {

        String delivery_time, pickup_time, status, situation,reason;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {


                    Log.e("order resp: ", jsonObject.toString());

                    JSONObject obj = jsonObject;
                    delivery_time = obj.getString("delivery_time");
                    pickup_time = obj.getString("pickup_time");
                    status = obj.getString("status");

                    situation="success";
                    reason ="Order successful";
                    try {
                        if(situation.equalsIgnoreCase("success"))
                        {
                            Intent i=new Intent(RefillOrderActivity.this, ThankYouForOrderActivity.class);
                            i.putExtra("order_id", orderBucket.substring(orderBucket.indexOf('#')+1));
                            i.putExtra("delivery_slot",delivery_time);
                            i.putExtra("delivery_address",deliveryAddressFinal);
                            i.putExtra("coupon",couponFinal);
                            i.putExtra("coupon_details",couponDetailsFinal);
                            startActivity(i);
                        }
                        else
                            Toast.makeText(RefillOrderActivity.this, reason, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    situation="fail";
                    reason= "Order can't be submitted due to network issues. Try Again";
                }
            } catch (Exception e) {
                Log.e("3err: ",""+e.getMessage());
                e.printStackTrace();
            }
        }


        @Override
        public void onRequestStart(int requestCode) {

            if(mDilatingDotsProgressBar.getVisibility()!=View.VISIBLE)
                mDilatingDotsProgressBar.showNow();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }



        @Override
        public void onRequestFinish(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()==View.VISIBLE)
                mDilatingDotsProgressBar.hideNow();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());


            mDilatingDotsProgressBar.hideNow();

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            mDilatingDotsProgressBar.hideNow();
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());
            if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
            {
                new SweetAlertDialog(RefillOrderActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("There might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                RefillOrderActivity.this.finish();
                               // System.exit(0);

                            }
                        })

                        .show();
            }


        }


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



    private static JSONObject getJsonObjectOrder(String key_tag, ContentValues params, JSONObject delivery, JSONObject pickup, ArrayList<JSONObject> documents ) throws JSONException {

        //Stores JSON
        JSONObject holder = new JSONObject();

        //object for storing Json
        JSONObject data = new JSONObject();


        for (Map.Entry<String, Object> entry : params.valueSet()) {
            String key = entry.getKey(); // name
            String value = entry.getValue().toString(); // value
            data.put(key, value);
        }

        data.put("pickup_address", pickup);
        data.put("delivery_address",delivery);

        JSONArray document_jsonarray = new JSONArray(documents);

        data.put("documents", document_jsonarray);
        //puts email and 'foo@bar.com'  together in map
        holder.put(key_tag, data);

        return holder;
    }



}


