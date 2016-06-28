package com.forateq.cloudcheetah;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.forateq.cloudcheetah.components.DaggerNetworkComponent;
import com.forateq.cloudcheetah.components.NetworkComponent;
import com.forateq.cloudcheetah.modules.AppModule;
import com.forateq.cloudcheetah.modules.NetworkModule;
import com.onesignal.OneSignal;

/** This class is used to initialize the mobile database, and the dependency injector of the application
 * Created by Vallejos Family on 5/11/2016.
 */
public class CloudCheetahApp extends Application {

    private NetworkComponent networkComponent;
    public static String currentProjectComponent = "";
    public static String currentTaskComponent = "";
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        OneSignal.startInit(this).init();
        networkComponent = DaggerNetworkComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule("http://128.199.140.96"))
                .build();
    }

    public NetworkComponent getNetworkComponent(){
        return networkComponent;
    }

}
