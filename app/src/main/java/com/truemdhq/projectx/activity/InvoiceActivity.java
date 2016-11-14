package com.truemdhq.projectx.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.adapter.CustomInvoiceListAdapter;
import com.truemdhq.projectx.model.Client;
import com.truemdhq.projectx.model.Invoice;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 16/04/16.
 */
public class InvoiceActivity extends AppCompatActivity {


    Dialog mBottomSheetDialog1;



    @BindView(R.id.add_invoices) RelativeLayout addInvoices;
    @BindView(R.id.list)
    ListView mInvoiceListView;
    @BindView(R.id.btn_back) ImageView btnBack;
    @BindView(R.id.refresh_layout)
    CircleRefreshLayout crl;
    @OnClick(R.id.btn_back)
    public void onClick1(ImageView imageView){
        onBackPressed();
    }
    @OnClick(R.id.add_invoices)
    public void onClick2(RelativeLayout relativeLayout){

        openBottomSheetForChoosingInvoiceType();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invoice_projectx);
        ButterKnife.bind(this);

        Log.e("Hawk:",""+Hawk.get("invoiceList"));

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


        mInvoiceListView.setAdapter(new CustomInvoiceListAdapter(InvoiceActivity.this,  invoices));


        crl.setOnRefreshListener(
                new CircleRefreshLayout.OnCircleRefreshListener() {
                    @Override
                    public void refreshing() {
                        // do something when refresh starts
                        loopRefresh();
                    }

                    @Override
                    public void completeRefresh() {
                        // do something when refresh complete
                    }
                });


    }

    public void loopRefresh(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                crl.finishRefreshing();
            }
        }, 1);




    }



    @Override
    public void onBackPressed(){
        finish();
        //push from top to bottom
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    public void openBottomSheetForChoosingInvoiceType( ) {

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_invoice_type_projectx, null);

       RelativeLayout invoice, quote, creditMemo;

        invoice = (RelativeLayout) view.findViewById(R.id.choice_invoice);
        quote = (RelativeLayout) view.findViewById(R.id.choice_quote);
        creditMemo = (RelativeLayout) view.findViewById(R.id.choice_credit_memo);

        RelativeLayout header = (RelativeLayout) view.findViewById(R.id.bs_header_main);



        mBottomSheetDialog1= new Dialog(InvoiceActivity.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog1.setContentView (view);
        mBottomSheetDialog1.setCancelable(true);
        mBottomSheetDialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog1.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog1.show();

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(InvoiceActivity.this, InvoiceCreateActivity.class);
                startActivity(nextActivity);
                //push from bottom to top
                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                //slide from right to left
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(InvoiceActivity.this, InvoiceCreateActivity.class);
                startActivity(nextActivity);
                //push from bottom to top
                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                //slide from right to left
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        creditMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(InvoiceActivity.this, InvoiceCreateActivity.class);
                startActivity(nextActivity);
                //push from bottom to top
                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                //slide from right to left
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog1.dismiss();
            }
        });

    }


}
