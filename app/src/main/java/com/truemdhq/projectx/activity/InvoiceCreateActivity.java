package com.truemdhq.projectx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.model.Invoice;
import com.truemdhq.projectx.model.InvoiceParent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yashvardhansrivastava on 11/11/16.
 */
public class InvoiceCreateActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @OnClick(R.id.btn_back)
    public void onClick1(ImageView imageView){
        onBackPressed();
    }

    @BindView(R.id.add_client)
    RelativeLayout addClient;
    @OnClick(R.id.add_client)
    public void onClick3(RelativeLayout imageView){
        onAddItemPressed();
    }

    @BindView(R.id.client_choose)
    RelativeLayout clientChoose;
    @OnClick(R.id.client_choose)
    public void onClick4(RelativeLayout imageView){
        onClientChoosePressed();
    }


    @BindView(R.id.btn_bottom)
    RelativeLayout btnBottom;
    @OnClick(R.id.btn_bottom)

    public void onClick2(RelativeLayout relativeLayout){
        Intent nextActivity = new Intent(InvoiceCreateActivity.this, InvoiceViewActivity.class);


        nextActivity.putExtra("invoice", 0);

        startActivity(nextActivity);
        //push from bottom to top
        //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        //slide from right to left
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_invoice_projectx);
        ButterKnife.bind(this);

    }
    @Override
    public void onBackPressed(){
        finish();
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    public void onAddItemPressed(){
        Intent nextActivity = new Intent(InvoiceCreateActivity.this, AddItemChoiceActivity.class);
        startActivity(nextActivity);
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    public void onClientChoosePressed(){
        Intent nextActivity = new Intent(InvoiceCreateActivity.this, ChooseClientActivity.class);
        startActivity(nextActivity);
        //push from top to bottom
        //overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

}
