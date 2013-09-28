package com.augmentum.note.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.augmentum.note.activity.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ALARM_ACTION = "com.augmentum.note.ALARM_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("AlarmReceiver", "intent");

        if (ALARM_ACTION.equals(intent.getAction())) {
            intent.setClass(context, AlarmActivity.class);
            context.startActivity(intent);
        }

    }
}
