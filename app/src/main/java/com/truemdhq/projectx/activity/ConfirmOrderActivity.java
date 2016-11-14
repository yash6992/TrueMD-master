package com.truemdhq.projectx.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import android.widget.ImageView;
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
import com.truemdhq.projectx.adapter.CustomInvoiceListAdapter;
import com.wang.avi.AVLoadingIndicatorView;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.adapter.CustomAddressBSAdapter;
import com.truemdhq.projectx.adapter.CustomCouponBSAdapter;
import com.truemdhq.projectx.adapter.CustomInvoiceListAdapter;
import com.truemdhq.projectx.helper.SessionManager;
import com.truemdhq.projectx.helper.ProjectXJSONUtils;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 20/04/16.
 */
public class ConfirmOrderActivity extends AppCompatActivity {


    ImageView coImage0,coImage1,coImage2,coImage3,coImage4,coImage5,coImage6,coImage7,coImage8,coImage9;
    AVLoadingIndicatorView coli0,coli1,coli2,coli3,coli4,coli5,coli6,coli7,coli8,coli9;
    LinearLayout coll0,coll1,coll2,coll3,coll4,coll5,coll6,coll7,coll8,coll9;
    ImageButton backCOIB;TextView submitCOIB; DilatingDotsProgressBar mDilatingDotsProgressBar;
    ArrayList<LinearLayout> llList; boolean errorUploading=false;
    ArrayList<AVLoadingIndicatorView> loadingList;
    ScrollView scrollCO; CardView deliveryCO;
    ArrayList<Bitmap> confirmBitmapList;static SessionManager session; static int completeUpload;
    RelativeLayout  pickupCO; String []  language;ArrayList<String> coupon, valid,details,addresstypetosend, addressvaluetosend;
    TextView deliveryConditions, discount,couponHCO,couponTCO,couponST, couponD, submitcouponTVCO,titleCO, pickupHCO, pickupTVCO, pickupD, pickupST, deliveryHCO,deliveryD, deliveryST, languageHCO,languageST, languageD,languageTVCO; CheckBox radioButtonCO;
    EditText couponCOET,yourOrderCO, commentsCO; ArrayList<JSONObject> documentsUploaded;LinearLayout couponLLCO;
    public static ArrayList<JSONObject> pickupjarray, deliveryjarray;
    public static TextView deliveryTVCO;
    public static String deliveryAddressFinal, pickupAddressFinal, couponFinal, languageFinal, orderidFinal, couponDetailsFinal;
    public static Dialog mBottomSheetDialog; public static  JSONObject pickupSelectedjobj, deliverySelectedjobj, selectedAddressjobj;

    public static String orderBucket ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        session = new SessionManager(ConfirmOrderActivity.this);

        Intent fromMark = getIntent();
        final ArrayList<Uri> confirmUriList = fromMark.getParcelableArrayListExtra("imageList");
        confirmBitmapList = new ArrayList<Bitmap>();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        deliveryAddressFinal="No address selected";
        pickupAddressFinal="No address selected";
        couponFinal="";couponDetailsFinal="";
        languageFinal="English";

        completeUpload=0;
        coupon= new ArrayList<String>();
        valid= new ArrayList<String>();
        details= new ArrayList<String>();
        addresstypetosend= new ArrayList<String>();
        addressvaluetosend= new ArrayList<String>();

        pickupSelectedjobj  = new JSONObject();
        deliverySelectedjobj = new JSONObject();
        selectedAddressjobj = new JSONObject();
        documentsUploaded = new ArrayList<JSONObject>();
        pickupjarray= new ArrayList<JSONObject>();
        deliveryjarray= new ArrayList<JSONObject>();

