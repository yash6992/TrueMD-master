package info.truemd.android.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonArrayRequester;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import info.truemd.android.R;
import info.truemd.android.adapter.CustomCardReminderAdapter;
import info.truemd.android.adapter.CustomUpcomingOrderAdapter;
import info.truemd.android.helper.SessionManager;
import info.truemd.android.receiver.AlarmReceiver;
import io.paperdb.Paper;
import link.fls.swipestack.SwipeStack;

/**
 * Created by yashvardhansrivastava on 04/05/16.
 */
public class ReminderAlarmActivity extends AppCompatActivity implements SwipeStack.SwipeStackListener, View.OnClickListener{

    private TextView mButtonLeft, r_title, noReminders;

    private ArrayList<String> mData;
    private ImageButton backImage; RelativeLayout rl_reminder;
    private SwipeStack mSwipeStack; SessionManager session;
    private CustomCardReminderAdapter mAdapter;
    JSONObject pouchesJO;
    public JSONArray pouchesAllOrders, pouchesToShow;
    DilatingDotsProgressBar mDilatingDotsProgressBar;
    SessionManager sessionManager; ImageView placeholder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Typeface tf_l= Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        rl_reminder= (RelativeLayout) findViewById(R.id.rl_reminder);
        session=new SessionManager(getApplicationContext());
        mSwipeStack = (SwipeStack) findViewById(R.id.swipeStack);
        mButtonLeft = (TextView) findViewById(R.id.buttonSwipeLeft);
        r_title = (TextView) findViewById((R.id.r_title_tv));
        backImage = (ImageButton) findViewById(R.id.r_backImageButton);
        noReminders = (TextView) findViewById(R.id.no_reminders);
        noReminders.setVisibility(View.GONE);

        mDilatingDotsProgressBar=(DilatingDotsProgressBar) findViewById(R.id.progress_4);
        mButtonLeft.setOnClickListener(this);
        pouchesAllOrders = new JSONArray();
        pouchesToShow = new JSONArray();
        pouchesJO=new JSONObject();

