package com.truemdhq.projectx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.truemdhq.projectx.service.AlarmService;

/**
 * Created by yashvardhansrivastava on 21/05/16.
 */
public class AlarmSetter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmService.class);
        service.setAction(AlarmService.CREATE);
        context.startService(service);
    }

}
