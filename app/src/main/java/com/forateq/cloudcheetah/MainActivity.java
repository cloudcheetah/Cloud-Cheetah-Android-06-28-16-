package com.forateq.cloudcheetah;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.MainFragment;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * this class is the main activity of the class, this is the entry point of the application
 */
public class MainActivity extends CameraActivity {

    private static FragmentManager fragmentManager;
    private AccountManager accountManager;
    private Account[] accounts;
    private Account account;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }



    public void init(){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.enableDebugLogging(true);
        if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
            MainFragment mainFragment = new MainFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mainFragment)
                    .commit();
        }
        ApplicationContext.getInstance().init(getApplicationContext());
        if(isStoragePermissionGranted()){
            Log.e("Granted", "Granted");
        }
        else{
            Log.e("Not Granted", "Not Granted");
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.e("debug", "User:" + userId);
                editor.putString(AccountGeneral.REGISTRATION_ID, userId);
                editor.commit();
                if (registrationId != null)
                    Log.e("debug", "registrationId:" + registrationId);
            }
        });
        OneSignal.enableInAppAlertNotification(true);
        String userId = sharedPreferences.getString(AccountGeneral.REGISTRATION_ID, "");
        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'Test Message'}, 'include_player_ids': ['" + userId + "']}"), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initAccounts();
    }

    public void initAccounts(){
        accountManager = AccountManager.get(this);
        accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        if(accounts.length == 0){
            Log.e("Authenticating", "Authenticating");
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

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("MainActivity","Permission is granted");
                return true;
            } else {

                Log.e("MainActivity","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.e("MainActivity","Permission is granted");
            return true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.e("MainActivity","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

}
