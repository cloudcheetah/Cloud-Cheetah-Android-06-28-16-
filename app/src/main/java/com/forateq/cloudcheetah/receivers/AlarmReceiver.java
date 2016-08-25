package com.forateq.cloudcheetah.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.forateq.cloudcheetah.utils.AlarmEvent;
import com.forateq.cloudcheetah.utils.NetworkStateChanged;
import com.forateq.cloudcheetah.utils.WakeLocker;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by PC1 on 8/19/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        // TODO Auto-generated method stub
        WakeLocker.acquire(ctx);
        EventBus.getDefault().post(new AlarmEvent(true));
    }
}
