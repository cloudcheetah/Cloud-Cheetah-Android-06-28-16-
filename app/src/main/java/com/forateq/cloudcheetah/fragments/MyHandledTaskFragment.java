package com.forateq.cloudcheetah.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.MyHandledTasksAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.MyHandledTasks;
import com.forateq.cloudcheetah.pojo.MyHandledTasksResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Vallejos Family on 8/30/2016.
 */

public class MyHandledTaskFragment extends Fragment {

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_tasks)
    RecyclerView listTasks;
    public static MyHandledTasksAdapter myHandledTasksAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String TAG = "MyTasksFragment";
    private String started = "";
    private String hold = "";
    private String resume = "";
    private String cancel = "";
    private String complete = "";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    private SharedPreferences preferences;
    private String[] status = new String[]{
            "Started",
            "Hold",
            "Resume",
            "Cancel",
            "Complete"
    };
    private boolean[] checkedStatus;
    private List<String> statusList;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_handled_tasks_fragment,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) ApplicationContext.get()).getNetworkComponent().inject(this);
        getMyHandledTasks();
    }

    public void init(){
        preferences = getActivity().getSharedPreferences("Filter", Context.MODE_PRIVATE);
        checkedStatus = new boolean[]{
                preferences.getBoolean(status[0], false),
                preferences.getBoolean(status[1], false),
                preferences.getBoolean(status[2], false),
                preferences.getBoolean(status[3], false),
                preferences.getBoolean(status[4], false)

        };
        statusList = Arrays.asList(status);
        if(checkedStatus[0] || checkedStatus[1] || checkedStatus[2] || checkedStatus[3] || checkedStatus[4]){
            if(checkedStatus[0]){
                started = ""+1;
            }
            if(checkedStatus[1]){
                hold = ""+2;
            }
            if(checkedStatus[2]){
                resume = ""+3;
            }
            if(checkedStatus[3]){
                cancel = ""+4;
            }
            if(checkedStatus[4]){
                complete = ""+5;
            }
            myHandledTasksAdapter = new MyHandledTasksAdapter(MyHandledTasks.getFilterHandledTasks(started, hold, resume, cancel, complete), getActivity());
        }
        else{
            myHandledTasksAdapter = new MyHandledTasksAdapter(MyHandledTasks.getMyHandledTasks(), getActivity());
        }
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listTasks.setLayoutManager(mLinearLayoutManager);
        listTasks.setAdapter(myHandledTasksAdapter);
        listTasks.setItemAnimator(new DefaultItemAnimator());
        setSearchForTasks();
    }

    public void setSearchForTasks(){
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getMyHandledTasks(){
        if (isNetworkAvailable()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Getting my handled tasks...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            Observable<MyHandledTasksResponseWrapper> observable = cloudCheetahAPIService.getMyHandledTasks(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID), sessionKey);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<MyHandledTasksResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                            SharedPreferences prefs = ApplicationContext.get().getSharedPreferences(AccountGeneral.ACCOUNT_NAME, Context.MODE_PRIVATE);
                            Log.e(TAG, prefs.getString(AccountGeneral.ACCOUNT_USERNAME, ""));
                            init();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("GettingTasks", e.getMessage(), e);
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(MyHandledTasksResponseWrapper myHandledTasksResponseWrapper) {
                            Gson gson = new GsonBuilder()
                                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                                    .create();
                            Log.e("Response", gson.toJson(myHandledTasksResponseWrapper));
                            for (MyHandledTasks myHandledTasks : myHandledTasksResponseWrapper.getData()) {
                                if (MyHandledTasks.getMyTask(myHandledTasks.getTaskId()) == null) {
                                    myHandledTasks.save();
                                }
                            }
                        }
                    });
        } else {
            Log.e(TAG, "Getting tasks...");
            SharedPreferences prefs = ApplicationContext.get().getSharedPreferences(AccountGeneral.ACCOUNT_NAME, Context.MODE_PRIVATE);
            Log.e(TAG, prefs.getString(AccountGeneral.ACCOUNT_USERNAME, ""));
        }
    }

    /**
     * Checks if there is a network available before login
     *
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}