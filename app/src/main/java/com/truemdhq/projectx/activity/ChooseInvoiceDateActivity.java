package com.truemdhq.projectx.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orhanobut.hawk.Hawk;
import com.truemdhq.projectx.R;
import com.truemdhq.projectx.model.Client;
import com.truemdhq.projectx.model.Invoice;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yashvardhansrivastava on 15/11/16.
 */
public class ChooseInvoiceDateActivity extends AppCompatActivity {


    private int mYear, mMonth, mDay;
    String sinvoiceDate, sinvoiceDueIn;

    @BindView(R.id.invoice_date)TextView invoiceDate;
    @BindView(R.id.invoice_date_h)TextView invoiceDateHeader;
    @OnClick(R.id.invoice_date_h)
    public void onClick3(TextView imageView){
       chooseDatePicker();
    }

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @OnClick(R.id.btn_back)
    public void onClick1(ImageView imageView){
        onBackPressed();
    }
    @BindView(R.id.btn_bottom)
    RelativeLayout btnBottom;
    @OnClick(R.id.btn_bottom)


    public void onClick2(RelativeLayout relativeLayout) {

        confirm();

    }
    @BindView(R.id.invoice_duedate) EditText duein;


    Invoice invoiceDraft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_invoice_date_projectx);
        ButterKnife.bind(this);
            Typeface tf_l= Typeface.createFromAsset(getAssets(), "MarkOffcPro.ttf");
            duein.setTypeface(tf_l);
            duein.setTextSize(20.0f);

        try{

            ObjectMapper objectMapper = new ObjectMapper();
             invoiceDraft = objectMapper.readValue(Hawk.get("invoiceDraft").toString(), Invoice.class);


        }
        catch (Exception e){
            Log.e("DateError: ",""+e.getMessage());
        }

            chooseDatePicker();
    }

    @Override
    public void onBackPressed(){
        Intent newIntent = new Intent(ChooseInvoiceDateActivity.this, InvoiceCreateActivity.class);
        startActivity(newIntent);
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    public void confirm(){

        sinvoiceDate = invoiceDate.getText().toString();
        sinvoiceDueIn = duein.getText().toString()+" days";

        invoiceDraft.setInvoiceDate(sinvoiceDate);
        invoiceDraft.setInvoiceDueDate(sinvoiceDueIn);

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInvoiceDraft = objectMapper.writeValueAsString(invoiceDraft);

            Hawk.put("invoiceDraft", jsonInvoiceDraft);


        }
        catch (Exception e){
            Log.e("DateError: ",""+e.getMessage());
        }

        Intent newIntent = new Intent(ChooseInvoiceDateActivity.this, InvoiceCreateActivity.class);

        startActivityForResult(newIntent, 3);
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();

    }

    void chooseDatePicker(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        invoiceDate.setText(dayOfMonth + "/ " + (monthOfYear + 1) + "/ " + year);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.show();
    }

}