        Typeface tf_l=Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");
        Typeface tf_r=Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress_3);
        couponCOET = (EditText) findViewById(R.id.coupon_et); couponCOET.setTypeface(tf_l);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(couponCOET.getWindowToken(), 0);
        commentsCO = (EditText) findViewById(R.id.comments_co); commentsCO.setTypeface(tf_l);
        InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(commentsCO.getWindowToken(), 0);

        coli0=(AVLoadingIndicatorView)findViewById(R.id.load0);
        coli1=(AVLoadingIndicatorView)findViewById(R.id.load1);
        coli2=(AVLoadingIndicatorView)findViewById(R.id.load2);
        coli3=(AVLoadingIndicatorView)findViewById(R.id.load3);
        coli4=(AVLoadingIndicatorView)findViewById(R.id.load4);
        coli5=(AVLoadingIndicatorView)findViewById(R.id.load5);
        coli6=(AVLoadingIndicatorView)findViewById(R.id.load6);
        coli7=(AVLoadingIndicatorView)findViewById(R.id.load7);
        coli8=(AVLoadingIndicatorView)findViewById(R.id.load8);
        coli9=(AVLoadingIndicatorView)findViewById(R.id.load9);


        coll0=(LinearLayout)findViewById(R.id.coL0);
        coll1=(LinearLayout)findViewById(R.id.coL1);
        coll2=(LinearLayout)findViewById(R.id.coL2);
        coll3=(LinearLayout)findViewById(R.id.coL3);
        coll4=(LinearLayout)findViewById(R.id.coL4);
        coll5=(LinearLayout)findViewById(R.id.coL5);
        coll6=(LinearLayout)findViewById(R.id.coL6);
        coll7=(LinearLayout)findViewById(R.id.coL7);
        coll8=(LinearLayout)findViewById(R.id.coL8);
        coll9=(LinearLayout)findViewById(R.id.coL9);


        coImage0=(ImageView)findViewById(R.id.coImage0);
        coImage1=(ImageView)findViewById(R.id.coImage1);
        coImage2=(ImageView)findViewById(R.id.coImage2);
        coImage3=(ImageView)findViewById(R.id.coImage3);
        coImage4=(ImageView)findViewById(R.id.coImage4);
        coImage5=(ImageView)findViewById(R.id.coImage5);
        coImage6=(ImageView)findViewById(R.id.coImage6);
        coImage7=(ImageView)findViewById(R.id.coImage7);
        coImage8=(ImageView)findViewById(R.id.coImage8);
        coImage9=(ImageView)findViewById(R.id.coImage9);

        llList = new ArrayList<LinearLayout>();

        llList.add(coll0);
        llList.add(coll1);
        llList.add(coll2);
        llList.add(coll3);
        llList.add(coll4);
        llList.add(coll5);
        llList.add(coll6);
        llList.add(coll7);
        llList.add(coll8);
        llList.add(coll9);


        loadingList = new ArrayList<AVLoadingIndicatorView>();

        loadingList.add(coli0);
        loadingList.add(coli1);
        loadingList.add(coli2);
        loadingList.add(coli3);
        loadingList.add(coli4);
        loadingList.add(coli5);
        loadingList.add(coli6);
        loadingList.add(coli7);
        loadingList.add(coli8);
        loadingList.add(coli9);


        scrollCO=(ScrollView)findViewById(R.id.scroll_co);

        final ArrayList<ImageView> confirmImageList = new ArrayList<ImageView>();

        confirmImageList.add(coImage0);
        confirmImageList.add(coImage1);
        confirmImageList.add(coImage2);
        confirmImageList.add(coImage3);
        confirmImageList.add(coImage4);
        confirmImageList.add(coImage5);
        confirmImageList.add(coImage6);
        confirmImageList.add(coImage7);
        confirmImageList.add(coImage8);
        confirmImageList.add(coImage9);

        deliveryCO = (CardView) findViewById(R.id.pickup_address_co);
        pickupCO = (RelativeLayout) findViewById(R.id.delivery_address_co);

        backCOIB = (ImageButton) findViewById(R.id.backImageButtonMark_co);
        submitCOIB = (TextView) findViewById(R.id.CheckImageButtonMark_co); submitCOIB.setTypeface(tf_l);

        deliveryConditions = (TextView) findViewById(R.id.delivery_conditions);deliveryConditions.setTypeface(tf_l);
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
        yourOrderCO = (EditText) findViewById(R.id.your_order_co);yourOrderCO.setTypeface(tf_r);
        discount = (TextView) findViewById(R.id.discount_msg);discount.setTypeface(tf_l);
        couponST = (TextView) findViewById(R.id.coupon_co_ST);couponST.setTypeface(tf_l);
        couponD = (TextView) findViewById(R.id.coupon_descrip);couponD.setTypeface(tf_r);
        pickupST = (TextView) findViewById(R.id.delivery_address_coST);pickupST.setTypeface(tf_l);
        deliveryST = (TextView) findViewById(R.id.pickup_address_coST);deliveryST.setTypeface(tf_l);
        languageST = (TextView) findViewById(R.id.language_coST);languageST.setTypeface(tf_l);

        radioButtonCO = (CheckBox) findViewById(R.id.addressRadioButton_co);radioButtonCO.setTypeface(tf_l);
        pickupCO.setVisibility(View.GONE);
        radioButtonCO.setChecked(true);

        discount.setText(MainActivity.discountMsg);

        String pincode = Paper.book("user").read("pincode");

        String cityname =Paper.book("user").read("pincode_city");

        if(pincode.startsWith("4520"))
            deliveryConditions.setText(cityname.substring(0,cityname.indexOf(','))+": Free home delivery for all orders.");
        else
            deliveryConditions.setText(cityname.substring(0,cityname.indexOf(','))+": "+MainActivity.shippingMsg);

        yourOrderCO.setText(Paper.book("user").read("name", ""));

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

        orderidFinal=StringUtils.leftPad(String.valueOf((int) Math.ceil(Math.random() * 100000)), 6, "0");
        orderBucket = user.get(SessionManager.KEY_MOBILE_UM) +"#"+orderidFinal;

        int i=0;
        for (i=0;i<confirmUriList.size();i++)
        {
            setImageFromUri(confirmImageList.get(i),confirmUriList.get(i));
            loadingList.get(i).setVisibility(View.GONE);
            llList.get(i).setVisibility(View.GONE);
        }
        for (;i<10;i++)
        {
            confirmImageList.get(i).setVisibility(View.GONE);
            loadingList.get(i).setVisibility(View.GONE);
            llList.get(i).setVisibility(View.GONE);

        }

        View.OnClickListener clicklistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";

                switch (v.getId()) {
                    case R.id.coImage0:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage0);
                        break;
                    case R.id.coImage1:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage1);
                        break;
                    case R.id.coImage2:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage2);
                        break;
                    case R.id.coImage3:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage3);
                        break;
                    case R.id.coImage4:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage4);
                        break;
                    case R.id.coImage5:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage5);
                        break;
                    case R.id.coImage6:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage6);
                        break;
                    case R.id.coImage7:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage7);
                        break;
                    case R.id.coImage8:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage8);
                        break;
                    case R.id.coImage9:
                        Log.e("Confirm: ", "in BS IV");
                        openBottomSheetImageViewer(coImage9);
                        break;
                    case R.id.delivery_address_coTV:
                        if (addresstypetosend.size() == 0) {
                            Intent toAddAddress = new Intent(ConfirmOrderActivity.this, AddAddressActivity.class);

                            startActivity(toAddAddress);
                        } else
                            openBottomSheetAddress("Select Address", false, addresstypetosend, addressvaluetosend);
                        break;

                    case R.id.pickup_address_coTV:
                        if (addresstypetosend.size() == 0) {
                            Intent toAddAddress = new Intent(ConfirmOrderActivity.this, AddAddressActivity.class);

                            startActivity(toAddAddress);
                        } else
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
                        if (addresstypetosend.size() == 0) {
                            Toast.makeText(ConfirmOrderActivity.this, "You don't have any personal coupon codes.", Toast.LENGTH_SHORT).show();
                        } else
                            openBottomSheetCoupon("Select Coupon", coupon, valid, details);
                        break;
                    case R.id.backImageButtonMark_co:
                        onBackPressed();
                        break;
                    case R.id.CheckImageButtonMark_co:


                        if(couponFinal.length()!=0){
                                    if (ProjectXJSONUtils.isEmpty(selectedAddressjobj)) {
                                        Log.e("in orderValid():: ", "2 completeUpload= " + completeUpload);
                                        Log.e("in orderValid():: ", "2 delivery= " + selectedAddressjobj.toString());
                                        completeUpload = 0;
                                        documentsUploaded.clear();
                                        Toast.makeText(ConfirmOrderActivity.this, "Please select the delivery address.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mDilatingDotsProgressBar.showNow();
                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Log.e("Confirm Activity:: ", "pressed upload:: " + confirmUriList.get(0).getPath().toString());
                                        int j = 0;
                                        for (j = 0; j < confirmBitmapList.size(); j++) {
                                            new UploadBitmapOperation().execute(confirmBitmapList.get(j));
                                            loadingList.get(j).setVisibility(View.VISIBLE);
                                            llList.get(j).setVisibility(View.VISIBLE);
                                            confirmImageList.get(j).setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);


                                        }
                                    }
                                }
                        else {

                            new SweetAlertDialog(ConfirmOrderActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                    .setTitleText("No coupons selected")
                                    .setContentText("Please add coupons to get discounts.")
                                    .setCancelText("I'll add coupons.")
                                    .setConfirmText("Continue")
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            if (ProjectXJSONUtils.isEmpty(selectedAddressjobj)) {
                                                Log.e("in orderValid():: ", "2 completeUpload= " + completeUpload);
                                                Log.e("in orderValid():: ", "2 delivery= " + selectedAddressjobj.toString());
                                                completeUpload = 0;
                                                documentsUploaded.clear();
                                                Toast.makeText(ConfirmOrderActivity.this, "Please select the delivery address.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                mDilatingDotsProgressBar.showNow();
                                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                Log.e("Confirm Activity:: ", "pressed upload:: " + confirmUriList.get(0).getPath().toString());
                                                int j = 0;
                                                for (j = 0; j < confirmBitmapList.size(); j++) {
                                                    new UploadBitmapOperation().execute(confirmBitmapList.get(j));
                                                    loadingList.get(j).setVisibility(View.VISIBLE);
                                                    llList.get(j).setVisibility(View.VISIBLE);
                                                    confirmImageList.get(j).setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);


                                                }
                                            }


                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.cancel();
                                        }
                                    })
                                    .show();
                                }


                        break;
                    default:
                        break;


                }


            }
        };

        deliveryAddressFinal="No address selected";
        pickupAddressFinal="No address selected";
        couponFinal="";
        languageFinal="English";

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

        for(int g=0; g<10;g++)
            confirmImageList.get(g).setOnClickListener(clicklistener);

    }
    @Override
    public void onBackPressed(){


        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You will have to select all prescription images again.")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        finish();
                        startActivity(new Intent(ConfirmOrderActivity.this, OrderMedicineActivity.class));

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();



    }
    public void setImageFromUri(ImageView imgView, Uri absPathUri)
    {
        if(absPathUri!=null)
        {
            Bitmap myImg = BitmapFactory.decodeFile(absPathUri.getPath());
            Matrix matrix = new Matrix();
            matrix.postRotate(0);
            Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(),myImg.getHeight(),
                    matrix, true);
            Bitmap rotated_s = Bitmap.createScaledBitmap(rotated, myImg.getWidth(), myImg.getHeight(), true);

            Bitmap converted_resized = getResizedBitmap(rotated_s, 1024);


            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            imgView.setImageBitmap(rotated);

            confirmBitmapList.add(converted_resized);
        }
    }
    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private static String multipost(String urlString, MultipartEntity reqEntity) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000000);
            conn.setConnectTimeout(1500000);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);


            HashMap<String, String> user = session.getUserDetails();

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("X-User-Token",user.get(SessionManager.KEY_AUTHENTICATION_UM));
            conn.setRequestProperty("X-User-Email",user.get(SessionManager.KEY_MOBILE_UM)+"@truemd.in");
            conn.addRequestProperty("Content-length", reqEntity.getContentLength() + "");
            conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            reqEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readStream(conn.getInputStream());
            }

        } catch (Exception e) {
            Log.e("ConfirmOrderActivity", "multipart post error " + e + "(" + urlString + ")");
        }
        return null;
    }

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }


    public class UploadBitmapOperation extends AsyncTask<Bitmap, Integer, String> {

        UploadBitmapOperation asyncObject; CountDownTimer imageUploadTimer;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            /**
             * show dialog
             */

            Log.e("RESP:: ", "on pre");

            asyncObject = this;
             imageUploadTimer = new CountDownTimer(300000, 12000) {
                public void onTick(long millisUntilFinished) {
                    // You can monitor the progress here as well by changing the onTick() time

                    //Toast.makeText(ConfirmOrderActivity.this, "Images are being uploaded.", Toast.LENGTH_SHORT).show();
                }
                public void onFinish() {
                    // stop async task if not in progress
                    if (asyncObject.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncObject.cancel(false);
                        // Add any specific task you wish to do as your extended class variable works here as well.



                        new SweetAlertDialog(ConfirmOrderActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Oops..!!")
                                .setContentText("There might be an issue with your internet connection.\n Try after some time.")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        ConfirmOrderActivity.this.finish();
                                        startActivity(new Intent(ConfirmOrderActivity.this, OrderMedicineActivity.class));
//                                            System.exit(0);

                                    }
                                })

                                .show();

                    }
                }
            };
            imageUploadTimer.start();



            new MainActivity().checkInternet(ConfirmOrderActivity.this);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground( Bitmap... messageText) {
            // TODO Auto-generated method stub
            /**
             * Do network related stuff
             * return string response.
             */
            String result = new String ();
            int count = messageText.length;String line="";

            Bitmap bitmap = messageText[0];
            String filename = orderBucket+"#####"+"truemd_android.png";
            Log.e("CO_doInBg resp: ", "in Do in bg" );
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            ContentBody contentPart = new ByteArrayBody(bos.toByteArray(), filename);

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("file", contentPart);
            String response = ""+multipost(MainActivity.app_url+"/documents.json", reqEntity);

            HashMap<String, String> user = session.getUserDetails();

            Log.e("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM)) ;
            Log.e("RESP:: ", "P: "+response);

            try {
                if (response.length() > 3) {
                    Log.e("Siri resp: ", "entry");
                    Log.e("Siri resp: ", response);
                    JSONObject jsonResponse = new JSONObject(response);



                    String key=jsonResponse.getString("key");

                    if(key.length()>1) {
                        completeUpload++;
                        documentsUploaded.add(jsonResponse);
                    }
                    //JSONObject medicineJsonObject = jsonResponse.getJSONObject("medicine");

                }
            } catch (Exception e) {
                Log.e("RESP:: ", "error"+e.getMessage());
                //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                errorUploading=true;
                e.printStackTrace();


            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            /**
             * update ui thread and remove dialog
             */

            imageUploadTimer.cancel();



            if(errorUploading)
            {
                new SweetAlertDialog(ConfirmOrderActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("There seems to be an issue with the servers. Please try again.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                ConfirmOrderActivity.this.finish();
                                startActivity(new Intent(ConfirmOrderActivity.this, OrderMedicineActivity.class));
//                                            System.exit(0);

                            }
                        })

                        .show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
            else
            {
                Log.e("RESP:: ", "on post execute");
                Toast.makeText(getApplicationContext(),completeUpload+"/ "+confirmBitmapList.size()+" images uploaded.", Toast.LENGTH_SHORT).show();
                //hidepDialog();
                loadingList.get(completeUpload-1).setVisibility(View.GONE);
                llList.get(completeUpload-1).setVisibility(View.GONE);
            }

            if(completeUpload==confirmBitmapList.size()) {
                //mDilatingDotsProgressBar.hideNow();
                Log.e("in inner class:: ", "order submission started");
                submitOrder();
            }
            else
               // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            //commentsCO.setText(completeUpload+" of "+confirmBitmapList.size()+" images uploaded.");

            super.onPostExecute(result);
        }
    }

    public void openBottomSheetAddress ( String heading,boolean delivery, ArrayList<String> addressType, ArrayList<String> addressValue) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_address, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);

        String [] addressTypeArray = addressType.toArray(new String[coupon.size()]);
        String [] addressValueArray = addressValue.toArray( new String[valid.size()]);



        ListView lv=(ListView) view.findViewById(R.id.listView);
        lv.setAdapter(new CustomAddressBSAdapter(view.getContext(), addressTypeArray, addressValueArray));

        TextView addAddress = (TextView) view.findViewById(R.id.add_address_button_bs);



        txth.setTypeface(tf_r);

        txth.setText(heading);

        mBottomSheetDialog = new Dialog (ConfirmOrderActivity.this,
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

                Intent toAddAddress = new Intent(ConfirmOrderActivity.this, AddAddressActivity.class);
                if(mBottomSheetDialog.isShowing())
                    mBottomSheetDialog.dismiss();
                startActivity(toAddAddress);
            }
        });


    }
    public void openBottomSheetCoupon ( String heading,ArrayList<String> coupon, ArrayList<String> valid, ArrayList<String> details) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");
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

        mBottomSheetDialog = new Dialog (ConfirmOrderActivity.this,
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

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_invoice_type_projectx, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);


        ListView lv=(ListView) view.findViewById(R.id.listView);
        //lv.setAdapter(new CustomInvoiceListAdapter(view.getContext(), language));

        txth.setTypeface(tf_r);

        txth.setText(heading);

         mBottomSheetDialog = new Dialog (ConfirmOrderActivity.this,
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
                mRequester = new RequestBuilder(ConfirmOrderActivity.this)
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



        }  catch (Exception e) {
            e.printStackTrace();

            Log.e("exception in add :", e.getMessage());

            try {
                JsonArrayRequester mRequester;
                mRequester = new RequestBuilder(ConfirmOrderActivity.this)
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



    public boolean checkCouponCodeValidity(String couponCode){

        ContentValues codecodepair = new ContentValues();
        codecodepair.put("code", couponCode);

        boolean couponValidBool=true;

        try {
            JSONObject couponJsonObject = getJsonObjectFromContentValues("coupon", codecodepair);
            HashMap<String, String> user = session.getUserDetails();

                JsonObjectRequester mRequester;
                mRequester = new RequestBuilder(ConfirmOrderActivity.this)
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

        String returnString;

        Log.e("in orderValid():: ","completeUpload= "+completeUpload);

        if(completeUpload!=confirmBitmapList.size())
        {
            if(mDilatingDotsProgressBar.getVisibility()==View.VISIBLE)
                mDilatingDotsProgressBar.hideNow();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            returnString="Prescription was not uploaded properly, Please Re-try";
            Log.e("in orderValid():: ","1 completeUpload= "+completeUpload);
            completeUpload=0;
            documentsUploaded.clear();
        }

        else
        {
            returnString = "ok";
            Log.e("in orderValid():: ","3 completeUpload= "+completeUpload);
            completeUpload=0;
        }


        return returnString;
    }

    public void submitOrder(){

        Log.e("in submitOrder():: ", "order submission started");

        String orderValid= orderValid();

        if(orderValid.equalsIgnoreCase("ok")){

            Log.e("in submitOrder():: ", "order valid ok");

            HashMap<String, String> user = session.getUserDetails();

            ContentValues codecodepair = new ContentValues();
            codecodepair.put("patient_name", yourOrderCO.getText().toString());
            codecodepair.put("notes", commentsCO.getText().toString());
            codecodepair.put("order_bucket", orderBucket);
            codecodepair.put("language", languageFinal);
            codecodepair.put("coupon",couponFinal);
            codecodepair.put("contact",user.get(SessionManager.KEY_MOBILE_UM));
            codecodepair.put("status","OCap");
            //codecodepair.put("name",MainActivity.nameFromGetUser);

            boolean couponValidBool=true;

            try {

                Log.e("in submitOrder():: ", "inside try");

                JSONObject orderJsonObject = getJsonObjectOrder("order", codecodepair, selectedAddressjobj, pickupSelectedjobj, documentsUploaded);
               // HashMap<String, String> user = session.getUserDetails();

                JsonObjectRequester mRequester;
                mRequester = new RequestBuilder(ConfirmOrderActivity.this)
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


                mRequester.request(Methods.POST, MainActivity.app_url+"/orders.json", orderJsonObject);




            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        else{

            Toast.makeText(ConfirmOrderActivity.this,orderValid,Toast.LENGTH_SHORT).show();


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
//
//                if(coupon.size()>0)
//                   // Toast.makeText(ConfirmOrderActivity.this, "User coupons received.", Toast.LENGTH_SHORT).show();
//                else
//                   // Toast.makeText(ConfirmOrderActivity.this, "No user coupons", Toast.LENGTH_SHORT).show();
//

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


        @Override
        public void onRequestStart(int requestCode) {
           if(mDilatingDotsProgressBar.getVisibility()!=View.VISIBLE);
            //mDilatingDotsProgressBar.showNow();
        }



        @Override
        public void onRequestFinish(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()==View.VISIBLE);
            //mDilatingDotsProgressBar.hideNow();


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

                        Log.e("ListenerToGetAddress", "before");
                        Log.e("deliveryjarray", deliveryjarray.toString());
                        Log.e("ListenerToGetAddress", "after");


                        //Toast.makeText(ConfirmOrderActivity.this, "User address received.", Toast.LENGTH_SHORT).show();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onRequestStart(int requestCode) {
//            if(mDilatingDotsProgressBar.getVisibility()!=View.VISIBLE)
//                mDilatingDotsProgressBar.showNow();
        }




        @Override
        public void onRequestFinish(int requestCode) {
//            if(mDilatingDotsProgressBar.getVisibility()==View.VISIBLE)
//                mDilatingDotsProgressBar.hideNow();


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


                    Toast.makeText(ConfirmOrderActivity.this, message, Toast.LENGTH_SHORT).show();
                    submitcouponTVCO.setText("Cancel");

                }
            } catch (Exception e) {
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

    }

    private class JsonObjectListenerToOrder extends Response.SimpleObjectResponse {

        String delivery_time, pickup_time, status, situation,reason;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {


                    Log.e("Siri resp: ", jsonObject.toString());

                    JSONObject obj = jsonObject;
                    delivery_time = obj.getString("delivery_time");
                    pickup_time = obj.getString("pickup_time");
                    status = obj.getString("status");

                    situation="success";
                    reason ="Order successful";

                    try {
                        if(situation.equalsIgnoreCase("success"))
                        {
                            Intent i=new Intent(ConfirmOrderActivity.this, ThankYouForOrderActivity.class);
                            i.putExtra("order_id", orderidFinal);
                            i.putExtra("delivery_slot",delivery_time);
                            i.putExtra("delivery_address",deliveryAddressFinal);
                            i.putExtra("coupon",couponFinal);
                            i.putExtra("coupon_details",couponDetailsFinal);
                            startActivity(i);
                        }
                        else
                            Toast.makeText(ConfirmOrderActivity.this, reason, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }
                else
                {
                   situation="fail";
                    reason= "Order can't be submitted due to network issues. Try Again";

                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onRequestStart(int requestCode) {

//            if(mDilatingDotsProgressBar.getVisibility()!=View.VISIBLE)
//                mDilatingDotsProgressBar.showNow();

        }



        @Override
        public void onRequestFinish(int requestCode) {
            if(mDilatingDotsProgressBar.getVisibility()==View.VISIBLE)
                mDilatingDotsProgressBar.hideNow();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



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

    private static JSONObject getNoTagJsonObjectFromContentValues(ContentValues params) throws JSONException {

        //Stores JSON
        JSONObject holder = new JSONObject();


        //object for storing Json
        JSONObject data = new JSONObject();


        for (Map.Entry<String, Object> entry : params.valueSet()) {
            String key = entry.getKey(); // name
            String value = entry.getValue().toString(); // value
            data.put(key, value);
        }



        return data;
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


    public void openBottomSheetImageViewer( ImageView b){

        Log.e("Confirm: ","in BS IV");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_image_viewer, null);

        ImageView iv = (ImageView) view.findViewById(R.id.bs_imageView);

        Bitmap bitmap = ((BitmapDrawable)b.getDrawable()).getBitmap();

        iv.setImageBitmap(bitmap);

        final Dialog mBottomSheetDialog = new Dialog (this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.CENTER);
        mBottomSheetDialog.show();


    }

    }
