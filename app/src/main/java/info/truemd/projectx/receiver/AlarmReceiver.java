package com.truemdhq.projectx.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.List;
import java.util.Random;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.MainActivity;
import com.truemdhq.projectx.activity.ReminderActivity;
import com.truemdhq.projectx.activity.ReminderAlarmActivity;
import io.paperdb.Paper;

/**
 * Created by yashvardhansrivastava on 21/05/16.
 */
public class AlarmReceiver extends BroadcastReceiver
{
    private boolean reminderOn=true;   Notification mNotification;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub
        // here you can start an activity or service depending on your need
        // for ex you can start an activity to vibrate phone or to ring the phone

       String id= intent.getStringExtra("id");

        List<String> allKeys = Paper.book("reminder").getAllKeys();
        if(allKeys.contains("reminderOn"))
        {
            String reminderOnS = Paper.book("reminder").read("reminderOn");
            if(reminderOnS.equalsIgnoreCase("true"))
                reminderOn=true;
            else
                reminderOn=false;
        }
        else
        {

            Paper.book("reminder").write("reminderOn", "true");
            reminderOn=true;

        }
        Log.e("ReminderActivity: ", ""+reminderOn);




        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        Intent intent3 = new Intent(context, ReminderAlarmActivity.class);
        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent3);
        PendingIntent pIntent3 = PendingIntent.getActivity(context, Integer.parseInt(id), intent3, PendingIntent.FLAG_UPDATE_CURRENT);

        //assigned a unique id to notifications
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        if(reminderOn)
        {

           mNotification = new Notification.Builder(context)
                .setContentTitle("TrueMD Reminder")
                .setContentText("")
                .setSubText("Remember to take your medicines")
                .setSmallIcon(R.drawable.ic_stat_noti)
                .setContentIntent(pIntent3)
                .setSound(soundUri)
                .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // If you want to hide the notification after it was selected, do the code below
            mNotification.flags = Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(m, mNotification);

            Log.e("AlarmReceiver: ","Alarm Triggered "+id+": "+ DateTime.now());
            //Toast.makeText(context, "Alarm Triggered "+id+": "+ DateTime.now(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.e("AlarmReceiver: ","Silent Alarm Triggered "+id+": "+ DateTime.now());
        }
    }

}