        placeholder = (ImageView) findViewById(R.id.ra_placeholderi);
        String ps=pouchesJO.toString();
        sessionManager= new SessionManager(ReminderAlarmActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        placeholder.setImageResource(R.drawable.emptyreminders);
        placeholder.setVisibility(View.VISIBLE);


//        try {
//            JSONArray orders = new JSONArray();
//            JSONObject patient = getUserFromMain.getJSONObject("patient");
//
//            //get all pouches
//            if(patient.has("orders"))
//            {
//                 orders = patient.getJSONArray("orders");
//                for(int i=0;i<orders.length();i++)
//                {
//                    JSONArray pouches = new JSONArray();
//                    JSONObject order = orders.getJSONObject(i);
//
//                    if(order.has("pouches"))
//                    {
//                        Log.e("Reminder: ","order"+i);
//                        pouches = order.getJSONArray("pouches");
//                        for(int j=0;j<pouches.length();j++)
//                        {
//                            Log.e("Reminder: ","pouch"+j);
//                            JSONObject pouch = pouches.getJSONObject(j);
//                            pouchesAllOrders.put(pouch);
//                        }
//                    }
//                    else {
//                        continue;
//                    }
//
//                }
//            }
//
//            //arrange pouches acc. to datetime
//             JSONArray sortedJsonArray = new JSONArray();
//
//            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
//            for (int i = 0; i < pouchesAllOrders.length(); i++) {
//                jsonValues.add(pouchesAllOrders.getJSONObject(i));
//            }
//            Collections.sort( jsonValues, new Comparator<JSONObject>() {
//                //You can change "Name" with "ID" if you want to sort by ID
//                private static final String KEY_NAME = "datetime";
//
//                @Override
//                public int compare(JSONObject a, JSONObject b) {
//                    String valA = new String();
//                    DateTime dateA= new DateTime();
//                    String valB = new String();
//                    DateTime dateB= new DateTime();
//
//                    if(a.has("datetime")&&b.has("datetime"))
//                    {
//                        try {
//                        valA = (String) a.get(KEY_NAME);
//                        valB = (String) b.get(KEY_NAME);
//
//
//                        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
//                                .withLocale(Locale.ROOT)
//                                .withChronology(ISOChronology.getInstanceUTC());
//
//                        dateA = formatter.parseDateTime(valA);
//                        dateB = formatter.parseDateTime(valB);
//
//
//                    } catch (JSONException e) {
//                        //do something
//                            noReminders.setVisibility(View.VISIBLE);
//                    }
//                }
//
//                    return dateA.compareTo(dateB);
//                    //if you want to change the sort order, simply use the following:
//                    //return -valA.compareTo(valB);
//                }
//            });
//
//            for (int i = 0; i < pouchesAllOrders.length(); i++) {
//                sortedJsonArray.put(jsonValues.get(i));
//            }
//
//            //remove outdated pouches
//
//            for (int i=0; i<pouchesAllOrders.length();i++)
//            {
//                JSONObject pouch = pouchesAllOrders.getJSONObject(i);
//                if(pouch.has("datetime"))
//                {
//                    String datetime = pouch.getString("datetime");
//                    Log.e("ReminderPouchTime: ", i+":"+datetime);
//                    DateTime current = DateTime.now();
//                    DateTime dateA = new DateTime();
//
//                    try {
//                            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
//                                    .withLocale(Locale.ROOT)
//                                    .withChronology(ISOChronology.getInstanceUTC());
//
//                            dateA = formatter.parseDateTime(datetime);
//
//                        if(dateA.compareTo(current)>0) {
//                            Log.e("ReminderPouchTime: ", i+":");
//                            pouchesToShow.put(pouch);
//                        }
//
//
//                        } catch (Exception e) {
//                            //do something
//                        Log.e("Reminder datetime: ", e.getMessage());
//                        }
//                    }
//                else {
//                    //noReminders.setVisibility(View.VISIBLE);
//                    continue;
//                }
//
//
//            }
//
//            if(pouchesToShow.length()==0)
//                noReminders.setVisibility(View.VISIBLE);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("Reminder activity: ", e.getMessage());
//            noReminders.setVisibility(View.VISIBLE);
//        }

        //get this data from paper.
        Log.e("ReminderActivity:", ""+MainActivity.reminderOn);
        List<String> allKeys = Paper.book("reminder").getAllKeys();
        if(allKeys.contains("reminderOn"))
        {
            String reminderOnS = Paper.book("reminder").read("reminderOn");
            if(reminderOnS.equalsIgnoreCase("true"))
                MainActivity.reminderOn=true;
            else
                MainActivity.reminderOn=false;
        }
        else
        {

            Paper.book("reminder").write("reminderOn", "true");
            MainActivity.reminderOn=true;

        }
        Log.e("ReminderActivity: ", ""+MainActivity.reminderOn);
        if(MainActivity.reminderOn)
        {
            mButtonLeft.setText("Click to turn reminders off.");
            mButtonLeft.setTextColor(getResources().getColor(R.color.windowBackground));
        }
        else
        {
            mButtonLeft.setText("Turn reminders on");
            mButtonLeft.setTextColor(getResources().getColor(R.color.aluminum));

        }

        getReminderFromPaper();




        r_title.setTypeface(tf_l); mButtonLeft.setTypeface(tf_l); noReminders.setTypeface(tf_l);


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ReminderActivity: ", "back button pressed.");
                onBackPressed();
            }
        });

        r_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long time1 = new GregorianCalendar().getTimeInMillis()+40*1000;
                Long time2 = new GregorianCalendar().getTimeInMillis()+20*1000;
