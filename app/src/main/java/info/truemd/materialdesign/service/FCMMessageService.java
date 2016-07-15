package info.truemd.materialdesign.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.alirezaafkar.json.requester.requesters.JsonArrayRequester;
import com.alirezaafkar.json.requester.requesters.JsonObjectRequester;
import com.alirezaafkar.json.requester.requesters.RequestBuilder;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonArray;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.truemd.materialdesign.R;
import info.truemd.materialdesign.activity.AllNotificationActivity;
import info.truemd.materialdesign.activity.MainActivity;
import info.truemd.materialdesign.activity.OrderDetailsActivity;
import info.truemd.materialdesign.activity.PrescriptionListActivity;
import info.truemd.materialdesign.helper.SessionManager;
import info.truemd.materialdesign.helper.TrueMDJSONUtils;
import info.truemd.materialdesign.receiver.AlarmReceiver;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 22/05/16.
 */
public class FCMMessageService extends FirebaseMessagingService{
    private static final String TAG = "MyFirebaseMsgService";

    int reminderIDOld=0; SessionManager sessionManager;

    private JSONArray allNotifications; String notificationJArrayString;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.e(TAG, "Notification Received");
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getData().toString());
        Log.e("FCM:","Reminders till now:"+ Paper.book("reminder_id").read("id"));

        try {
            reminderIDOld = Integer.parseInt(""+Paper.book("reminder_id").read("id"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String title = TrueMDJSONUtils.goThroughNullCheck(remoteMessage.getData().get("title"));
        String activity = TrueMDJSONUtils.goThroughNullCheck(remoteMessage.getData().get("status"));
        String timestamp = TrueMDJSONUtils.goThroughNullCheck(remoteMessage.getData().get("timestamp"));
        String orderNo = TrueMDJSONUtils.goThroughNullCheck(remoteMessage.getData().get("order_no"));
        String message = TrueMDJSONUtils.goThroughNullCheck(remoteMessage.getData().get("message"));

        Map<String, String> singleNotification = remoteMessage.getData();

        JSONObject singleNotificationObject = new JSONObject(singleNotification);
        Log.e("JSONNotification: ",singleNotificationObject.toString());

        sessionManager= new SessionManager(FCMMessageService.this);
        HashMap<String, String> user = sessionManager.getUserDetails();

        List<String> allKeys5 = Paper.book("reminder").getAllKeys();
        if(allKeys5.contains(""+user.get(SessionManager.KEY_MOBILE_UM)))
        {
            // Paper.book("introduction").write("intro","0");
        }
        else
        {
            Paper.book("reminder").write(""+user.get(SessionManager.KEY_MOBILE_UM),""+new JSONObject().toString());
        }
        Log.e("AppController:", ""+Paper.book("reminder").read(""+user.get(SessionManager.KEY_MOBILE_UM)));


        //set notification initialization on paper.
        List<String> allKeysnoti = Paper.book("notification").getAllKeys();
        if(allKeysnoti.contains(""+user.get(SessionManager.KEY_MOBILE_UM)))
        {
            notificationJArrayString = Paper.book("notification").read(""+user.get(SessionManager.KEY_MOBILE_UM));
        }
        else
        {
            JSONArray f = new JSONArray();
            Paper.book("notification").write(""+user.get(SessionManager.KEY_MOBILE_UM), "[]");
        }
        Log.e("FCMNotification:", notificationJArrayString.length()+" "+Paper.book("notification").read(""+user.get(SessionManager.KEY_MOBILE_UM)));

        try {
            allNotifications = new JSONArray(notificationJArrayString);
            allNotifications.put(singleNotificationObject);
        } catch (JSONException e) {
            allNotifications=new JSONArray();
            Log.e("FCMMessageJA: ", e.getMessage());
            e.printStackTrace();
        }

        Paper.book("notification").write(""+user.get(SessionManager.KEY_MOBILE_UM), allNotifications.toString());
        Log.e("FCMNotificationEDIT:", allNotifications.toString().length()+" "+Paper.book("notification").read(""+user.get(SessionManager.KEY_MOBILE_UM)));


        if( activity.equalsIgnoreCase("OPac") )
        {
            performActionOPac(title, activity, timestamp, orderNo, message);
        }
        else if( activity.equalsIgnoreCase("ODel")|| activity.equalsIgnoreCase("OCan"))
        {
            performActionODel(title, activity, timestamp, orderNo, message);
        }
        else if( activity.equalsIgnoreCase("ORej") )
        {
            performActionODel(title, activity, timestamp, orderNo, message);
        }
        else
        sendNotificationNormal(title, activity, timestamp, orderNo, message);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotificationNormal(String... messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageBody[0])
                .setContentText(messageBody[4])
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void performActionOPac(String... messageBody) {

        Log.e("Noti: ", "performActionOPac");

        Intent intent = new Intent(this, PrescriptionListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageBody[0])
                .setContentText(messageBody[4])
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        //scheduleAlarm(30000,1);
        //scheduleAlarm(60000,2);
//        setAlarmForTime("2016-07-10T16:27:00.001Z");
//        setAlarmForTime("2016-07-10T16:28:00.001Z");
//        setAlarmForTime("2016-07-10T16:29:00.001Z");
//        setAlarmForTime("2016-07-10T16:30:00.001Z");
//        setAlarmForTime("2016-07-10T16:31:00.001Z");
//        setAlarmForTime("2016-07-10T16:22:00.001Z");
//        setAlarmForTime("2016-07-10T16:23:00.001Z");
//        setAlarmForTime("2016-07-10T16:24:00.001Z");
//        setAlarmForTime("2016-07-10T16:25:00.001Z");
//        setAlarmForTime("2016-07-10T16:26:00.001Z");
        getPouches();


    }

    void setAlarmForTime(String forTime){

        long longtime=0;

        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                    .withLocale(Locale.ROOT)
                    .withChronology(ISOChronology.getInstanceUTC());

            DateTime dateA = formatter.parseDateTime(forTime);
            DateTime current = DateTime.now();
            Log.e("setReminderPouch: ", "datetime: "+dateA+" current: "+current);

            if(dateA.compareTo(current)>0) {
                //pouchesToShow.put(pouch);
                //Log.e("setReminderPouch: ", "datetime: "+dateA+" current: "+current);
                long getTimeToAlarmMilliSec = dateA.getMillis()-current.getMillis();
                Log.e("setReminderPouch: ", ""+getTimeToAlarmMilliSec);
                scheduleAlarm(getTimeToAlarmMilliSec,1);

            }


        } catch (Exception e) {

            Log.e("setReminderPouch: ", e.getMessage());
        }


    }

    private void performActionODel(String... messageBody) {


        //get order screen details



        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageBody[0])
                .setContentText(messageBody[4])
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public boolean setRemindersFromPouches(JSONArray pouches) throws JSONException
    {
//        {
//            "_id":{
//            "$oid":"574d085131d54fff074cffcd"
//        },
//            "code":"620089",
//                "created_at":"2016-05-31T03:43:13.948Z",
//                "date":"30 May",
//                "datetime":"2016-05-30T16:00:00.000+00:00",
//                "day":"Mon",
//                "day_no":1,
//                "meds":[
//            {
//                "name":"LANOXIN 0.25MG TABLET",
//                    "truemdCode":"IQz6I3",
//                    "quantity":0,
//                    "truePack":true,
//                    "daily_frequency":4,
//                    "weekly_frequency":null,
//                    "duration_type":"day",
//                    "meal":"after"
//            },
//            {
//                "name":"CLOPIDOGREL 75MG TABLET",
//                    "truemdCode":"46V434",
//                    "quantity":0,
//                    "truePack":true,
//                    "daily_frequency":3,
//                    "weekly_frequency":null,
//                    "duration_type":"tbc",
//                    "meal":"before"
//            }
//            ],
//            "patient_name":"Yashvardhan ",
//                "time":"12:00 PM",
//                "time_of_day":"afternoon",
//                "updated_at":"2016-05-31T03:43:13.948Z"
//        }

        boolean success = false;

        for (int i=0; i<pouches.length();i++)
        {
            JSONObject pouch = pouches.getJSONObject(i);
            if(pouch.has("datetime"))
            {
                String datetime = pouch.getString("datetime");
                Log.e("setReminderPouch: ", "datetime string: "+datetime);
                DateTime current = DateTime.now();
                DateTime dateA = new DateTime();

                try {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
                            .withLocale(Locale.ROOT)
                            .withChronology(ISOChronology.getInstanceUTC());

                    dateA = formatter.parseDateTime(datetime).minusMillis(19800000);

                    if(dateA.compareTo(current)>0) {
                        //pouchesToShow.put(pouch);
                        Log.e("setReminderPouch: ", "datetime: "+dateA+" current: "+current);
                        long getTimeToAlarmMilliSec = dateA.getMillis()-current.getMillis();
                        Log.e("setReminderPouch: ", ""+getTimeToAlarmMilliSec);
                        String remID= Paper.book("reminder_id").read("id");
                        int id = Integer.parseInt(remID);
                        scheduleAlarm(getTimeToAlarmMilliSec,id );
                        id++;

                        Paper.book("reminder_id").write("id", ""+id);
                    }


                } catch (Exception e) {
                    //do something
                    success=false;
                    Log.e("setReminderPouch: ", e.getMessage());
                }
            }
            else {

                continue;
            }

            success=true;
        }

        return success;
    }

    public  void cancelAllAlarms(Context context, int id)
    {
        for(int alarm_id=0;alarm_id<id;alarm_id++) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intentAlarm = new Intent(context, AlarmReceiver.class);
            //PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 0, updateServiceIntent, 0);
            PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(this,id,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            // Cancel alarms
            try {
                alarmManager.cancel(pendingUpdateIntent);
            } catch (Exception e) {
                Log.e(TAG, "AlarmManager update was not canceled. " + e.toString());
            }
        }
    }
    public  void scheduleAlarm(long time, int id)
    {
        // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
        // we fetch  the current time in milliseconds and added 1 day time
        // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day

        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers and
        //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
        Intent intentAlarm = new Intent(this, AlarmReceiver.class);
        intentAlarm.putExtra("id",""+id);

        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time

        if (Build.VERSION.SDK_INT >= 19) {

            long alarmscheduletime= DateTime.now().getMillis()+time;

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmscheduletime, PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

            DateTime alarm = new DateTime(alarmscheduletime);

            Log.e(""+ id+ " Alarm Scheduled>=19: ",""+alarm+" : "+DateTime.now());

            Log.e(""+ id+ " Alarm Scheduled>=19: ",""+alarm+" : "+DateTime.now());
        }else {
            long alarmscheduletime= DateTime.now().getMillis()+time;

            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmscheduletime, PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

            DateTime alarm = new DateTime(alarmscheduletime);

            Log.e(""+ id+" Alarm Scheduled: ",""+alarm+" : "+DateTime.now());
        }
        //alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this,id,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));


    }

    void getPouches(){

        Log.e("MainActivity: ","in checkAuthentication()");
        SessionManager session = new SessionManager(FCMMessageService.this);
        HashMap<String, String> user = session.getUserDetails();

        try {


            JsonObjectRequester mRequester;
            mRequester = new RequestBuilder(FCMMessageService.this)
                    //.requestCode(REQUEST_CODE)
                    .contentType(ContentType.TYPE_JSON) //or ContentType.TYPE_FORM
                    .showError(true) //Show error with toast on Network or Server error
                    .shouldCache(false)
                    .timeOut(50000)
                    .priority(Request.Priority.NORMAL)
                    .allowNullResponse(true)
                    //.tag(REQUEST_TAG)
                    .addToHeader("X-User-Token", user.get(SessionManager.KEY_AUTHENTICATION_UM))
                    .addToHeader("Content-Type", "application/json")
                    .addToHeader("X-User-Email", user.get(SessionManager.KEY_MOBILE_UM) + "@truemd.in")
                    .buildObjectRequester(new JsonObjectListenerToGetPouches()); //or .buildArrayRequester(listener);

            mRequester.request(Methods.GET, "http://truemd-carebook.rhcloud.com/pouches.json?after_now=true&mobile="+user.get(SessionManager.KEY_MOBILE_UM));


        } catch (Exception e) {
            Log.e("MainActivity: ","in checkAuthentication(): "+ e.getMessage());
            e.printStackTrace();
        }
    }

    class JsonObjectListenerToGetPouches extends Response.SimpleObjectResponse {


        boolean success=false;

        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {

            sessionManager= new SessionManager(FCMMessageService.this);
            HashMap<String, String> user = sessionManager.getUserDetails();
            //Ok
            try {
                if (jsonObject.toString().length() > 5) {

                    Log.e("getPouches() resp: ", jsonObject.toString());
                    JSONArray pouches = jsonObject.getJSONArray("pouches");
                    Paper.book("reminder").write(""+user.get(SessionManager.KEY_MOBILE_UM),jsonObject.toString());
                    setRemindersFromPouches(pouches);
                    Log.e("FCMinGetPouches:","Reminders set:"+ Paper.book("reminder_id").read("id"));
                    cancelAllAlarms(FCMMessageService.this, reminderIDOld);
                    Log.e("FCMinGetPouches:","Reminders cancelled:"+reminderIDOld);

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("FCMinGetPouches:","Reminders set error:"+ Paper.book("reminder_id").read("id"));
                Log.e("FCMMessageService: ","in getPouches(): "+e.getMessage());
            }
        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {
            //Error (Not server or network error)
            Log.e ("FCMonErrorResponse: ", requestCode +" : "+errorObject.toString()+" : "+volleyError.getMessage());

        }


    }



}
