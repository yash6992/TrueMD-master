package com.truemdhq.projectx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.adapter.CustomAddressBSAdapter;
import com.truemdhq.projectx.adapter.CustomPreviousOrderAdapter;
import com.truemdhq.projectx.adapter.CustomSearchAdapter;
import com.truemdhq.projectx.adapter.CustomSubstitutesAdapter;
import com.truemdhq.projectx.helper.SessionManager;
import com.truemdhq.projectx.model.JsonParse;
import com.truemdhq.projectx.model.ResponseChat;
import com.truemdhq.projectx.model.SuggestGetSet;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 25/03/16.
 */
public class PreviousOrderFragment extends Fragment {

    View rootView; int rece;
    ListView lv; Button placeholdertv;
    public static DilatingDotsProgressBar mDilatingDotsProgressBar;
    Context context_pof; ImageView placeholderi;
    JsonArrayRequester mRequester;
    ArrayList<JSONObject> aljoPreviousOrders;
    SessionManager session; LinearLayout placeholder;
    ArrayList<String> dateList, med1List, med2List, nameList, gTotalList, orderNoList;


    public PreviousOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_previous_order, container, false);

        context_pof=getActivity();
        dateList= new ArrayList<>();
        nameList= new ArrayList<>();
        med1List= new ArrayList<>();
        med2List= new ArrayList<>();
        gTotalList= new ArrayList<>();
        orderNoList= new ArrayList<>();
        aljoPreviousOrders = new ArrayList<>();



        session = new SessionManager(context_pof);

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) rootView.findViewById(R.id.po_progress);
        placeholdertv=(Button) rootView.findViewById(R.id.po_placeholderTextView);
        placeholder=(LinearLayout) rootView.findViewById(R.id.po_placeholder);
        placeholderi=(ImageView) rootView.findViewById(R.id.po_imageviewp);
         Typeface tf_l=Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");

        placeholder.setVisibility(View.INVISIBLE); placeholdertv.setTypeface(tf_l);

        getPreviousOrder();
        placeholdertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),OrderMedicineActivity.class));
            }
        });

        lv=(ListView) rootView.findViewById(R.id.po_listView);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



    public void getPreviousOrder(){
        String result = new String ();
        String line="";

        HashMap<String, String> user = session.getUserDetails();

        try {

            mRequester = new RequestBuilder(context_pof)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(50000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                            //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildArrayRequester(new JsonArrayListenerToGetPreviousOrder()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, MainActivity.app_url+"/orders.json?status=ODel,OCan,ORef,ORej");

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class JsonArrayListenerToGetPreviousOrder extends Response.SimpleArrayResponse {

        String [] dateArray, gTotalArray, med1Array, med2Array, nameArray, orderNoArray;

        @Override
        public void onResponse(int requestCode, @Nullable JSONArray jsonArray) {
            //Ok
            try {
                if (jsonArray.toString().length() > 5) {



                    Log.e("POrder resp: ", jsonArray.toString());

                    for(int i=0; i< jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        aljoPreviousOrders.add(obj);

                        String dater=obj.getString("created_at");
                        HashMap<String, String> dateHash= getDateHash(dater);
                       String date= dateHash.get("dd")+" "+dateHash.get("MM")+", "+dateHash.get("EEEE");

                        Log.e("POrder resp: ", i+" : "+obj.toString());


                        String name=obj.getString("patient_name");

                        String med1 = "";//obj.getString("");
                        String med2 = "";//obj.getString("");

                        try {
                            if(obj.has("meds")) {
                                if(obj.getJSONArray("meds").toString().length()>5) {
                                    med1 = obj.getJSONArray("meds").optJSONObject(0).optString("med_name");
                                    // med2 = obj.getJSONArray("meds").optJSONObject(1).optString("med_name");
                                }
                            }
                        } catch (JSONException e) {
                            med1="";
                            e.printStackTrace();
                        }

                        Log.e("POrder resp: ", "after med ini");
                        String gtotal=" N.A.";

                        if(obj.has("invoice"))
                        {
                            gtotal=obj.getJSONObject("invoice").getString("net_total");
                            gtotal="\u20B9 "+""+String.format("%.2f",Double.parseDouble(gtotal));
                        }
                        else
                        {}

                        String orderNo=obj.getString("order_bucket");
                        orderNo = orderNo.substring(orderNo.indexOf('#')+1);

                        Log.e("POrder resp: ", "after static key ini");

                        dateList.add(date);
                        med1List.add(med1);
                        med2List.add(med2);
                        nameList.add(name);
                        orderNoList.add(orderNo);
                        gTotalList.add(gtotal);

                        Log.e("POrder resp: ", "after arraylist dec and ini");

                        dateArray = dateList.toArray(new String[dateList.size()]);
                        med1Array = med1List.toArray( new String[med1List.size()]);
                         med2Array = med2List.toArray(new String[med2List.size()]);
                        nameArray = nameList.toArray( new String[nameList.size()]);
                       orderNoArray = orderNoList.toArray(new String[orderNoList.size()]);
                        gTotalArray = gTotalList.toArray( new String[gTotalList.size()]);



                    }

                    Log.e("POrder resp: ", "before");
                    //Log.e("prevOrderjarray", deliveryjarray.toString());
                    if(dateArray.length==0)
                    {
                        Log.e("POrder resp: ", "in if for length=0");
                       // placeholdertv.setText("You don't have any completed orders.");
                        placeholderi.setImageResource(R.drawable.emptypreviousorder1);
                        placeholder.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Log.e("POrder resp: ", "in else for length=0");
                        lv.setAdapter(new CustomPreviousOrderAdapter(getActivity(),dateArray,orderNoArray,nameArray, med1Array, med2Array, gTotalArray, aljoPreviousOrders ));
                    }
                    Log.e("POrder resp: ", "after");


                    //Toast.makeText(context_pof, "Previous Orders received.", Toast.LENGTH_SHORT).show();


                }
                else {
                  //  placeholdertv.setText("You don't have any completed orders.");
                    placeholderi.setImageResource(R.drawable.emptypreviousorder1);
                    placeholder.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {

                Log.e("POrder resp: ", e.getMessage());
               // placeholdertv.setText("There is some network issue, Could you please try again.");
                placeholderi.setImageResource(R.drawable.networkissue);placeholdertv.setVisibility(View.INVISIBLE);
                placeholder.setVisibility(View.VISIBLE);

                e.printStackTrace();
            }
        }
        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, String errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());
           // placeholdertv.setText("There is some network issue, Could you please try again.");
            placeholderi.setImageResource(R.drawable.networkissue);placeholdertv.setVisibility(View.INVISIBLE);
            placeholder.setVisibility(View.VISIBLE);

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
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());
            //placeholdertv.setText("You don't have any completed orders.");
            placeholderi.setImageResource(R.drawable.emptypreviousorder1);
            placeholder.setVisibility(View.VISIBLE);



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
    public void onDetach() {
        super.onDetach();
        if (isRemoving() || getActivity().isFinishing()) {
            mRequester.setCallback(null);
        }
    }
    public void setBlockUser()
    {
        mDilatingDotsProgressBar.showNow();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


    }
    public void cancelBlockUser ()
    {
        mDilatingDotsProgressBar.hideNow();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


    }


}
