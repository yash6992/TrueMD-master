package com.truemdhq.projectx.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


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
import com.truemdhq.projectx.adapter.CustomPrescriptionMedAdapter;
import com.truemdhq.projectx.helper.ExceptionHandler;
import com.truemdhq.projectx.helper.TrueMDJSONUtils;

/**
 * Created by yashvardhansrivastava on 19/01/16.
 */
public class PrescriptionDetailsActivity extends AppCompatActivity {

    ImageButton backImage; TextView pd_title, tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    ListView lv_1; LinearLayout userImage, doctorImage;
    Context context_pda;

    JSONObject presObj;  JSONArray jsonArray;
    ArrayList<JSONObject> medObjList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_prescription_details);

        Intent fromPL = getIntent();
        String presObjString = fromPL.getStringExtra("pl_obj");
        try {
            presObj = new JSONObject(presObjString);

           jsonArray =new JSONArray();



            backImage = (ImageButton) findViewById(R.id.pd_backImageButton);
            userImage = (LinearLayout) findViewById(R.id.pd_patient_image_h);
            doctorImage = (LinearLayout) findViewById(R.id.pd_doctor_image_h);

        context_pda=PrescriptionDetailsActivity.this;

            lv_1=(ListView) findViewById(R.id.pd_listView);

        medObjList= new ArrayList<>();

            pd_title= (TextView) findViewById(R.id.pd_title_tv);
            tv1 = (TextView) findViewById(R.id.pd_date);
            tv2 = (TextView) findViewById(R.id.pd_pres_no_h);
            tv3 = (TextView) findViewById(R.id.pd_pres_no);
            tv4 = (TextView) findViewById(R.id.pd_username);
            tv5 = (TextView) findViewById(R.id.pd_doctorname);
            tv6 = (TextView) findViewById(R.id.pd_document);
            tv7 = (TextView) findViewById(R.id.pd_comments);

            Typeface tf_r = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
            Typeface tf_b = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

            pd_title.setTypeface(tf_r);
            tv1.setTypeface(tf_b);
            tv2.setTypeface(tf_r);
            tv3.setTypeface(tf_r);
            tv4.setTypeface(tf_r);
            tv5.setTypeface(tf_r);
            tv6.setTypeface(tf_r);
            tv7.setTypeface(tf_r);








        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        String dater=presObj.getString("created_at");
        HashMap<String, String> dateHash= getDateHash(dater);
        String date= dateHash.get("dd")+" "+dateHash.get("MM")+", "+dateHash.get("EEEE");

        Log.e("PL resp: ", "after date ini");


        String name=presObj.getString("patient_name");

        //String medsummary="COMBIFLAM 500 MG + 4 more meds";//obj.getString("");

        String doctorname=presObj.getString("doctor_name");

        String presNo=presObj.getString("prescription_bucket");
        presNo = presNo.substring(presNo.indexOf('#')+5);




            tv1.setText(date);
            //tv2.setText(presNo);
            tv3.setText(TrueMDJSONUtils.goThroughNullCheck(presNo));
            tv4.setText(TrueMDJSONUtils.goThroughNullCheck(name));
            tv5.setText(TrueMDJSONUtils.goThroughNullCheck(doctorname));
           // tv6.setText();
            tv7.setText("Comments from the doctor (if any)");

        Log.e("PL resp: ", "after static key ini");


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    openBottomSheetPatientInfo(tv4.getText().toString(), "", presObj.getString("patient_age"), presObj.getString("patient_gender"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        doctorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openBottomSheetDoctorInfo(tv5.getText().toString(),presObj.getString("doctor_qualification"), presObj.getString("doctor_address"),presObj.getString("doctor_contact"),presObj.getString("doctor_registration") );
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheetDocs("Prescription Docs");


            }
        });






            jsonArray = presObj.getJSONArray("meds");

            if (jsonArray.toString().length() > 5) {



                Log.e("PL resp: ", jsonArray.toString());

                for(int i=0; i< jsonArray.length();i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);


                    Log.e("PL resp: ", "after arraylist dec and ini");

                    medObjList.add(obj);
                }


                JSONObject[] medObjArray = medObjList.toArray( new JSONObject[medObjList.size()]);

                    lv_1= (ListView) findViewById(R.id.pd_listView);
                    lv_1.setAdapter(new CustomPrescriptionMedAdapter(context_pda, medObjArray ));


                Log.e("PL resp: ", ""+medObjArray.length);



            }
        } catch (Exception e) {

            Log.e("PL resp: ", e.getMessage());

            e.printStackTrace();
        }

        new MainActivity().checkInternet(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_search) {
            Intent intent_main_search = new Intent(this, SearchActivity.class);
            startActivityForResult(intent_main_search, 0);
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {

//        finish();
//
//        Intent intent_main = new Intent(getApplicationContext(),Pre.class);
//        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        getApplicationContext().startActivity(intent_main);


        super.onBackPressed();

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

    public void openBottomSheetPatientInfo ( String heading, String content, String age, String gender) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_patient_info, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);
        TextView txtc = (TextView)view.findViewById( R.id.txt_medname);
        TextView txtah = (TextView)view.findViewById( R.id.age_h);
        TextView txta = (TextView)view.findViewById( R.id.age);
        TextView txtgh = (TextView)view.findViewById( R.id.gender_h);
        TextView txtg = (TextView)view.findViewById( R.id.gender);


        txth.setTypeface(tf_b);
        txtc.setTypeface(tf_r);
        txtah.setTypeface(tf_r);
        txta.setTypeface(tf_r);
        txtgh.setTypeface(tf_r);
        txtg.setTypeface(tf_r);

        txtg.setText(TrueMDJSONUtils.goThroughNullCheck(gender));
        txth.setText(TrueMDJSONUtils.goThroughNullCheck(heading));
        txtc.setText("");
        txta.setText(TrueMDJSONUtils.goThroughNullCheck(age));



        final Dialog mBottomSheetDialog = new Dialog (this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }
    public void openBottomSheetDoctorInfo ( String heading, String qual, String address, String contact, String registration) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_doctor_info, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);
        TextView txtq = (TextView)view.findViewById( R.id.txt_medname);
        TextView txtah = (TextView)view.findViewById( R.id.d_address_h);
        TextView txta = (TextView)view.findViewById( R.id.d_address);
        TextView txtch = (TextView)view.findViewById( R.id.contact_h);
        TextView txtc = (TextView)view.findViewById( R.id.contact);
        TextView txtrh = (TextView)view.findViewById( R.id.registration_h);
        TextView txtr = (TextView)view.findViewById( R.id.registration);


        txth.setTypeface(tf_b);
        txtq.setTypeface(tf_r);
        txtah.setTypeface(tf_r);
        txta.setTypeface(tf_r);
        txtch.setTypeface(tf_r);
        txtc.setTypeface(tf_r);
        txtrh.setTypeface(tf_r);
        txtr.setTypeface(tf_r);

        txtr.setText(TrueMDJSONUtils.goThroughNullCheck(registration));
        txth.setText(TrueMDJSONUtils.goThroughNullCheck(heading));
        txtq.setText(TrueMDJSONUtils.goThroughNullCheck(qual));
        txta.setText(TrueMDJSONUtils.goThroughNullCheck(address));
        txtc.setText(TrueMDJSONUtils.goThroughNullCheck(contact));



        final Dialog mBottomSheetDialog = new Dialog (this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }

    public void openBottomSheetDocs ( String heading) {

        Typeface tf_r= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        Typeface tf_b= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_prescription_docs, null);
        TextView txth = (TextView)view.findViewById( R.id.tv2);
        TextView txtc = (TextView)view.findViewById( R.id.txt_medname);



        txth.setTypeface(tf_b);
        txtc.setTypeface(tf_r);



        txth.setText(heading);
        txtc.setText("");




        final Dialog mBottomSheetDialog = new Dialog (this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


    }
}
