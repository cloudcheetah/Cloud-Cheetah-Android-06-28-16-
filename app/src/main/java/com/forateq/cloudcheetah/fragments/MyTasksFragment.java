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
import com.forateq.cloudcheetah.adapters.MyTasksAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.MyTasks;
import com.forateq.cloudcheetah.pojo.MyTasksResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

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
public class MyTasksFragment extends Fragment {

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_tasks)
    RecyclerView listTasks;
    public static MyTasksAdapter myTasksAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String TAG = "MyTasksFragment";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_tasks_fragment,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) ApplicationContext.get()).getNetworkComponent().inject(this);
        getMyTasks();
    }

    public void init(){
        myTasksAdapter = new MyTasksAdapter(MyTasks.getMyTasks(), ApplicationContext.get());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listTasks.setLayoutManager(mLinearLayoutManager);
        listTasks.setAdapter(myTasksAdapter);
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

    public void getMyTasks() {
        if (isNetworkAvailable()) {
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Getting my tasks...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String myTaskTimeStamp = sharedPreferences.getString(AccountGeneral.MY_TASKS_TIMESTAMP, "");
            Observable<MyTasksResponseWrapper> observable = cloudCheetahAPIService.getMyTasks(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID), sessionKey, myTaskTimeStamp);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<MyTasksResponseWrapper>() {
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
                        public void onNext(MyTasksResponseWrapper myTasksResponseWrapper) {
                            Log.e("Size", "" + myTasksResponseWrapper.getData().size());
                            Gson gson = new GsonBuilder()
                                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                                    .create();
                            Log.e("Response", gson.toJson(myTasksResponseWrapper));
                            for (MyTasks myTasks : myTasksResponseWrapper.getData()) {
                                if (MyTasks.getMyTask(myTasks.getTaskId()) == null) {
                                    myTasks.save();
                                }
                            }

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(AccountGeneral.MY_TASKS_TIMESTAMP, myTasksResponseWrapper.getTimestamp());
                            editor.apply();
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
