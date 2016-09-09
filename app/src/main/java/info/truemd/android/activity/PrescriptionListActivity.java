package info.truemd.android.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonArrayRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import cn.pedant.SweetAlert.SweetAlertDialog;
import info.truemd.android.R;
import info.truemd.android.adapter.CustomPrescriptionAdapter;
import info.truemd.android.adapter.CustomPreviousOrderAdapter;
import info.truemd.android.helper.SessionManager;
import info.truemd.android.helper.SimpleGestureFilter;
import info.truemd.android.helper.SimpleGestureFilter.SimpleGestureListener;
import info.truemd.android.model.Medicine;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 19/01/16.
 */
public class PrescriptionListActivity extends AppCompatActivity {

    ImageButton backImage; TextView pl_title, descrip;
    ListView lv; Button placeholdertv;
    DilatingDotsProgressBar mDilatingDotsProgressBar;
    Context context_pla;
    JsonArrayRequester mRequester;

    ImageView placeholderi;LinearLayout placeholder;
    SessionManager session;
    ArrayList<String> dateList, doctorList, nameList, medsummaryList, presNoList, descripList; ArrayList<JSONObject> oidList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);

        Paper.book("nav").write("selected","1");
        backImage = (ImageButton) findViewById(R.id.pl_backImageButton);

        context_pla=PrescriptionListActivity.this;
        dateList= new ArrayList<>();
        nameList= new ArrayList<>();
        medsummaryList= new ArrayList<>();
        doctorList= new ArrayList<>();
        presNoList= new ArrayList<>();
        descripList= new ArrayList<>();
        oidList= new ArrayList<>();


        session = new SessionManager(context_pla);

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.pl_progress);
        placeholder = (LinearLayout) findViewById(R.id.pl_placeholder);
        placeholderi=(ImageView) findViewById(R.id.pl_imageviewp);
        placeholdertv=(Button) findViewById(R.id.pl_placeholderTextView);

        placeholder.setVisibility(View.GONE);

        getPrescriptionList();

        lv=(ListView) findViewById(R.id.pl_listView);


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        placeholdertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrescriptionListActivity.this,OrderMedicineActivity.class));
            }
        });
       

        pl_title= (TextView) findViewById(R.id.pl_title_tv);
        descrip= (TextView) findViewById(R.id.pl_descrip);

        Typeface tf_l= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        pl_title.setTypeface(tf_l); descrip.setTypeface(tf_l); placeholdertv.setTypeface(tf_l);

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

        finish();

        Intent intent_main = new Intent(getApplicationContext(),MainActivity.class);
        intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplicationContext().startActivity(intent_main);


        super.onBackPressed();

    }


    public void getPrescriptionList(){
        String result = new String ();
        String line="";

        HashMap<String, String> user = session.getUserDetails();

        try {

            mRequester = new RequestBuilder(context_pla)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    .timeOut(50000)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildArrayRequester(new JsonArrayListenerToGetPreviousOrder()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, MainActivity.app_url+"/prescriptions.json");

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class JsonArrayListenerToGetPreviousOrder extends Response.SimpleArrayResponse {

        String [] dateArray, medsummaryArray, nameArray, doctornameArray, descripArray, presNoArray; JSONObject [] oidArray;

        @Override
        public void onResponse(int requestCode, @Nullable JSONArray jsonArray) {
            //Ok
            try {
                if (jsonArray.toString().length() > 5) {

                    Log.e("PL resp: ", jsonArray.toString());

                    for(int i=0; i< jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String dater=obj.getString("created_at");
                        HashMap<String, String> dateHash= getDateHash(dater);
                        String date= dateHash.get("dd")+" "+dateHash.get("MM")+", "+dateHash.get("EEEE");

                        Log.e("PL resp: ", "after date ini");
                        String medsummary = "";

                        String name=obj.getString("patient_name");

                        if(obj.has("meds")) {
                            JSONArray medsJSA = obj.optJSONArray("meds");

                            int meds = medsJSA.length();
                                    medsummary = ""+medsJSA.getJSONObject(0).optString("med_name");//obj.getString("");
                           if(meds>2)
                            medsummary=medsummary +" + "+(meds-1)+" more meds";
                            else if(meds==2)
                               medsummary=medsummary +" + "+(meds-1)+" more med";

                        }
                        else {
                            medsummary = "";//obj.getString("");
                        }

                        String doctorname=obj.getString("doctor_name");
                        String descrip="";
                        String presNo=obj.getString("prescription_bucket");
                        presNo = presNo.substring(presNo.indexOf('#')+5);



//                        JSONObject id = obj.getJSONObject("_id");
//                        String oid = id.getString("$oid");


                        Log.e("PL resp: ", "after static key ini");

                        dateList.add(date);
                        medsummaryList.add(medsummary);
                        doctorList.add(doctorname);
                        nameList.add(name);
                        presNoList.add(presNo);
                        descripList.add(descrip);
                        oidList.add(obj);

                        Log.e("PL resp: ", "after arraylist dec and ini");




                    }


                    dateArray = dateList.toArray(new String[dateList.size()]);
                    medsummaryArray = medsummaryList.toArray( new String[medsummaryList.size()]);
                    doctornameArray = doctorList.toArray(new String[doctorList.size()]);
                    nameArray = nameList.toArray( new String[nameList.size()]);
                    descripArray = descripList.toArray(new String[descripList.size()]);
                    presNoArray = presNoList.toArray( new String[presNoList.size()]);
                    oidArray = oidList.toArray( new JSONObject[oidList.size()]);


                    Log.e("PL resp: ", "before");
                    //Log.e("prevOrderjarray", deliveryjarray.toString());
                    if(dateArray.length==0)
                    {
                        Log.e("PL resp: ", "in if for length=0");
                       // placeholdertv.setText("You don't have any digitized prescriptions.");
                        placeholderi.setImageResource(R.drawable.emptyprescription);
                        placeholder.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        //placeholder.setVisibility(View.INVISIBLE);
                        Log.e("PL resp: ", "in else for length=0");
                        lv.setAdapter(new CustomPrescriptionAdapter(context_pla,dateArray,presNoArray,nameArray, medsummaryArray, doctornameArray, descripArray, oidArray ));
                    }
                    Log.e("PL resp: ", "after");


                    //Toast.makeText(context_pla, "Pres received.", Toast.LENGTH_SHORT).show();


                }
                else {
                   // placeholdertv.setText("You don't have any digitized prescriptions.");
                    placeholderi.setImageResource(R.drawable.emptyprescription);
                    placeholder.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {

                Log.e("PL resp: ", e.getMessage());
               // placeholdertv.setText("There is some network issue, Could you please try again.");
                placeholderi.setImageResource(R.drawable.networkissue);
                placeholdertv.setVisibility(View.INVISIBLE);
                placeholder.setVisibility(View.VISIBLE);


                e.printStackTrace();
            }
        }
        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, String errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());
            //placeholdertv.setText("There is some network issue, Could you please try again.");
            placeholderi.setImageResource(R.drawable.networkissue);
            placeholdertv.setVisibility(View.INVISIBLE);
            placeholder.setVisibility(View.VISIBLE);
            cancelBlockUser();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            //reauthenticate(MainActivity.this);
        }





        @Override
        public void onRequestStart(int requestCode) {

            Log.e("POrder resp: ", "onRequestStarted()");
            setBlockUser();
        }




        @Override
        public void onRequestFinish(int requestCode) {
            Log.e("POrder resp: ", "onRequestFinish()");
            cancelBlockUser();

        }
        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            Log.e ("onFinishResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());


            if(message.equalsIgnoreCase("Timeout error")||message.equalsIgnoreCase("No Connection")||message.equalsIgnoreCase("Check your connection")||message.equalsIgnoreCase("Server error"))
            {
                new SweetAlertDialog(PrescriptionListActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops..!!")
                        .setContentText("There might be an issue with your internet connection.\n Try after some time.")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                PrescriptionListActivity.this.finish();
                                System.exit(0);

                            }
                        })

                        .show();
            }
            else{
                //placeholdertv.setText("You don't have any digitized prescriptions. Order from home screen to get your prescriptions digitised. ");
                placeholderi.setImageResource(R.drawable.emptyprescription);
                Log.e("onFinishResponse: ","made visible");
                placeholder.setVisibility(View.VISIBLE);
            }


        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            mRequester.setCallback(null);
        }
    }
    public void setBlockUser()
    {
        mDilatingDotsProgressBar.showNow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


    }
    public void cancelBlockUser ()
    {
        mDilatingDotsProgressBar.hideNow();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


    }





}
