package com.truemdhq.projectx.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.adapter.CustomInvoiceItemListAdapter;
import com.truemdhq.projectx.model.Invoice;
import com.truemdhq.projectx.model.InvoiceParent;
import com.truemdhq.projectx.model.Item;
import com.truemdhq.projectx.views.TextViewFont1Medium;
import com.truemdhq.projectx.views.TextViewFont2Large;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yashvardhansrivastava on 11/11/16.
 */
public class InvoiceViewActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @OnClick(R.id.btn_back)

    public void onClick1(ImageView imageView){
        onBackPressed();
    }

    @BindView(R.id.vendor_name)    TextView vendorName;
    @BindView(R.id.vendor_address)    TextView vendorAddress;
    @BindView(R.id.invoice_date_)    TextView invoiceDate;
    @BindView(R.id.invoice_due_in_)    TextView invoiceDueIn;
    @BindView(R.id.invoice_no_)    TextView invoiceNo;
    @BindView(R.id.vendor_contact) TextView vendorContact;

    @BindView(R.id.subtotal)
    TextViewFont1Medium subtotal;
    @BindView(R.id.vat)    TextViewFont1Medium vat;
    @BindView(R.id.tax1)    TextViewFont1Medium tax1;
    @BindView(R.id.tax2)    TextViewFont1Medium tax2;
    @BindView(R.id.shipping)    TextViewFont1Medium shipping;
    @BindView(R.id.total)    TextViewFont1Medium total;
    @BindView(R.id.previously_paid)    TextViewFont1Medium previouslyPaid;
    @BindView(R.id.balance_due)
    TextViewFont2Large balanceDue;

    @BindView(R.id.vendor_logo)    ImageView vendorLogo;

    @BindView(R.id.item_list)
    ListView itemList;

    Invoice invoiceDisplay;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invoice_view_projectx);
        ButterKnife.bind(this);

       int p = getIntent().getIntExtra("invoice",0);
        List<Invoice> invoices = null;
        try {
            String sJSONObjectHavingJSONArray = "{\"invoices\":"+Hawk.get("invoiceList")+"}";
            JSONObject jsonObjectHavingJSONArray = new JSONObject(sJSONObjectHavingJSONArray);

            JSONArray invoiceJSONArray = jsonObjectHavingJSONArray.getJSONArray("invoices");
            Log.e("InvoiceList: ",""+invoiceJSONArray.toString());
            invoices = new ArrayList<>();
            for(int i =0; i<invoiceJSONArray.length();i++){
                ObjectMapper objectMapper = new ObjectMapper();
                Invoice invoice = objectMapper.readValue(invoiceJSONArray.get(i).toString(), Invoice.class);
                Log.e("InvoiceList: "+i,": "+invoice.getInvoiceNo());
                invoices.add(invoice);
            }
        } catch (Exception e) {
            Log.e("Hawk:",""+e.getMessage());

            e.printStackTrace();
        }

        Log.e("InvoiceList: ",""+invoices.size());


        invoiceDisplay= invoices.get(p);

       vendorName.setText(invoiceDisplay.getClient().getVendorName());
         vendorAddress.setText(invoiceDisplay.getClient().getVendorBillingAddress());
       invoiceDate.setText(invoiceDisplay.getInvoiceDate());
            invoiceDueIn.setText(invoiceDisplay.getInvoiceDueDate());
         invoiceNo.setText(invoiceDisplay.getInvoiceNo());
         vendorContact.setText(invoiceDisplay.getClient().getVendorPhoneNo()+" | "+invoiceDisplay.getClient().getVendorEmail());

        subtotal.setText(invoiceDisplay.getSubtotal(),2);
         vat.setText(invoiceDisplay.getVat(),2);
        tax1.setText(invoiceDisplay.getTax1(),2);
        tax2.setText(invoiceDisplay.getTax2(),2);
       shipping.setText(invoiceDisplay.getShipping(),2);
        total.setText(invoiceDisplay.getTotal(),2);
        previouslyPaid.setText(invoiceDisplay.getPaid(),2);
        balanceDue.setText(invoiceDisplay.getBalanceDue(),2);


        itemList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setListViewHeightBasedOnChildren(itemList);


        List<Item> listOfItems = invoiceDisplay.getItems();

        Log.e("InvoiceItemList: ","size: "+listOfItems.size());

        itemList.setAdapter(new CustomInvoiceItemListAdapter(InvoiceViewActivity.this,listOfItems ));




    }

    @Override
    public void onBackPressed(){
        finish();
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

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

    }
