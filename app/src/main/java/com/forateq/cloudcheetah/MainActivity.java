package com.forateq.cloudcheetah;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.CalendarFragment;
import com.forateq.cloudcheetah.fragments.ChatFragment;
import com.forateq.cloudcheetah.fragments.ContactsFragment;
import com.forateq.cloudcheetah.fragments.ERPFragment;
import com.forateq.cloudcheetah.fragments.HomeFragment;
import com.forateq.cloudcheetah.fragments.MainFragment;
import com.forateq.cloudcheetah.fragments.NotificationsFragment;
import com.forateq.cloudcheetah.fragments.ProfileFragment;
import com.forateq.cloudcheetah.models.ToDo;
import com.forateq.cloudcheetah.receivers.AlarmReceiver;
import com.forateq.cloudcheetah.service.AlarmService;
import com.forateq.cloudcheetah.utils.AlarmEvent;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.utils.NetworkStateChanged;
import com.onesignal.OneSignal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * this class is the main activity of the class, this is the entry point of the application
 */
public class MainActivity extends CameraActivity implements EasyPermissions.PermissionCallbacks{

    private static FragmentManager fragmentManager;
    private AccountManager accountManager;
    private Account[] accounts;
    private Account account;
    private static final int RC_LOCATION_INTERNET_PERM = 124;
    public static Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this); // register EventBus
        Intent i= new Intent(this, AlarmService.class);
        this.startService(i);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        init();
    }



    public void init(){
        CloudCheetahApp.homeFragment = new HomeFragment();
        CloudCheetahApp.chatFragment = new ChatFragment();
        CloudCheetahApp.contactsFragment = new ContactsFragment();
        CloudCheetahApp.erpFragment = new ERPFragment();
        CloudCheetahApp.profileFragment = new ProfileFragment();
        CloudCheetahApp.notificationsFragment = new NotificationsFragment();
        CloudCheetahApp.calendarFragment = new CalendarFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.enableDebugLogging(true);
        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            MainFragment mainFragment = new MainFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mainFragment)
                    .commit();
        }
        ApplicationContext.getInstance().init(getApplicationContext());
        OneSignal.enableInAppAlertNotification(false);
        OneSignal.enableNotificationsWhenActive(false);
        if (Build.VERSION.SDK_INT >= 23) {
            Log.e("Build Here", "Here");
            locationAndContactsTask();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initAccounts();
    }

    public void initAccounts(){
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
        boolean isLogin = sharedPreferences.getBoolean(AccountGeneral.LOGIN_STATUS, false);
        if(isLogin == false){
            addNewAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
        }
    }

    private void addNewAccount(String accountType, String authTokenType) {
        AccountManager.get(this).addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                    finish();
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    public static void replaceFragment(Fragment fragment, Bundle bundle, String tag){
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(tag)
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .commit();
    }

    public static void replaceFragment(Fragment fragment , String tag){
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(tag)
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                .commit();
    }

    public static void popFragment(){
        fragmentManager.popBackStack();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0 ){
            popFragment();
        } else {
            super.onBackPressed();
        }
    }


    public void checkAlarm(){
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 60); // first time
        long frequency= 60 * 1000; // in ms
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(RC_LOCATION_INTERNET_PERM)
    public void locationAndContactsTask() {
        String[] perms = { android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.VIBRATE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.GET_ACCOUNTS,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.WAKE_LOCK,
                android.Manifest.permission.SYSTEM_ALERT_WINDOW

        };
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_location_contacts),
                    RC_LOCATION_INTERNET_PERM, perms);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this); // unregister EventBus
    }

    // method that will be called when someone posts an event NetworkStateChanged
    @Subscribe
    public void onEventMainThread(NetworkStateChanged event) {
        if (!event.isInternetConnected()) {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void onEventMainThread(AlarmEvent event) {
        if (event.isAlarm()) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String date = month+"-"+day+"-"+year;
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            String time = hour + ":" + minute;
            Log.e("datetime", ""+date+time);
            ToDo todo = ToDo.getToDo(date, time);
            if(todo != null){
                startVibrate();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("stop_vibrate", true);
                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
                NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_stop_white_24dp, "Stop", pIntent).build();
                NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
                notification.setContentTitle("To Do");
                notification.setContentText(todo.getNote());
                notification.setSmallIcon(R.mipmap.ic_launcher);
                notification.setContentIntent(pIntent);
                notification.addAction(action);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification.build());
            }
            else{
                Log.e("Null", "Null");
            }
        }
    }

    public void startVibrate() {
        long pattern[] = { 0, 100, 200, 300, 400 };
        vibrator.vibrate(pattern, 0);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                vibrator.cancel();
            }
        }, 45000);
    }

}
