package com.forateq.cloudcheetah;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.forateq.cloudcheetah.components.DaggerNetworkComponent;
import com.forateq.cloudcheetah.components.NetworkComponent;
import com.forateq.cloudcheetah.fragments.CalendarFragment;
import com.forateq.cloudcheetah.fragments.ChatFragment;
import com.forateq.cloudcheetah.fragments.ContactsFragment;
import com.forateq.cloudcheetah.fragments.ERPFragment;
import com.forateq.cloudcheetah.fragments.HomeFragment;
import com.forateq.cloudcheetah.fragments.NotificationsFragment;
import com.forateq.cloudcheetah.fragments.ProfileFragment;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.modules.AppModule;
import com.forateq.cloudcheetah.modules.NetworkModule;
import com.forateq.cloudcheetah.service.NotificationOpenedHandler;
import com.forateq.cloudcheetah.utils.MyLifeCycleHandler;
import com.onesignal.OneSignal;

import org.json.JSONObject;

/** This class is used to initialize the mobile database, and the dependency injector of the application
 * Created by Vallejos Family on 5/11/2016.
 */
public class CloudCheetahApp extends Application {

    private NetworkComponent networkComponent;
    public static String currentProjectComponent = "";
    public static String currentTaskComponent = "";
    public static String currentReportComponent = "";
    //Notifications
    public static int notificationType = 0;
    public static int currentReceiverId = 0;
    public static int currentSenderId = 0;
    public static int projectChatId = 0;
    public static TaskProgressReports taskProgressReports = null;
    public static int taskId = 0;
    //Fragments
    public static HomeFragment homeFragment = null;
    public static ChatFragment chatFragment = null;
    public static ERPFragment erpFragment = null;
    public static ProfileFragment profileFragment = null;
    public static ContactsFragment contactsFragment = null;
    public static NotificationsFragment notificationsFragment = null;
    public static CalendarFragment calendarFragment = null;

    public static CloudCheetahApp cloudCheetahApp;
    @Override
    public void onCreate() {
        super.onCreate();
        cloudCheetahApp = CloudCheetahApp.this;
        ActiveAndroid.initialize(this);
        networkComponent = DaggerNetworkComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule("http://128.199.140.96"))
                .build();
        OneSignal.startInit(this).setNotificationOpenedHandler(new NotificationOpenedHandler()).init();
        registerActivityLifecycleCallbacks(new MyLifeCycleHandler());
    }

    public static CloudCheetahApp getAppContext() {
        return cloudCheetahApp;
    }


    public NetworkComponent getNetworkComponent(){
        return networkComponent;
    }

}
