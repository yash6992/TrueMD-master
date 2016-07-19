package info.truemd.android.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;

import info.truemd.android.receiver.AlarmReceiver;

/**
 * Created by yashvardhansrivastava on 21/05/16.
 */
public class AlarmService extends IntentService {

    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";

    private IntentFilter matcher;

    public AlarmService() {
        super("");
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        String notificationId = intent.getStringExtra("notificationId");

        if (matcher.matchAction(action)) {
            execute(action, notificationId);
        }
    }

    private void execute(String action, String notificationId) {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


            Intent i = new Intent(this, AlarmReceiver.class);

            PendingIntent pi = PendingIntent.getBroadcast(this, Integer.parseInt(notificationId), i,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            if (CREATE.equals(action)) {
                am.set(AlarmManager.RTC_WAKEUP, 1212112, pi);

            } else if (CANCEL.equals(action)) {
                am.cancel(pi);
            }
        }

    }


