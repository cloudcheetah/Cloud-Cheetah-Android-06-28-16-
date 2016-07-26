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
import android.widget.EditText;
import android.widget.ImageView;
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
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.pojo.AddCustomerWrapper;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
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
 * Created by PC1 on 7/26/2016.
 */
public class CustomerViewFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.ripple_update)
    MaterialRippleLayout rippleUpdate;
    @Bind(R.id.ripple_delete)
    MaterialRippleLayout rippleDelete;
    @Bind(R.id.customer_label)
    TextView customerLabelTV;
    @Bind(R.id.update_save)
    ImageView updateSaveIV;
    @Bind(R.id.customer_name)
    EditText customerNameET;
    @Bind(R.id.customer_address)
    EditText customerAddressET;
    @Bind(R.id.customer_notes)
    EditText customerNotesET;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    int customer_id;
    int position;
    int size;
    boolean isSave;
    Customers customers;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.customer_view_fragment, container, false);
        customer_id = getArguments().getInt("customer_id");
        position = getArguments().getInt("position");
        size = getArguments().getInt("size");
        customers = Customers.getCustomerById(customer_id);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        init();
    }

    public void init(){
        customerNameET.setText(customers.getName());
        customerAddressET.setText(customers.getAddress());
        customerNotesET.setText(customers.getNotes());
        customerLabelTV.setText(customers.getName());
        disAbleEdit();
    }

    public void enableEdit(){
        customerNameET.setEnabled(true);
        customerAddressET.setEnabled(true);
        customerNotesET.setEnabled(true);
    }

    public void disAbleEdit(){
        customerNameET.setEnabled(false);
        customerAddressET.setEnabled(false);
        customerNotesET.setEnabled(false);
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }


    @OnClick(R.id.ripple_update)
    public void update() {
        if (isSave) {
            if (isNetworkAvailable()) {
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Updating customer...");
                mProgressDialog.show();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Observable<AddCustomerWrapper> observable = cloudCheetahAPIService.updateCustomer(customer_id,
                        customerNameET.getText().toString(),
                        customerAddressET.getText().toString(),
                        customerNotesET.getText().toString(),
                        userName,
                        deviceid,
                        sessionKey,
                        AccountGeneral.METHOD_PUT);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AddCustomerWrapper>() {
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
                                Log.e("UpdateCustomer", e.getMessage(), e);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onNext(AddCustomerWrapper addCustomerWrapper) {
                                if(addCustomerWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                    Customers newCustomer = addCustomerWrapper.getData();
                                    customers.setName(newCustomer.getName());
                                    customers.setAddress(newCustomer.getAddress());
                                    customers.setNotes(newCustomer.getNotes());
                                    customers.save();
                                    CustomersFragment.customersAdapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(getActivity(), "There is a problem updating customer. Please contact the administrator. Error code: "+addCustomerWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else {
                Toast.makeText(getActivity(), "Please connect to a network to update customer.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Log.e("Clicked", "Clicked");
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
                    .content("Are you sure you want to delete this customer?")
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
                            if(isNetworkAvailable()){
                                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setMessage("Deleting customer...");
                                mProgressDialog.show();
                                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                                String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                Observable<ResponseWrapper> observable = cloudCheetahAPIService.deleteCustomer(customer_id,
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
                                                Log.e("DeleteCustomer", e.getMessage(), e);
                                                if(mProgressDialog.isShowing()){
                                                    mProgressDialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onNext(ResponseWrapper responseWrapper) {
                                                if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                                    Toast.makeText(getActivity(), "Customer deleted successfully.", Toast.LENGTH_SHORT).show();
                                                    new Delete().from(Customers.class).where("customer_id = ?", customer_id).execute();
                                                    CustomersFragment.customersAdapter.notifyItemRemoved(position);
                                                    CustomersFragment.customersAdapter.notifyItemRangeChanged(position, size);
                                                }
                                                else{
                                                    Toast.makeText(getActivity(), "There is a problem deleting the customer. Please contact the administrator. Error code: "+responseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(getActivity(), "Please connect to the internet to delete customer.", Toast.LENGTH_SHORT).show();
                            }
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
            Toast.makeText(getActivity(), "Please connect to a network to delete customer.", Toast.LENGTH_SHORT).show();
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

}
