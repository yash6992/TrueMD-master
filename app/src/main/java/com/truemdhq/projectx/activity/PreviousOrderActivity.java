package com.truemdhq.projectx.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.adapter.CustomInvoiceAdapter;
import com.truemdhq.projectx.helper.ExceptionHandler;
import com.truemdhq.projectx.helper.ProjectXJSONUtils;

/**
 * Created by yashvardhansrivastava on 27/04/16.
 */
public class PreviousOrderActivity extends AppCompatActivity {


    TextView docslink,poa_title_tv,poa_total_amount_h,poa_total_amount, poa_status, poa_status_h, poa_no_of_items_h,poa_no_of_items,poa_please_note_h,poa_please_note;
    TextView poa_please_note_link,poa_order_details_h,poa_order_no_h,poa_order_no,poa_order_placed_h,poa_order_placed,poa_delivery_address_h,poa_delivery_address;
    TextView poa_queries_h,poa_queries,poa_payment_method_h,poa_payment_method,poa_amount_h,poa_amount, poa_delivery_time_h,poa_delivery_time, poa_amount_details ;
    JSONObject jsonObjectPreviousOrder, jsonObjectInvoice;ImageButton poa_backImageButton;
    JSONArray invoiceItems; ArrayList<JSONObject> aljoII;Dialog mBottomSheetDialog1;
    ListView lv; Context context_poa; DilatingDotsProgressBar mDilatingDotsProgressBar;
    String mode,shorturl=""; ArrayList<String> docsUrls;
    boolean status,instamojo_flag=false; JSONArray documents;
    ImageView iv0,iv1,iv2,iv3,iv4,iv5,iv6,iv7,iv8,iv9;
    Button poa_refill_order;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_previous_order);
        context_poa=getApplicationContext();

        Intent i=getIntent();
        String jos = i.getStringExtra("aljo");
        jsonObjectPreviousOrder = new JSONObject();
        jsonObjectInvoice = new JSONObject();
        invoiceItems = new JSONArray();
        aljoII = new ArrayList<>();
        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.poa_progress);
        Typeface tf_l=Typeface.createFromAsset(getAssets(),"MarkOffcPro-Medium.ttf");
        Typeface tf_b=Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Bold.ttf");

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


        poa_backImageButton = (ImageButton) findViewById(R.id.poa_backImageButton);
        poa_title_tv = (TextView) findViewById(R.id.poa_title_tv);
        poa_total_amount_h = (TextView) findViewById(R.id.poa_total_amount_h);
        poa_total_amount = (TextView) findViewById(R.id.poa_total_amount);
        poa_status_h = (TextView) findViewById(R.id.poa_status_h);
        poa_refill_order = (Button) findViewById(R.id.poa_refill_order);
        docslink = (TextView) findViewById(R.id.poa_document);


        poa_status = (TextView) findViewById(R.id.poa_status);
        poa_no_of_items_h = (TextView) findViewById(R.id.poa_no_of_items_h);
        poa_no_of_items = (TextView) findViewById(R.id.poa_no_of_items);
        poa_please_note_h = (TextView) findViewById(R.id.poa_please_note_h);

        poa_please_note = (TextView) findViewById(R.id.poa_please_note);
        poa_please_note_link = (TextView) findViewById(R.id.poa_please_note_link);
        poa_order_details_h = (TextView) findViewById(R.id.poa_order_details_h);
        poa_order_no_h = (TextView) findViewById(R.id.poa_order_no_h);

        poa_order_no = (TextView) findViewById(R.id.poa_order_no);
        poa_order_placed_h = (TextView) findViewById(R.id.poa_order_placed_h);
        poa_order_placed = (TextView) findViewById(R.id.poa_order_placed);
        poa_delivery_address_h = (TextView) findViewById(R.id.poa_delivery_address_h);

        poa_delivery_address = (TextView) findViewById(R.id.poa_delivery_address);
        poa_queries_h = (TextView) findViewById(R.id.poa_queries_h);
        poa_queries = (TextView) findViewById(R.id.poa_queries);
        poa_payment_method_h = (TextView) findViewById(R.id.poa_payment_method_h);

        poa_payment_method = (TextView) findViewById(R.id.poa_payment_method);
        poa_amount_h = (TextView) findViewById(R.id.poa_amount_h);
        poa_amount = (TextView) findViewById(R.id.poa_amount);
        poa_amount_details = (TextView) findViewById(R.id.poa_amount_details);
        poa_delivery_time = (TextView) findViewById(R.id.poa_delivery_time);
        poa_delivery_time_h = (TextView) findViewById(R.id.poa_delivery_time_h);

        poa_title_tv.setTypeface(tf_l);poa_total_amount_h.setTypeface(tf_l);poa_refill_order.setTypeface(tf_l);
        poa_total_amount.setTypeface(tf_l); poa_status.setTypeface(tf_l); poa_status_h.setTypeface(tf_l);
        poa_no_of_items_h.setTypeface(tf_l);poa_no_of_items.setTypeface(tf_l);poa_please_note_h.setTypeface(tf_b);
        poa_please_note.setTypeface(tf_l);poa_please_note_link.setTypeface(tf_l);poa_order_details_h.setTypeface(tf_l);
        poa_order_no_h.setTypeface(tf_l);poa_order_no.setTypeface(tf_l);
        poa_order_placed_h.setTypeface(tf_l);poa_order_placed.setTypeface(tf_l);poa_delivery_address_h.setTypeface(tf_l);poa_delivery_address.setTypeface(tf_l);
        poa_queries_h.setTypeface(tf_l);poa_queries.setTypeface(tf_l);poa_payment_method_h.setTypeface(tf_l);
        poa_payment_method.setTypeface(tf_l);poa_amount_h.setTypeface(tf_l);poa_amount.setTypeface(tf_b);
        poa_amount_details.setTypeface(tf_l);poa_delivery_time_h.setTypeface(tf_l);poa_delivery_time.setTypeface(tf_l);
        docslink.setTypeface(tf_l);
        lv=(ListView) findViewById(R.id.poa_listView);

        lv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        poa_backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(jsonObjectPreviousOrder.optString("status").equalsIgnoreCase("ODel"))
        {


        }
        else{
            poa_refill_order.setVisibility(View.GONE);
        }

        poa_refill_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openBottomSheetForPreRefill();

            }
        });
        setListViewHeightBasedOnChildren(lv);


        try {
            JSONObject ujo = jsonObjectPreviousOrder;

            documents=jsonObjectPreviousOrder.getJSONArray("documents");



            for (int loop =0; loop < documents.length() ; loop ++){

                JSONObject d=documents.getJSONObject(loop);
                String publicUrl = d.optString("public_url");
                docsUrls.add(publicUrl);
            }



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
            msg="The order is complete.";
        }


        try {
            poa_title_tv.setText("ORDER DETAILS");
            poa_total_amount_h.setText("AMOUNT");
           // poa_total_amount.setText(""+"\u20B9 "+String.format("%.2f",Double.parseDouble(jsonObjectInvoice.optString("net_total"))));
            poa_total_amount.setText(total_amount);
            poa_status.setText(""+jsonObjectPreviousOrder.optString("status_msg"));
            poa_status_h.setText("STATUS");
            poa_no_of_items_h.setText("ITEMS");
            poa_no_of_items.setText(""+invoiceItems.length());
            poa_please_note_h.setText("Please Note: ");
            poa_please_note.setText(msg);
            poa_please_note_link.setText("(?)");
            poa_order_details_h.setText("ORDER DETAILS");
            poa_order_no_h.setText("Order No: ");
            String orderNo = jsonObjectPreviousOrder.optString("order_bucket");
            poa_order_no.setText(""+orderNo.substring(orderNo.indexOf('#')+1));
            poa_order_placed_h.setText("Order placed: ");
            String dater=jsonObjectPreviousOrder.optString("created_at");
            HashMap<String, String> dateHash= getDateHash(dater);
            String date= dateHash.get("dd")+" "+dateHash.get("MM")+", "+dateHash.get("EEEE");
            poa_order_placed.setText(""+date);
            poa_delivery_address_h.setText("Delivery Address: ");
            JSONObject add = jsonObjectPreviousOrder.getJSONObject("delivery_address");
            poa_delivery_address.setText(""+
                    add.optString("type")+"\n"+
                    add.optString("name")+"\n"+
                    add.optString("line1")+"\n"+
                    add.optString("line2")+"\n"+
                    add.optString("landmark")+"\n"+
                    add.optString("city")+"- "+add.optString("pincode")+"."

            );
            poa_queries_h.setText("For any queries, Call ");
            poa_queries.setText("74000 74005");
            poa_payment_method_h.setText("Payment method: ");
            poa_payment_method.setText(""+ mode.toUpperCase());
            poa_amount_h.setText("Total Amount: ");
            //poa_amount.setText(""+"\u20B9 "+String.format("%.2f",Double.parseDouble(jsonObjectInvoice.optString("net_total"))));
            poa_amount.setText(amount);
            //poa_amount_details.setText("(Gross Total: "+String.format("%.2f", jsonObjectInvoice.optDouble("gross_total"))+" - "+"Discount: "+String.format("%.2f",jsonObjectInvoice.optDouble("discounts"))+")");
            poa_amount_details.setText(amount_details);
            poa_delivery_time.setText(""+jsonObjectInvoice.optString("bill_date")+", "+jsonObjectInvoice.optString("bill_time"));
            poa_delivery_time_h.setText("Delivered At: ");
        } catch (Exception e) {
            Log.e("POA_exception",e.getMessage());
            e.printStackTrace();
        }

        if(aljoII.size()==0)
        {

        }
        else
        {
            lv.setAdapter(new CustomInvoiceAdapter(context_poa,PreviousOrderActivity.this,aljoII ));

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
    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();

    }
    public void openBottomSheetForPreRefill( ) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pre_refill, null);

        TextView txth = (TextView)view.findViewById( R.id.tv2);
        TextView txt = (TextView)view.findViewById( R.id.refill_txt);
        //phone_number = (EditText)view.findViewById( R.id.input_phone_number_password);
        Button submit = (Button) view.findViewById( R.id.btn_next);

        txth.setTypeface(tf_r);txt.setTypeface(tf_r);
        //phone_number.setTypeface(tf_r);
        submit.setTypeface(tf_r);

        //txth.setText(heading);



        mBottomSheetDialog1= new Dialog(PreviousOrderActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog1.setContentView (view);
        mBottomSheetDialog1.setCancelable(true);
        mBottomSheetDialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog1.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog1.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toRefill = new Intent(PreviousOrderActivity.this, RefillOrderActivity.class);
                toRefill.putExtra("refillJOS", jsonObjectPreviousOrder.toString());
                mBottomSheetDialog1.dismiss();
                startActivity(toRefill);
            }
        });

    }

    public void openBottomSheetDocs (ArrayList<String> docUrls) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "MarkOffcPro-Medium.ttf");
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
