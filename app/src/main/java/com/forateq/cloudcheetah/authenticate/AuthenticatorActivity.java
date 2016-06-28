package com.forateq.cloudcheetah.authenticate;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.LoginWrapper;
import com.forateq.cloudcheetah.pojo.ResourceData;
import com.forateq.cloudcheetah.pojo.ResourceListResponseWrapper;
import com.forateq.cloudcheetah.pojo.UserData;
import com.forateq.cloudcheetah.pojo.UsersListResponseWrapper;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This class is used to display the user login and authenticate the credentials used by the current user of the app
 * Created by Vallejos Family on 5/12/2016.
 */
public class AuthenticatorActivity extends AppCompatActivity{

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
    public final static String PARAM_USER_PASS = "USER_PASS";
    private String authtoken;
    private AccountManager mAccountManager;
    private String mAuthTokenType;
    private String mAccountType;
    private Bundle mResultBundle = null;
    private String notification_id;
    @Bind(R.id.username)
    EditText usernameEditText;
    @Bind(R.id.password)
    EditText passwordEditText;
    @Bind(R.id.login)
    Button loginButton;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    public static final String TAG = "AuthenticatorActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AccountGeneral.DEVICE_ID = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mAccountType = getIntent().getStringExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE);
        mAccountManager = AccountManager.get(getBaseContext());
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.e("debug", "User:" + userId);
                notification_id = userId;
                if (registrationId != null) {
                    Log.e("debug", "registrationId:" + registrationId);
                }
            }
        });
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null) {
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
        }
        ButterKnife.bind(this);
        ((CloudCheetahApp) getApplication()).getNetworkComponent().inject(this);
    }

    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    /**
     * This method is used to submit the credentials input by the users to the web services for authentication
     */
    public void submit(){
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
            final String userName = usernameEditText.getText().toString();
            final String userPass = passwordEditText.getText().toString();
            Observable<LoginWrapper> observable = cloudCheetahAPIService.login(userName, userPass, AccountGeneral.DEVICE_ID, CloudCheetahAPIService.SERVER_TOKEN, notification_id);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<LoginWrapper>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Bundle data = new Bundle();
                                data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                                Intent res = new Intent();
                                res.putExtras(data);
                                if(mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();
                                finishLogin(res);
                            }

                            @Override
                            public void onNext(LoginWrapper loginWrapper) {
                                Bundle data = new Bundle();
                                if(loginWrapper.getLogin().isLogin_success()){
                                    authtoken =  loginWrapper.getKey().getSession_key();
                                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);
                                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                                    SharedPreferences sharedPreferences = AuthenticatorActivity.this.getSharedPreferences(AccountGeneral.ACCOUNT_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(AccountGeneral.ACCOUNT_USERNAME, userName);
                                    editor.putString(AccountGeneral.SESSION_KEY, authtoken);
                                    editor.putString(AccountGeneral.NOTIFICATION_ID, notification_id);
                                    editor.putString(AccountGeneral.USER_ID, ""+loginWrapper.getLogin().getId());
                                    editor.commit();
                                    data.putString(PARAM_USER_PASS, userPass);
                                    try {
                                        OneSignal.postNotification(new JSONObject("{'contents': {'en':'Login success!'}, 'include_player_ids': ['" + notification_id + "']}"), null);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                }
                                else{
                                    Toast.makeText(AuthenticatorActivity.this, "Username and password does not match", Toast.LENGTH_LONG).show();
                                    if(mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                }
                                new Delete().from(Users.class).execute();
                                saveUsers(userName, AccountGeneral.DEVICE_ID, authtoken);
                                Intent res = new Intent();
                                res.putExtras(data);
                                finishLogin(res);
                            }
                        });


        }
        else{
            Toast.makeText(this, "Please connect to the internet to login", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.login)
    void login(){
        Log.e("Clicked", "Clicked");
        submit();
    }

    /**
     * Checks if there is a network available before login
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void saveUsers(final String userid, final String deviceid, final String key){
        Observable<UsersListResponseWrapper> observable = cloudCheetahAPIService.getAllUsers(userid, deviceid, key);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UsersListResponseWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(UsersListResponseWrapper usersListResponseWrapper) {
                        for(UserData userData : usersListResponseWrapper.getData()){
                            Users users = new Users();
                            users.setUser_id(userData.getId());
                            users.setUser_name(userData.getUser_id());
                            users.setEmployee_id(userData.getEmployee_id());
                            users.setIs_admin(userData.is_admin());
                            users.setActive(userData.isActive());
                            users.setFull_name(userData.getFull_name());
                            users.setFirst_name(userData.getFirst_name());
                            users.setLast_name(userData.getLast_name());
                            users.save();
                        }
                        saveResources(userid, deviceid, key);
                    }
                });
    }

    public void saveResources(String userid, String deviceid, String key){


        Observable<ResourceListResponseWrapper> observable = cloudCheetahAPIService.getAllResources(userid, deviceid, key);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResourceListResponseWrapper>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(ResourceListResponseWrapper resourceListResponseWrapper) {
                        for(ResourceData resourceData : resourceListResponseWrapper.getData()){
                            Resources resources = new Resources();
                            resources.setResource_id(resourceData.getId());
                            resources.setName(resourceData.getName());
                            resources.setParent_id(resourceData.getParent_id());
                            resources.setAccount_id(resourceData.getAccount_id());
                            resources.setType_id(resourceData.getType_id());
                            resources.setUnit_of_measurement(resourceData.getUnit_of_measurement());
                            resources.setUnit_cost(resourceData.getUnit_cost());
                            resources.setSales_price(resourceData.getSales_price());
                            resources.setReorder_point(resourceData.getReorder_point());
                            resources.setVendor_id(resourceData.getVendor_id());
                            resources.save();
                        }
                    }
                });

    }

    /**
     * Closes the login activity after successful login
     * @param intent
     */
    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        Intent loginIntent = new Intent(this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(loginIntent);
    }
}