//
//                scheduleAlarm(time1,1);
//                scheduleAlarm(time2,2);
            }
        });
        // fillWithTestData();
    }

    void getReminderFromPaper(){

        String ps=pouchesJO.toString();

        HashMap<String, String> user = sessionManager.getUserDetails();

        try {

            List<String> allKeysrem = Paper.book("reminder").getAllKeys();
            Log.e("allKeysrem: ",""+allKeysrem.toString());
            if(allKeysrem.contains(""+user.get(SessionManager.KEY_MOBILE_UM)))
            {
                ps = Paper.book("reminder").read(""+user.get(SessionManager.KEY_MOBILE_UM)).toString();
                Log.e("ifAllreminder:", ps);
                pouchesJO= new JSONObject(ps);
                Log.e("finallyAllreminder:",ps.length()+":"+ ps.toString());

            }
            else
            {
                // Paper.book("reminder").write(""+user.get(SessionManager.KEY_MOBILE_UM), new JSONObject().toString());
                ps = Paper.book("reminder").read(""+user.get(SessionManager.KEY_MOBILE_UM),"{}");
                pouchesJO=new JSONObject();
                Log.e("elseAllreminder:", ""+ps);
            }

            Log.e("finallyAllreminder:", ""+Paper.book("reminder").read(""+user.get(SessionManager.KEY_MOBILE_UM)));




            if (pouchesJO.toString().length() > 5) {


                Log.e("UOrder resp: ", "in else for length=0");
                try {
                    pouchesToShow=pouchesJO.getJSONArray("pouches");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(pouchesToShow.length()!=0) {
                    Log.e("showrem: ", "fromPaper");
                    showRem();
                    storeReminder();
                }
                else{
                    placeholder.setImageResource(R.drawable.noremindersremaining);
                    placeholder.setVisibility(View.VISIBLE);
                    // noReminders.setVisibility(View.VISIBLE);
                }
                Log.e("UOrder resp: ", "after");


                //Toast.makeText(getApplicationContext(), "reminders received.", Toast.LENGTH_SHORT).show();

                //lv.setAdapter(new CustomUpcomingOrderAdapter(getApplicationContext(),dateArray,orderNoArray,nameArray, statusArray, dotsArray, gTotalArray, deliveryTimeArray,aljoUOrders  ));
            }



            else {
                //placeholder.setText("You don't have any upcoming orders.");
                //getReminder();
                placeholder.setImageResource(R.drawable.emptyreminders);
                placeholder.setVisibility(View.VISIBLE);
                getReminder();
            }
        } catch (Exception e) {

            Log.e("UOrderPapererr: ", e.getMessage());
            // placeholder.setText("There is some network issue, Could you please try again.");
            placeholder.setImageResource(R.drawable.emptyreminders);
            placeholder.setVisibility(View.VISIBLE);
            getReminder();
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        if (v.equals(mButtonLeft)) {
            {
                if (MainActivity.reminderOn) {
                    mButtonLeft.setText("Turn reminders on");
                    mButtonLeft.setTextColor(getResources().getColor(R.color.aluminum));
                    MainActivity.reminderOn=false;

                    //save in paper
                    Paper.book("reminder").write("reminderOn", "false");
                    Log.e("ReminderActivity:", ""+MainActivity.reminderOn);
                }
                else  {
                    mButtonLeft.setText("Click to turn reminders off.");
                    mButtonLeft.setTextColor(getResources().getColor(R.color.windowBackground));
                    MainActivity.reminderOn=true;
                    //save in paper
                    Paper.book("reminder").write("reminderOn", "true");
                    Log.e("ReminderActivity:", ""+MainActivity.reminderOn);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        finish();
        ReminderAlarmActivity.this.finishAffinity();
        super.onBackPressed();

    }


    @Override
    public void onViewSwipedToRight(int position) {
        //String swipedElement = mAdapter.getItem(position);
//        Toast.makeText(this, "Swiped" + position,
//                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onViewSwipedToLeft(int position) {
        //String swipedElement = mAdapter.getItem(position);
//        Toast.makeText(this, "Swiped" + position,
//                Toast.LENGTH_SHORT).show();
    }
//    public  void scheduleAlarm(Long time, int id)
//    {
//        // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
//        // we fetch  the current time in milliseconds and added 1 day time
//        // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day
//
//        // create an Intent and set the class which will execute when Alarm triggers, here we have
//        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
//        // alarm triggers and
//        //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
//        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
//        intentAlarm.putExtra("id",""+id);
//
//        // create the object
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        //set the alarm for particular time
//
//        if (Build.VERSION.SDK_INT >= 19)
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,id,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
//
//        else
//            alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,id,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
//
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,id,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
//        Log.e("Alarm Scheduled: ",""+DateTime.now());
//        Toast.makeText(this, "Alarm Scheduled "+DateTime.now(), Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    public void onStackEmpty() {
        //Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        placeholder.setImageResource(R.drawable.noremindersremaining);
        placeholder.setVisibility(View.VISIBLE);

    }



    public void getReminder(){
        String result = new String ();
        String line="";



        HashMap<String, String> user = session.getUserDetails();

        try {

            JsonObjectRequester mRequester = new RequestBuilder(getApplicationContext())
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(20000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToGetUpcomingOrder()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, MainActivity.app_url+"/pouches.json?after_now=true&mobile="+user.get(SessionManager.KEY_MOBILE_UM));

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class JsonObjectListenerToGetUpcomingOrder extends Response.SimpleObjectResponse {

        String [] dateArray, gTotalArray, statusArray, dotsArray, nameArray, orderNoArray, deliveryTimeArray;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {


                    Log.e("UOrder resp: ", "in else for length=0");
                    try {
                        HashMap<String, String> user = session.getUserDetails();
                        Paper.book("reminder").write(""+user.get(SessionManager.KEY_MOBILE_UM), jsonObject);
                        pouchesToShow=jsonObject.getJSONArray("pouches");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(pouchesToShow.length()!=0) {
                        Log.e("showrem: ", "fromWeb");
                        showRem();

                    }
                    else{
                        placeholder.setImageResource(R.drawable.emptyreminders);
                        placeholder.setVisibility(View.VISIBLE);
                    }
                    Log.e("UOrder resp: ", "after");


                    //Toast.makeText(getApplicationContext(), "reminders received.", Toast.LENGTH_SHORT).show();

                    //lv.setAdapter(new CustomUpcomingOrderAdapter(getApplicationContext(),dateArray,orderNoArray,nameArray, statusArray, dotsArray, gTotalArray, deliveryTimeArray,aljoUOrders  ));
                }



                else {
                    //placeholder.setText("You don't have any upcoming orders.");

                    placeholder.setImageResource(R.drawable.emptyreminders);
                    placeholder.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {

                Log.e("UOrderWeberr: ", e.getMessage());
                // placeholder.setText("There is some network issue, Could you please try again.");
                placeholder.setImageResource(R.drawable.emptyreminders);
                placeholder.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());

            placeholder.setImageResource(R.drawable.networkissue);
            placeholder.setVisibility(View.VISIBLE);
        }



        @Override
        public void onRequestStart(int requestCode) {
            //placeholder.setVisibility(View.INVISIBLE);
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
            //placeholder.setText("You don't have any upcoming orders. Press back and place an order from the home screen.");
            placeholder.setImageResource(R.drawable.networkissue);
            placeholder.setVisibility(View.VISIBLE);

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

    public void showRem(){

        JSONArray pouchesToReallyShow = new JSONArray();

        for(int loop=0; loop<pouchesToShow.length(); loop++){

            try {
                JSONObject pouchToCheck = pouchesToShow.getJSONObject(loop);

                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                        .withLocale(Locale.ROOT)
                        .withChronology(ISOChronology.getInstanceUTC());

                String forTime =  pouchToCheck.optString("datetime");

                DateTime dateA = formatter.parseDateTime(forTime);
                DateTime current = DateTime.now().minusMillis(3600000);
                Log.e("setReminderPouch: ", "datetime: "+dateA+" current: "+current);

                if(dateA.compareTo(current)>0) {

                    //jsonReallyValues.add(pouchToCheck);
                    pouchesToReallyShow.put(pouchToCheck);

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("showRemErr:: ",e.getMessage());

            }
        }

        placeholder.setVisibility(View.INVISIBLE);
        mData = new ArrayList<>();
        mAdapter = new CustomCardReminderAdapter(ReminderAlarmActivity.this, pouchesToReallyShow);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);
    }

    public void storeReminder(){
        String result = new String ();
        String line="";



        HashMap<String, String> user = session.getUserDetails();

        try {

            JsonObjectRequester mRequester = new RequestBuilder(getApplicationContext())
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(false) //Show error with toast on Network or Server error
                    .shouldCache(true)
                    .timeOut(20000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToStoreUpcomingOrder()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, MainActivity.app_url+"/pouches.json?after_now=true&mobile="+user.get(SessionManager.KEY_MOBILE_UM));

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class JsonObjectListenerToStoreUpcomingOrder extends Response.SimpleObjectResponse {

        String [] dateArray, gTotalArray, statusArray, dotsArray, nameArray, orderNoArray, deliveryTimeArray;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {


                    Log.e("UOrder resp: ", "in else for length=0");
                    try {
                        HashMap<String, String> user = session.getUserDetails();
                        Paper.book("reminder").write(""+user.get(SessionManager.KEY_MOBILE_UM), jsonObject);
                        pouchesToShow=jsonObject.getJSONArray("pouches");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(pouchesToShow.length()!=0) {
                        Log.e("showrem: ", "fromWeb");
                        //showRem();

                    }
                    else{
                        // placeholder.setImageResource(R.drawable.emptyreminders);
                        // placeholder.setVisibility(View.VISIBLE);
                    }
                    Log.e("UOrder resp: ", "after");


                    //  Toast.makeText(getApplicationContext(), "reminders received.", Toast.LENGTH_SHORT).show();

                    //lv.setAdapter(new CustomUpcomingOrderAdapter(getApplicationContext(),dateArray,orderNoArray,nameArray, statusArray, dotsArray, gTotalArray, deliveryTimeArray,aljoUOrders  ));
                }



                else {
                    //placeholder.setText("You don't have any upcoming orders.");

                    // placeholder.setImageResource(R.drawable.emptyreminders);
                    // placeholder.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {

                Log.e("UOrderWeberr: ", e.getMessage());
                // placeholder.setText("There is some network issue, Could you please try again.");
                // placeholder.setImageResource(R.drawable.emptyreminders);
                // placeholder.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("onErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());

            //placeholder.setImageResource(R.drawable.networkissue);
            //placeholder.setVisibility(View.VISIBLE);
        }



        @Override
        public void onRequestStart(int requestCode) {
            //placeholder.setVisibility(View.INVISIBLE);
            Log.e("UOrder resp: ", "onRequestStarted()");
            // setBlockUser();
        }




        @Override
        public void onRequestFinish(int requestCode) {
            Log.e("UOrder resp: ", "onRequestFinish()");
            // cancelBlockUser();


        }
        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {
            //Network or Server error
            Log.e ("onErrorResponse: ", requestCode +" : "+message.toString()+" : "+volleyError.getMessage());
            //placeholder.setText("You don't have any upcoming orders. Press back and place an order from the home screen.");
            // placeholder.setImageResource(R.drawable.networkissue);
            // placeholder.setVisibility(View.VISIBLE);

        }



    }


}
