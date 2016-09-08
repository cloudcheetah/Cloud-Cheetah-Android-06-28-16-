package com.forateq.cloudcheetah.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.afollestad.materialdialogs.MaterialDialog;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.CashInOut;
import com.forateq.cloudcheetah.models.Conversations;
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.models.Employees;
import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.models.MyTasks;
import com.forateq.cloudcheetah.models.ProjectMembers;
import com.forateq.cloudcheetah.models.ProjectResources;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.TaskCashInCashOut;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.models.TaskResources;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Units;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** This fragment is used to display the profile of the current user of the app
 * Created by Vallejos Family on 5/11/2016.
 */
public class ProfileFragment extends Fragment {

    @Bind(R.id.background)
    ImageView backGroundIV;
    @Bind(R.id.contact_no)
    TextView contactTV;
    @Bind(R.id.email)
    TextView emailTV;
    @Bind(R.id.birthday)
    TextView birthdayTV;
    @Bind(R.id.gender)
    TextView genderTV;
    @Bind(R.id.profile_pic)
    ImageView profilePicIV;
    @Bind(R.id.name)
    TextView nameTV;
    @Bind(R.id.address)
    TextView addressTV;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.logout)
    CardView logoutCV;
    Employees employee;
    String userName;
    public static final String TAG = "UpdateEmployee";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    private AccountManager accountManager;
    private Account [] accounts;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
        userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
        employee = Employees.getEmployee(Users.getEmployeeId(userName));
        return v;
    }

    public ProfileFragment() {
        super();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        init();
    }

    public void init(){
        Picasso.with(getActivity()).load(R.drawable.background).fit().centerCrop().into(backGroundIV);
        contactTV.setText(""+employee.getContact_no());
        emailTV.setText(""+employee.getEmail_address());
        birthdayTV.setText(""+employee.getDate_of_birth());
        if(employee.getGender_id() == 0){
            genderTV.setText("Male");
        }
        else{
            genderTV.setText("Female");
        }
        nameTV.setText(employee.getFirst_name() + " " + employee.getLast_name());
        addressTV.setText(employee.getAddress());
        Picasso.with(ApplicationContext.get()).load("http://"+employee.getImage()).placeholder( R.drawable.progress_animation ).resize(100, 100)
                .centerCrop().into(profilePicIV);
    }

    @OnClick(R.id.fab)
    public void updateProfile(){
        Bundle bundle = new Bundle();
        bundle.putInt("employee_id", Users.getEmployeeId(userName));
        UpdateEmployeeFragment updateEmployeeFragment = new UpdateEmployeeFragment();
        updateEmployeeFragment.setArguments(bundle);
        MainActivity.replaceFragment(updateEmployeeFragment, TAG);
    }

    @OnClick(R.id.logout)
    public void logout(){
        if(isNetworkAvailable()){
            final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                    .title("Logout")
                    .content("Are you sure you want to logout?")
                    .contentColorRes(R.color.colorText)
                    .titleColorRes(R.color.colorText)
                    .backgroundColorRes(R.color.colorPrimary)
                    .widgetColorRes(R.color.colorText)
                    .positiveText("Ok")
                    .negativeText("Cancel")
                    .positiveColorRes(R.color.colorText)
                    .negativeColorRes(R.color.colorText)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage("Logging out...");
                            mProgressDialog.show();
                            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            Log.e("Credentials", sessionKey + " " + userName + " " + deviceid);
                            Observable<ResponseWrapper> observable = cloudCheetahAPIService.logout(userName, deviceid, sessionKey);
                            observable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .unsubscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<ResponseWrapper>() {
                                        @Override
                                        public void onCompleted() {
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                            Log.e("Logout", e.getMessage(), e);
                                        }

                                        @Override
                                        public void onNext(ResponseWrapper responseWrapper) {
                                            if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                                truncateTables();
                                                Toast.makeText(getActivity(), "You have logout successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getActivity(), "Error: "+responseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                        }
                    });
            final MaterialDialog addNoteDialog = createNoteDialog.build();
            addNoteDialog.show();
        }
    }

    /**
     * Checks if there is a network available before login
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void truncateTables(){
        new Delete().from(Accounts.class).execute();
        new Delete().from(CashInOut.class).execute();
        new Delete().from(Conversations.class).execute();
        new Delete().from(Customers.class).execute();
        new Delete().from(Employees.class).execute();
        new Delete().from(Messages.class).execute();
        new Delete().from(MyTasks.class).execute();
        new Delete().from(ProjectMembers.class).execute();
        new Delete().from(ProjectResources.class).execute();
        new Delete().from(Projects.class).execute();
        new Delete().from(Resources.class).execute();
        new Delete().from(TaskCashInCashOut.class).execute();
        new Delete().from(TaskProgressReports.class).execute();
        new Delete().from(TaskResources.class).execute();
        new Delete().from(Tasks.class).execute();
        new Delete().from(Units.class).execute();
        new Delete().from(Users.class).execute();
        new Delete().from(Vendors.class).execute();
        accountManager = AccountManager.get(getActivity());
        accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        accountManager.removeAccount(accounts[0], null, null);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AccountGeneral.ACCOUNT_USERNAME, "");
        editor.putString(AccountGeneral.SESSION_KEY, "");
        editor.putString(AccountGeneral.NOTIFICATION_ID, "");
        editor.putString(AccountGeneral.PROJECT_TIMESTAMP, "");
        editor.putString(AccountGeneral.USER_ID, "");
        editor.putBoolean(AccountGeneral.LOGIN_STATUS, false);
        editor.commit();
        OneSignal.setSubscription(false);
        Intent loginIntent = new Intent(getActivity(), MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(loginIntent);
    }
}
