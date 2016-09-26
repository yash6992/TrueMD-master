package info.truemd.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import info.truemd.android.R;
import info.truemd.android.adapter.CustomInvoiceAdapter;
import info.truemd.android.adapter.CustomPreviousOrderAdapter;
import info.truemd.android.helper.ExceptionHandler;
import info.truemd.android.helper.SessionManager;

/**
 * Created by yashvardhansrivastava on 27/04/16.
 */
public class UpcomingOrderActivity extends AppCompatActivity {


    TextView uoa_title_tv,uoa_total_amount_h,uoa_total_amount, uoa_status, uoa_status_h, uoa_no_of_items_h,uoa_no_of_items,uoa_please_note_h,uoa_please_note;
    TextView uoa_please_note_link,uoa_order_details_h,uoa_order_no_h,uoa_order_no,uoa_order_placed_h,uoa_order_placed,uoa_delivery_address_h,uoa_delivery_address;
    TextView uoa_queries_h,uoa_queries,uoa_payment_method_h,uoa_payment_method,uoa_amount_h,uoa_amount, uoa_delivery_time_h,uoa_completed_task,uoa_delivery_time, uoa_amount_details ;
    TextView uoa_p,uoa_ph,uoa_btn_pn,docslink;
    boolean is_cancellable;
    JSONObject jsonObjectPreviousOrder, jsonObjectInvoice;ImageButton uoa_backImageButton;
    JSONArray invoiceItems; ArrayList<JSONObject> aljoII;
    ListView lv; Context context_poa;
    boolean status=false;ArrayList<String> docsUrls;
    boolean instamojo_flag=false;
    String mode="";
    String pstatus = "N.A.";
    String shorturl=""; JSONArray documents;
    DilatingDotsProgressBar mDilatingDotsProgressBar; String dollaroid;
    ImageView iv0,iv1,iv2,iv3,iv4,iv5,iv6,iv7,iv8,iv9;
    AppCompatButton uoa_cancel_order;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_upcoming_order);
        context_poa=getApplicationContext();

        Intent i=getIntent();
        String jos = i.getStringExtra("aljo");
        jsonObjectPreviousOrder = new JSONObject();
        jsonObjectInvoice = new JSONObject();
        invoiceItems = new JSONArray();
        aljoII = new ArrayList<>();
        Typeface tf_l=Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface tf_b=Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Bold.ttf");

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.uoa_progress);

         docsUrls = new ArrayList<>();

        try {
            jsonObjectPreviousOrder = new JSONObject(jos);
            if(jsonObjectPreviousOrder.has("invoice")) {
                jsonObjectInvoice=jsonObjectPreviousOrder.getJSONObject("invoice");
                if (jsonObjectInvoice.has("items")){
                    invoiceItems = jsonObjectPreviousOrder.getJSONObject("invoice").getJSONArray("items");
                    for (int j=0; j<invoiceItems.length(); j++)
                    {
                        aljoII.add(invoiceItems.getJSONObject(j));
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }




        uoa_backImageButton = (ImageButton) findViewById(R.id.uoa_backImageButton);
        uoa_title_tv = (TextView) findViewById(R.id.uoa_title_tv);
        uoa_total_amount_h = (TextView) findViewById(R.id.uoa_total_amount_h);
        uoa_total_amount = (TextView) findViewById(R.id.uoa_total_amount);
        uoa_status_h = (TextView) findViewById(R.id.uoa_status_h);
        uoa_cancel_order = (AppCompatButton) findViewById(R.id.cancel_order);
        docslink = (TextView) findViewById(R.id.uoa_document);

        uoa_status = (TextView) findViewById(R.id.uoa_status);
        uoa_no_of_items_h = (TextView) findViewById(R.id.uoa_no_of_items_h);
        uoa_no_of_items = (TextView) findViewById(R.id.uoa_no_of_items);
        uoa_please_note_h = (TextView) findViewById(R.id.uoa_please_note_h);

        uoa_please_note = (TextView) findViewById(R.id.uoa_please_note);
        uoa_please_note_link = (TextView) findViewById(R.id.uoa_please_note_link);
        uoa_order_details_h = (TextView) findViewById(R.id.uoa_order_details_h);
        uoa_order_no_h = (TextView) findViewById(R.id.uoa_order_no_h);

        uoa_order_no = (TextView) findViewById(R.id.uoa_order_no);
        uoa_order_placed_h = (TextView) findViewById(R.id.uoa_order_placed_h);
        uoa_order_placed = (TextView) findViewById(R.id.uoa_order_placed);
        uoa_delivery_address_h = (TextView) findViewById(R.id.uoa_delivery_address_h);

        uoa_delivery_address = (TextView) findViewById(R.id.uoa_delivery_address);
        uoa_queries_h = (TextView) findViewById(R.id.uoa_queries_h);
        uoa_queries = (TextView) findViewById(R.id.uoa_queries);
        uoa_payment_method_h = (TextView) findViewById(R.id.uoa_payment_method_h);

        uoa_payment_method = (TextView) findViewById(R.id.uoa_payment_method);
        uoa_amount_h = (TextView) findViewById(R.id.uoa_amount_h);
        uoa_amount = (TextView) findViewById(R.id.uoa_amount);
        uoa_amount_details = (TextView) findViewById(R.id.uoa_amount_details);
        uoa_delivery_time = (TextView) findViewById(R.id.uoa_delivery_time);
        uoa_delivery_time_h = (TextView) findViewById(R.id.uoa_delivery_time_h);
        uoa_completed_task = (TextView) findViewById(R.id.uoa_completed_task);
        uoa_ph = (TextView) findViewById(R.id.uoa_payment_status_h);
        uoa_p = (TextView) findViewById(R.id.uoa_payment_status);
        uoa_btn_pn = (TextView) findViewById(R.id.uoa_btn_pay_now);

        uoa_title_tv.setTypeface(tf_l);uoa_total_amount_h.setTypeface(tf_l); uoa_cancel_order.setTypeface(tf_l);
        uoa_total_amount.setTypeface(tf_l); uoa_status.setTypeface(tf_l); uoa_status_h.setTypeface(tf_l);
        uoa_no_of_items_h.setTypeface(tf_l);uoa_no_of_items.setTypeface(tf_l);uoa_please_note_h.setTypeface(tf_b);
        uoa_please_note.setTypeface(tf_l);uoa_please_note_link.setTypeface(tf_l);uoa_order_details_h.setTypeface(tf_l);
        uoa_order_no_h.setTypeface(tf_l);uoa_order_no.setTypeface(tf_l);
        uoa_order_placed_h.setTypeface(tf_l);uoa_order_placed.setTypeface(tf_l);uoa_delivery_address_h.setTypeface(tf_l);uoa_delivery_address.setTypeface(tf_l);
        uoa_queries_h.setTypeface(tf_l);uoa_queries.setTypeface(tf_l);uoa_payment_method_h.setTypeface(tf_l);
        uoa_payment_method.setTypeface(tf_l);uoa_amount_h.setTypeface(tf_l);uoa_amount.setTypeface(tf_b);
        uoa_amount_details.setTypeface(tf_l);uoa_delivery_time_h.setTypeface(tf_l);
        uoa_completed_task.setTypeface(tf_l);uoa_delivery_time.setTypeface(tf_l);
        uoa_ph.setTypeface(tf_l);
        uoa_p.setTypeface(tf_l);uoa_btn_pn.setTypeface(tf_l);docslink.setTypeface(tf_l);



        lv=(ListView) findViewById(R.id.uoa_listView);

        lv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        uoa_backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setListViewHeightBasedOnChildren(lv);

        try {
            documents=jsonObjectPreviousOrder.getJSONArray("documents");



            for (int loop =0; loop < documents.length() ; loop ++){

                JSONObject d=documents.getJSONObject(loop);
                String publicUrl = d.optString("public_url");
                docsUrls.add(publicUrl);
            }


            JSONObject ujo = jsonObjectPreviousOrder;


            if (ujo.has("payment")){
                JSONObject payment = ujo.getJSONObject("payment");
                if (payment.has("status")){
                    status = payment.getBoolean("status");
                }
                if (payment.has("mode")){
                    mode = payment.getString("mode");
                }
                if (payment.has("instamojo_flag")){
                    instamojo_flag = payment.getBoolean("instamojo_flag");
                }
                if (payment.has("instamojo")){
                    JSONObject instamojo=payment.getJSONObject("instamojo");
                    if (instamojo.has("payment_request")){
                        JSONObject payment_request=instamojo.getJSONObject("payment_request");
                        if (payment_request.has("shorturl")){
                            shorturl = payment_request.getString("shorturl");
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!status){ //payment not done; either invoice not generated or payment pending
            if(instamojo_flag){
                uoa_btn_pn.setVisibility(View.VISIBLE);
                pstatus=""+"\u20B9 "+String.format("%.2f",Double.parseDouble(jsonObjectInvoice.optString("net_total")));
            }
            else{
                uoa_btn_pn.setVisibility(View.GONE);
                pstatus="You'll be notified after order digitization.";
            }
        }
        else{ //payment done
            uoa_btn_pn.setVisibility(View.GONE);
            pstatus="Paid through CARD";
        }

        uoa_p.setText(pstatus);

        uoa_btn_pn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!shorturl.startsWith("http://") && !shorturl.startsWith("https://"))
                    shorturl= "http://" + shorturl;

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shorturl));
                finish();
                startActivity(browserIntent);

            }
        });

        try {
            JSONObject _id= jsonObjectPreviousOrder.getJSONObject("_id");
            dollaroid=_id.optString("$oid");
        } catch (JSONException e) {
            e.printStackTrace();

        }

        is_cancellable = jsonObjectPreviousOrder.optBoolean("is_cancellable",true);
        Log.e("OrderCancelLabel:",""+is_cancellable);

        if(!is_cancellable) {
            uoa_cancel_order.setBackgroundColor(getResources().getColor(R.color.aluminum));

        }
        else{

        }

        uoa_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(is_cancellable) {
                    cancelOrder();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "This order cannot be cancelled", Toast.LENGTH_SHORT).show();
                }

            }
        });

        String amount_details= null;
        String total_amount= null;
        String amount=null;
        String msg=null;
        try {
            msg= jsonObjectPreviousOrder.getJSONObject("truemd_comment").optString("msg");
            amount=""+"\u20B9 "+String.format("%.2f",Double.parseDouble(jsonObjectInvoice.optString("net_total")));
            amount_details = "(Gross Total: "+String.format("%.2f", jsonObjectInvoice.optDouble("gross_total"))+
                    " - "+"Discount: "+String.format("%.2f",jsonObjectInvoice.optDouble("discounts"))+")";
            total_amount = ""+"\u20B9 "+String.format("%.2f",Double.parseDouble(jsonObjectInvoice.optString("net_total")));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("uoa_exception_handled: ",e.getMessage());
            amount_details = "N.A.";
            total_amount = "N.A.";
            amount="N.A.";
            msg="The order is being processed.";
        }
        try {
            uoa_title_tv.setText("ORDER DETAILS");
            uoa_total_amount_h.setText("AMOUNT");
            uoa_total_amount.setText(total_amount);
            uoa_status.setText(""+jsonObjectPreviousOrder.optString("status_msg"));
            uoa_status_h.setText("STATUS");
            uoa_no_of_items_h.setText("ITEMS");
            uoa_no_of_items.setText(""+invoiceItems.length());
            uoa_please_note_h.setText("Please Note: ");
            //uoa_please_note.setText(""+jsonObjectPreviousOrder.optJSONObject("truemd_comment"));
            uoa_please_note.setText(""+msg);
            uoa_please_note_link.setText("(?)");
            uoa_order_details_h.setText("ORDER DETAILS");
            uoa_order_no_h.setText("Order No: ");
            String orderNo = jsonObjectPreviousOrder.optString("order_bucket");
            uoa_order_no.setText(""+orderNo.substring(orderNo.indexOf('#')+1));
            uoa_order_placed_h.setText("Order placed: ");
            String dater=jsonObjectPreviousOrder.optString("created_at");
            HashMap<String, String> dateHash= getDateHash(dater);
            String date= dateHash.get("dd")+" "+dateHash.get("MM")+", "+dateHash.get("EEEE");
            uoa_order_placed.setText(""+date);
            uoa_delivery_address_h.setText("Delivery Address: ");
            JSONObject add = jsonObjectPreviousOrder.getJSONObject("delivery_address");
            uoa_delivery_address.setText(""+
                    add.optString("type")+"\n"+
                    add.optString("name")+"\n"+
                    add.optString("line1")+"\n"+
                    add.optString("line2")+"\n"+
                    add.optString("landmark")+"\n"+
                    add.optString("city")+"- "+add.optString("pincode")+"."

            );
            uoa_queries_h.setText("For any queries, Call ");
            uoa_queries.setText("74000 74005");
            uoa_payment_method_h.setText("Payment method: ");
            uoa_payment_method.setText(""+mode.toUpperCase());
            uoa_amount_h.setText("Total Amount: ");
            uoa_amount.setText(amount);
            uoa_amount_details.setText(amount_details);
            //uoa_delivery_time.setText(""+jsonObjectInvoice.optString("bill_date")+", "+jsonObjectInvoice.optString("bill_time"));
            uoa_delivery_time_h.setText("Currently: ");
            uoa_delivery_time.setText(""+jsonObjectPreviousOrder.optString("status_tag"));
            //uoa_completed_task.setText(" has been completed.");
        } catch (Exception e) {
            Log.e("uoa_exception",e.getMessage());
            e.printStackTrace();
        }

        if(aljoII.size()==0)
        {

        }
        else
        {
            lv.setAdapter(new CustomInvoiceAdapter(context_poa,UpcomingOrderActivity.this,aljoII ));

        }

        docslink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetDocs(docsUrls);
            }
        });

    }
    public HashMap<String, String> getDateHash(String dateString){

        HashMap<String, String> dateHash = new HashMap<>();

        dateHash.put("yyyy", dateString.substring(0,4));

        String m =  dateString.substring(5,7);

        String monthString="";

        switch (m) {
            case "01":  monthString = "January";       break;
            case "02":  monthString = "February";      break;
            case "03":  monthString = "March";         break;
            case "04":  monthString = "April";         break;
            case "05":  monthString = "May";           break;
            case "06":  monthString = "June";          break;
            case "07":  monthString = "July";          break;
            case "08":  monthString = "August";        break;
            case "09":  monthString = "September";     break;
            case "10": monthString = "October";       break;
            case "11": monthString = "November";      break;
            case "12": monthString = "December";      break;
            default: monthString = "Invalid month"; break;
        }


        dateHash.put("MM", monthString.substring(0,3));
        dateHash.put("dd", dateString.substring(8,10));

        try {
            String input_date=dateString.substring(0,10);
            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
            Date dt1=format1.parse(input_date);
            DateFormat format2=new SimpleDateFormat("EEEE");
            String finalDay=format2.format(dt1);
            dateHash.put("EEEE",finalDay.substring(0,3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateHash;

    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    void cancelOrder() {

        Log.e("in submitOrder():: ", "order valid ok");

        SessionManager session = new SessionManager(UpcomingOrderActivity.this);

        HashMap<String, String> user = session.getUserDetails();

        ContentValues codecodepair = new ContentValues();

        codecodepair.put("status", "OCan");
        //codecodepair.put("name",MainActivity.nameFromGetUser);

        boolean couponValidBool = true;


        Log.e("in submitOrder():: ", "inside try");

        try {
            JSONObject orderJsonObject = getJsonObjectFromContentValues("order", codecodepair);
            // HashMap<String, String> user = session.getUserDetails();

            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(UpcomingOrderActivity.this)
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
                    .buildObjectRequester(new JsonObjectListenerToOrder()); //or .buildArrayRequester(listener);

            Log.e("in submitOrder():: ", orderJsonObject.toString());


            mRequester.request(Methods.PUT, MainActivity.app_url+"/orders/"+dollaroid+".json", orderJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private class JsonObjectListenerToOrder extends Response.SimpleObjectResponse {

        String delivery_time, pickup_time, status, situation,reason;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {

                    Intent cancelled = new Intent(UpcomingOrderActivity.this, OrderDetailsActivity.class);

                    startActivity(cancelled);
                }
                else
                {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


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

    public void openBottomSheetDocs (ArrayList<String> docUrls) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_prescription_docs, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);
        TextView txtc = (TextView)view.findViewById( R.id.txt_medname);





        iv0 = (ImageView) view.findViewById(R.id.pdbsImage0);
        iv7 = (ImageView) view.findViewById(R.id.pdbsImage7);
        iv1 = (ImageView) view.findViewById(R.id.pdbsImage1);
        iv2 = (ImageView) view.findViewById(R.id.pdbsImage2);
        iv3 = (ImageView) view.findViewById(R.id.pdbsImage3);
        iv4 = (ImageView) view.findViewById(R.id.pdbsImage4);
        iv5 = (ImageView) view.findViewById(R.id.pdbsImage5);
        iv6 = (ImageView) view.findViewById(R.id.pdbsImage6);
        iv8 = (ImageView) view.findViewById(R.id.pdbsImage8);
        iv9 = (ImageView) view.findViewById(R.id.pdbsImage9);

        ArrayList<ImageView> confirmImageList = new ArrayList<ImageView>();
        confirmImageList.add(iv0);
        confirmImageList.add(iv1);
        confirmImageList.add(iv2);
        confirmImageList.add(iv3);
        confirmImageList.add(iv4);
        confirmImageList.add(iv5);
        confirmImageList.add(iv6);
        confirmImageList.add(iv7);
        confirmImageList.add(iv8);
        confirmImageList.add(iv9);

        int i=0;
        for (i=0;i<docUrls.size();i++)
        {
            Glide
                    .with(getApplicationContext())
                    .load(docUrls.get(i))
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_loading_bs)
                    .into(confirmImageList.get(i));
        }
        for (;i<10;i++)
        {
            confirmImageList.get(i).setVisibility(View.GONE);
        }



        txth.setTypeface(tf_b);
        txtc.setTypeface(tf_r);



        txth.setText("Prescription Images");
        txtc.setText("");




        final Dialog mBottomSheetDialog = new Dialog (this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


        iv0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv0);
            }
        });
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv1);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv2);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv3);
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv4);
            }
        });
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv5);
            }
        });
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv6);
            }
        });
        iv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv7);
            }
        });
        iv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv8);
            }
        });
        iv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetImageViewer(0,iv9);
            }
        });


    }
    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();

    }
    public void openBottomSheetImageViewer(int position, ImageView b){

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
