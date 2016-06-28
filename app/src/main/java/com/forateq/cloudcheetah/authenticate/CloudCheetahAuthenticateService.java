package com.forateq.cloudcheetah.authenticate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Vallejos Family on 5/13/2016.
 */
public class CloudCheetahAuthenticateService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        CloudCheetahAuthenticator authenticator = new CloudCheetahAuthenticator(this);
        return authenticator.getIBinder();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.e("First", "FirstService destroyed");
    }

}
