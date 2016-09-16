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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.PurchaseRequestAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.PurchaseRequests;
import com.forateq.cloudcheetah.models.RequestItems;
import com.forateq.cloudcheetah.pojo.ListPurchaseRequestsResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Vallejos Family on 9/8/2016.
 */
public class PurchaseRequestsFragment extends Fragment{

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_purchase_requests)
    RecyclerView listPurchaseRequest;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String TAG = "PurchaseRequestsFragment";
    public static PurchaseRequestAdapter adapter;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.purchase_requests_fragment, container, false);
        return v;
    }

    public void init(){
        adapter = new PurchaseRequestAdapter(PurchaseRequests.getPurchaseRequests(), getActivity());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listPurchaseRequest.setAdapter(adapter);
        listPurchaseRequest.setLayoutManager(mLinearLayoutManager);
        listPurchaseRequest.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        getPurchaseRequests();
    }

    public void getPurchaseRequests(){
        if(isNetworkAvailable()){
            new Delete().from(PurchaseRequests.class).execute();
            new Delete().from(RequestItems.class).execute();
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Observable<ListPurchaseRequestsResponseWrapper> observable = cloudCheetahAPIService.getPurchaseRequests(userName, deviceid, sessionKey);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<ListPurchaseRequestsResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            init();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("GetPurchaseRequests", e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(ListPurchaseRequestsResponseWrapper listPurchaseRequestsResponseWrapper) {
                            if(listPurchaseRequestsResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                for(PurchaseRequests purchaseRequests : listPurchaseRequestsResponseWrapper.getData()){
                                    for(RequestItems requestItems : purchaseRequests.getDetails()){
                                        requestItems.save();
                                    }
                                    purchaseRequests.save();
                                }
                            }
                            else{
                                Toast.makeText(getActivity(), "There is a problem getting purchase requests. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            init();
        }
    }

    @OnClick(R.id.fab)
    void addPurchaseRequest(){
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_copy", false);
        AddPurchaseRequestFragment addPurchaseRequestFragment = new AddPurchaseRequestFragment();
        MainActivity.replaceFragment(addPurchaseRequestFragment, TAG);
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
}
