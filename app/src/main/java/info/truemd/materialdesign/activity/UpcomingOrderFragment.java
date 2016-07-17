package info.truemd.materialdesign.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import info.truemd.materialdesign.R;
import info.truemd.materialdesign.adapter.CustomAddressBSAdapter;
import info.truemd.materialdesign.adapter.CustomPreviousOrderAdapter;
import info.truemd.materialdesign.adapter.CustomSearchAdapter;
import info.truemd.materialdesign.adapter.CustomSubstitutesAdapter;
import info.truemd.materialdesign.adapter.CustomUpcomingOrderAdapter;
import info.truemd.materialdesign.helper.SessionManager;
import info.truemd.materialdesign.model.JsonParse;
import info.truemd.materialdesign.model.ResponseChat;
import info.truemd.materialdesign.model.SuggestGetSet;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 25/03/16.
 */
public class UpcomingOrderFragment extends Fragment {

    View rootView; int rece;
    ListView lv; TextView placeholdertv;
    DilatingDotsProgressBar mDilatingDotsProgressBar;
    Context context_uof;
    ArrayList<JSONObject> aljoUOrders;
    JsonArrayRequester mRequester; ImageView placeholderi;
    SessionManager session; LinearLayout placeholder;
    ArrayList<String> dateList, statusList, dotsList, nameList, gTotalList, orderNoList, deliveryTimeList;


