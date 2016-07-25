package com.forateq.cloudcheetah;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.MainFragment;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.CashInOut;
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.pojo.ProcessCashIn;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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
        OneSignal.enableNotificationsWhenActive(true);
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
        else{
//            for(Accounts accounts : Accounts.getAccounts()){
//                Log.e("Account Name", ""+accounts.getAccount_name());
//            }
//            for(Customers customers : Customers.getCustomers()){
//                Log.e("Customer Name", ""+customers.getName());
//            }
//            Gson gson = new GsonBuilder()
//                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
//                    .serializeNulls()
//                    .create();
//            List<CashInOut> cashInOutList = new ArrayList<>();
//
//            CashInOut cashInOut = new CashInOut();
//            cashInOut.setAccount_id(Accounts.getAccounts().get(0).getAccountId());
//            cashInOut.setAmount(100);
//            cashInOut.setDescription("Sample");
//            cashInOut.setInvoice_no(1);
//            cashInOut.setItem_id(1);
//            cashInOut.setLocation("QC");
//            cashInOut.setPayer_id(Customers.getCustomers().get(0).getCustomerId());
//            cashInOut.setQty(3);
//            cashInOut.setReceipt_no(2);
//            cashInOut.setTransaction_date("01/02/16");
//            cashInOut.setType_id(AccountGeneral.CASH_IN);
//
//            CashInOut cashInOut2 = new CashInOut();
//            cashInOut2.setAccount_id(Accounts.getAccounts().get(1).getAccountId());
//            cashInOut2.setAmount(100);
//            cashInOut2.setDescription("Sample");
//            cashInOut2.setInvoice_no(1);
//            cashInOut2.setItem_id(1);
//            cashInOut2.setLocation("QC");
//            cashInOut2.setPayer_id(Customers.getCustomers().get(1).getCustomerId());
//            cashInOut2.setQty(3);
//            cashInOut2.setReceipt_no(2);
//            cashInOut2.setTransaction_date("01/02/16");
//            cashInOut2.setType_id(AccountGeneral.CASH_OUT);
//
//            cashInOutList.add(cashInOut);
//            cashInOutList.add(cashInOut2);
//            ProcessCashIn processCashIn =  new ProcessCashIn();
//            processCashIn.setCash_flow(cashInOutList);
//            String json = gson.toJson(processCashIn);
//            Log.e("Json", json);
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
