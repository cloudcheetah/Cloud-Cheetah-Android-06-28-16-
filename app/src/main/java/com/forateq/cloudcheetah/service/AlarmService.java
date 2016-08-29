package com.forateq.cloudcheetah.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.forateq.cloudcheetah.receivers.AlarmReceiver;
import com.forateq.cloudcheetah.utils.AlarmEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by PC1 on 8/26/2016.
 */
public class AlarmService extends Service {
    private final IBinder mBinder = new MyBinder();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 45); // first time
        long frequency= 45 * 1000; // in ms
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        AlarmService getService() {
            return AlarmService.this;
        }
    }
}
