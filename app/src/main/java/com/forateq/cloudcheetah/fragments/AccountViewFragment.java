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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.pojo.AddAccountWrapper;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
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
public class AccountViewFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.ripple_update)
    MaterialRippleLayout rippleUpdate;
    @Bind(R.id.ripple_delete)
    MaterialRippleLayout rippleDelete;
    @Bind(R.id.account_name)
    EditText accountNameET;
    @Bind(R.id.account_type)
    Spinner accountTypeSP;
    @Bind(R.id.account_number)
    EditText accountNumberET;
    @Bind(R.id.account_description)
    EditText accountDescriptionET;
    @Bind(R.id.account_label)
    TextView accountLabelTV;
    @Bind(R.id.update_save)
    ImageView updateSaveIV;
    public static final String ACCOUNT_ASSET = "Asset";
    public static final String ACCOUNT_LIABILITY = "Liability";
    public static final String ACCOUNT_EQUITY = "Equity";
    public static final String ACCOUNT_INCOME = "Income";
    public static final String ACCOUNT_EXPENSE = "Expense";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    int account_id;
    int parent_id;
    int position;
    int size;
    List<String> accountType;
    Accounts accounts;
    boolean isSave;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_view_fragment, container, false);
        account_id = getArguments().getInt("account_id");
        parent_id = getArguments().getInt("parent_id");
        accounts = Accounts.getAccountById(account_id);
        position = getArguments().getInt("position");
        size = getArguments().getInt("size");
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
        int typePosition = typeAdapter.getPosition(accountType.get(accounts.getAccount_category_id() - 1));
        accountTypeSP.setSelection(typePosition);
        accountNameET.setText(accounts.getAccount_name());
        accountLabelTV.setText(accounts.getAccount_name());
        accountNumberET.setText(""+accounts.getAccount_number());
        accountDescriptionET.setText(accounts.getDescription());
        disAbleEdit();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        init();
    }

    public void enableEdit(){
        accountNameET.setEnabled(true);
        accountDescriptionET.setEnabled(true);
        accountNumberET.setEnabled(true);
        accountTypeSP.setEnabled(true);
    }

    public void disAbleEdit(){
        accountNameET.setEnabled(false);
        accountDescriptionET.setEnabled(false);
        accountNumberET.setEnabled(false);
        accountTypeSP.setEnabled(false);
    }

    @OnClick(R.id.ripple_update)
    public void update(){
        if(isSave){
            if(isNetworkAvailable()){
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Updating account...");
                mProgressDialog.show();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Observable<AddAccountWrapper> observable = cloudCheetahAPIService.updateAccount(account_id, accountNameET.getText().toString(),
                        accountNumberET.getText().toString(),
                        accountDescriptionET.getText().toString(),
                        parent_id,
                        accountType.indexOf(accountTypeSP.getSelectedItem().toString()) + 1,
                        userName,
                        deviceid,
                        sessionKey,
                        AccountGeneral.METHOD_PUT);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AddAccountWrapper>() {
                            @Override
                            public void onCompleted() {
                                disAbleEdit();
                                isSave = false;
                                updateSaveIV.setImageResource(R.mipmap.ic_mode_edit_white_24dp);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                                MainActivity.popFragment();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("UpdateAccount", e.getMessage(), e);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onNext(AddAccountWrapper addAccountWrapper) {
                                if(addAccountWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                    Accounts responseAccounts = addAccountWrapper.getData();
                                    accounts.setAccount_category_id(responseAccounts.getAccount_category_id());
                                    accounts.setAccount_name(responseAccounts.getAccount_name());
                                    accounts.setAccount_number(responseAccounts.getAccount_number());
                                    accounts.setDescription(responseAccounts.getDescription());
                                    accounts.save();
                                    AccountsFragment.accountsAdapter.addItem(accounts);
                                }
                                else{
                                    Toast.makeText(getActivity(), "There is a problem deleting account. Please contact the administrator. Error code: "+addAccountWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(getActivity(), "Please connect to a network to update the account.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            enableEdit();
            isSave = true;
            updateSaveIV.setImageResource(R.mipmap.ic_save_white_24dp);
        }
    }

    @OnClick(R.id.ripple_delete)
    public void delete(){
        if(isNetworkAvailable()){
            final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                    .title("Delete")
                    .content("Are you sure you want to delete this account?")
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
                            mProgressDialog.setMessage("Delete account...");
                            mProgressDialog.show();
                            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            Observable<ResponseWrapper> observable = cloudCheetahAPIService.deleteAccount(account_id,
                                    userName,
                                    deviceid,
                                    sessionKey,
                                    AccountGeneral.METHOD_DELETE);
                            observable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .unsubscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<ResponseWrapper>() {
                                        @Override
                                        public void onCompleted() {
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                            MainActivity.popFragment();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e("DeleteAccount", e.getMessage(), e);
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onNext(ResponseWrapper responseWrapper) {
                                            if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                                Toast.makeText(getActivity(), "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                                                new Delete().from(Accounts.class).where("account_id = ?", account_id).execute();
                                                AccountsFragment.accountsAdapter.notifyItemRemoved(position);
                                                AccountsFragment.accountsAdapter.notifyItemRangeChanged(position, size);
                                            }
                                            else{
                                                Toast.makeText(getActivity(), "There is a problem deleting the account. Please contact the administrator. Error code: "+responseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
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
        else{
            Toast.makeText(getActivity(), "Please connect to a network to delete the account.", Toast.LENGTH_SHORT).show();
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
