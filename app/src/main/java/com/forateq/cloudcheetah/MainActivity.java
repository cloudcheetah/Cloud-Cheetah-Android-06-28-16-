package com.forateq.cloudcheetah;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.MainFragment;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.onesignal.OneSignal;

import java.io.IOException;

/**
 * this class is the main activity of the class, this is the entry point of the application
 */
public class MainActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    private AccountManager accountManager;
    private Account[] accounts;
    private Account account;

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
}
