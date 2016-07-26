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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.AccountsAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.pojo.AddAccountWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by PC1 on 7/25/2016.
 */
public class AddAccountFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.account_name)
    EditText accountNameET;
    @Bind(R.id.account_type)
    Spinner accountTypeSP;
    @Bind(R.id.account_number)
    EditText accountNumberET;
    @Bind(R.id.account_description)
    EditText accountDescriptionET;
    @Bind(R.id.add_account)
    Button addAccountBT;
    public static final String ACCOUNT_ASSET = "Asset";
    public static final String ACCOUNT_LIABILITY = "Liability";
    public static final String ACCOUNT_EQUITY = "Equity";
    public static final String ACCOUNT_INCOME = "Income";
    public static final String ACCOUNT_EXPENSE = "Expense";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    int parent_id;
    List<String> accountType;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_account_fragment, container, false);
        parent_id = getArguments().getInt("parent_id");
        return v;
    }

    public void init(){
        accountType = new ArrayList<>();
        accountType.add(ACCOUNT_ASSET);
        accountType.add(ACCOUNT_LIABILITY);
        accountType.add(ACCOUNT_EQUITY);
        accountType.add(ACCOUNT_INCOME);
        accountType.add(ACCOUNT_EXPENSE);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, accountType);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSP.setAdapter(typeAdapter);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        init();
    }

    @OnClick(R.id.add_account)
    public void addAccount(){
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Adding account...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Observable<AddAccountWrapper> observable = cloudCheetahAPIService.addAccount(accountNameET.getText().toString(),
                    accountNumberET.getText().toString(),
                    accountDescriptionET.getText().toString(),
                    parent_id,
                    accountType.indexOf(accountTypeSP.getSelectedItem().toString()) + 1,
                    userName,
                    deviceid,
                    sessionKey);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<AddAccountWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            MainActivity.popFragment();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("AddAccount", e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(AddAccountWrapper addAccountWrapper) {
                            if(addAccountWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                Accounts accounts = addAccountWrapper.getData();
                                accounts.save();
                                AccountsFragment.accountsAdapter.addItem(accounts);
                            }
                            else{
                                Toast.makeText(getActivity(), "There is a problem adding new account. Please contact the administrator. Error code: "+addAccountWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "Please connect to a network to add this account.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
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