    public UpcomingOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_upcoming_order, container, false);

        context_uof=getActivity();
        
        dateList= new ArrayList<>();
        nameList= new ArrayList<>();
        dotsList= new ArrayList<>();
        statusList= new ArrayList<>();
        gTotalList= new ArrayList<>();
        orderNoList= new ArrayList<>();
        deliveryTimeList= new ArrayList<>();
        aljoUOrders = new ArrayList<>();



        session = new SessionManager(context_uof);

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) rootView.findViewById(R.id.uo_progress);
        placeholder = (LinearLayout) rootView.findViewById(R.id.uo_placeholder);
        placeholdertv=(TextView) rootView.findViewById(R.id.uo_placeholderTextView);

        Typeface tf_l=Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Regular.ttf");
        placeholderi=(ImageView) rootView.findViewById(R.id.uo_imageviewp);
        placeholder.setVisibility(View.INVISIBLE); placeholdertv.setTypeface(tf_l);

        placeholdertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),OrderMedicineActivity.class));
            }
        });

        getUpcomingOrder();

        lv=(ListView) rootView.findViewById(R.id.uo_listView);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }



    public void getUpcomingOrder(){
        String result = new String ();
        String line="";

        HashMap<String, String> user = session.getUserDetails();

        try {

            mRequester = new RequestBuilder(context_uof)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(true) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(50000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildArrayRequester(new JsonArrayListenerToGetUpcomingOrder()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, "http://truemd-carebook.rhcloud.com/orders.json?status_not=ODel,OCan,ORej,ORef");

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class JsonArrayListenerToGetUpcomingOrder extends Response.SimpleArrayResponse {

        String [] dateArray, gTotalArray, statusArray, dotsArray, nameArray, orderNoArray, deliveryTimeArray;

        @Override
        public void onResponse(int requestCode, @Nullable JSONArray jsonArray) {
            //Ok
            try {
                if (jsonArray.toString().length() > 5) {



                    Log.e("UOrder resp: ", jsonArray.toString());

                    for(int i=0; i< jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        aljoUOrders.add(obj);

                        String dater=obj.getString("created_at");
                        HashMap<String, String> dateHash= getDateHash(dater);
                        String date= dateHash.get("dd")+" "+dateHash.get("MM")+", "+dateHash.get("EEEE");

                        //Log.e("UOrder resp: ", i+"after date ini");


                        String name=obj.getString("patient_name");
                        String status=obj.getString("status");

                        String status_msg=obj.optString("status_msg");

                       // Log.e("UOrder resp: ", i+"after status ini");

                        String dots = statusToDots(status);
                        //Log.e("UOrder resp: ", i+"after dots ini");


                        String gtotal=" N.A.";

                        if(obj.has("invoice"))
                        {
                            gtotal=obj.getJSONObject("invoice").getString("net_total");
                            gtotal="\u20B9 "+""+String.format("%.2f",Double.parseDouble(gtotal));
                        }
                        else
                        {}
                        Log.e("UOrder resp: ", i+"after invoice ini");

                        String orderNo=obj.getString("order_bucket");
                        orderNo = orderNo.substring(orderNo.indexOf('#')+1);

                        Log.e("UOrder resp: ", i+"after static key ini");


                        String deliveryTimer = obj.getString("delivery_time");
                        HashMap<String, String> deliveryTimeHash= getDateHash(deliveryTimer);
                        String deliveryTime= deliveryTimeHash.get("dd")+" "+deliveryTimeHash.get("MM")+", "+deliveryTimeHash.get("hh:mm");

                        Log.e("UOrder resp: ", i+"after deliverytimer key ini");

                        dateList.add(date);
                        statusList.add(status_msg);
                        dotsList.add(dots);
                        nameList.add(name);
                        orderNoList.add(orderNo);
                        gTotalList.add(gtotal);
                        deliveryTimeList.add(deliveryTime);

                        Log.e("UOrder resp: ", i+"after arraylist dec and ini");

                        dateArray = dateList.toArray(new String[dateList.size()]);
                        statusArray = statusList.toArray( new String[statusList.size()]);
                        dotsArray = dotsList.toArray(new String[dotsList.size()]);
                        nameArray = nameList.toArray( new String[nameList.size()]);
                        orderNoArray = orderNoList.toArray(new String[orderNoList.size()]);
                        gTotalArray = gTotalList.toArray( new String[gTotalList.size()]);
                        deliveryTimeArray = deliveryTimeList.toArray(new String[deliveryTimeList.size()]);



                    }

                    Log.e("UOrder resp: ", "before");
                    //Log.e("prevOrderjarray", deliveryjarray.toString());
                    if(dateArray.length==0)
                    {
                        Log.e("UOrder resp: ", "in if for length=0");
                       // placeholdertv.setText("You don't have any upcoming orders.");
                        placeholderi.setImageResource(R.drawable.emptyupcomingorder1);
                        placeholder.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Log.e("UOrder resp: ", "in else for length=0");
                        lv.setAdapter(new CustomUpcomingOrderAdapter(context_uof,dateArray,orderNoArray,nameArray, statusArray, dotsArray, gTotalArray, deliveryTimeArray,aljoUOrders  ));
                    }
                    Log.e("UOrder resp: ", "after");


                    Toast.makeText(context_uof, "Upcoming Orders received.", Toast.LENGTH_SHORT).show();


                }
                else {
                   // placeholdertv.setText("You don't have any upcoming orders.");
                    placeholderi.setImageResource(R.drawable.emptyupcomingorder1);
                    placeholder.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {

                Log.e("UOrder err: ", e.getMessage());
                //placeholdertv.setText("There is some network issue, Could you please try again.");
                placeholderi.setImageResource(R.drawable.networkissue);
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
            placeholder.setVisibility(View.VISIBLE);

            //reauthenticate(MainActivity.this);
        }



        @Override
        public void onRequestStart(int requestCode) {

            Log.e("UOrder resp: ", "onRequestStarted()");
           setBlockUser();
        }




        @Override
        public void onRequestFinish(int requestCode) {
            Log.e("UOrder resp: ", "onRequestFinish()");
            cancelBlockUser();


        }
        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());
           // placeholdertv.setText("You don't have any upcoming orders. Press back and place an order from the home screen.");
            placeholderi.setImageResource(R.drawable.emptyupcomingorder1);
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

        dateHash.put("hh:mm",dateString.substring(11,16));

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

    public String statusToDots(String status){
        String dots="1";

        if(status.equalsIgnoreCase("ODis")||status.equalsIgnoreCase("ODel"))
            dots=""+4;
        else if(status.equalsIgnoreCase("OPac")||status.equalsIgnoreCase("QAs1")||status.equalsIgnoreCase("QAs2"))
            dots=""+3;
        else if(status.equalsIgnoreCase("ODig")||status.equalsIgnoreCase("OVer")
                ||status.equalsIgnoreCase("OAva")||status.equalsIgnoreCase("OPro")||status.equalsIgnoreCase("OPre"))
            dots=""+2;
        else if(status.equalsIgnoreCase("OCap")||status.equalsIgnoreCase("OVal"))
            dots=""+1;


        return dots;
    }

}
