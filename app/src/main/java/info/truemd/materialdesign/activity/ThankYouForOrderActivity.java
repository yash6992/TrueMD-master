package info.truemd.materialdesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import info.truemd.materialdesign.R;

/**
 * Created by yashvardhansrivastava on 20/04/16.
 */
public class ThankYouForOrderActivity extends AppCompatActivity {

    ImageView idealPres;
    ImageButton backImageButtonOM;
    TextView titleOM, t1, t2, t3, uploadPrescription,orderh,ordertv,dsloth,dslottv,deliveryh,deliverytv,gtotalh,gtotaltv,couponh,coupontv,totaltitleh, refillOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        Typeface tf_l=Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
        Typeface tf_r=Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Bold.ttf");
        Typeface tf_pacifico=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");

        Intent fromOrder = getIntent();



        idealPres=(ImageView)findViewById(R.id.idealPrescriptionImageViewOM);
        backImageButtonOM=(ImageButton) findViewById(R.id.backImageButtonOM);
        titleOM=(TextView) findViewById(R.id.titleOM);
        t1=(TextView) findViewById(R.id.textView4OM);
        t2=(TextView) findViewById(R.id.textView5OM);
        t3=(TextView) findViewById(R.id.textView6OM);
        orderh=(TextView) findViewById(R.id.order_id_h);orderh.setTypeface(tf_r);
        ordertv=(TextView) findViewById(R.id.order_id_tv);ordertv.setTypeface(tf_l);ordertv.setText(fromOrder.getStringExtra("order_id"));
        dsloth=(TextView) findViewById(R.id.delivery_slot_h);dsloth.setTypeface(tf_r);
        dslottv=(TextView) findViewById(R.id.delivery_slot_tv);dslottv.setTypeface(tf_l);
        String dater=fromOrder.getStringExtra("delivery_slot");
        HashMap<String, String> dateHash= getDateHash(dater);
        String date= dateHash.get("dd")+" "+dateHash.get("MM")+", "+dateHash.get("EEEE");
        dslottv.setText(date);
        gtotalh=(TextView) findViewById(R.id.total_id_h);gtotalh.setTypeface(tf_r);
        gtotaltv=(TextView) findViewById(R.id.total_id_tv);gtotaltv.setTypeface(tf_l);
        deliveryh=(TextView) findViewById(R.id.delivery_add_h);deliveryh.setTypeface(tf_r);
        deliverytv=(TextView) findViewById(R.id.delivery_add_tv);deliverytv.setTypeface(tf_l); deliverytv.setText(fromOrder.getStringExtra("delivery_address"));
        couponh=(TextView) findViewById(R.id.coupon_add_h);couponh.setTypeface(tf_r);
        coupontv=(TextView) findViewById(R.id.coupon_add_tv);coupontv.setTypeface(tf_l); coupontv.setText(fromOrder.getStringExtra("coupon")+"\n"+fromOrder.getStringExtra("coupon_details"));
        totaltitleh=(TextView) findViewById(R.id.total_id_title);totaltitleh.setTypeface(tf_r);
        uploadPrescription=(TextView) findViewById(R.id.uploadPrescriptionButtonOM);
        refillOrder=(TextView) findViewById(R.id.refillButtonOM);

        titleOM.setTypeface(tf_pacifico);
        t1.setTypeface(tf_l);t3.setTypeface(tf_l);t2.setTypeface(tf_l);uploadPrescription.setTypeface(tf_l);refillOrder.setTypeface(tf_l);

        uploadPrescription.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                MainActivity.fromThankYou = true;
                Intent intent = new Intent(ThankYouForOrderActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        refillOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Intent intent = new Intent(ThankYouForOrderActivity.this, SearchActivity.class);
                startActivity(intent);



            }
        });
        backImageButtonOM.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


              onBackPressed();


            }
        });



    }
    @Override
    public void onBackPressed() {
//            int fragments = getSupportFragmentManager().getBackStackEntryCount();
//            if (fragments == 1) {
//                finish();
//                return;
//            }
//
//            super.onBackPressed()
        finish();

        Intent intent_main = new Intent(getApplicationContext(),MainActivity.class);
        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplicationContext().startActivity(intent_main);



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



}
