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
import android.widget.Spinner;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.AddVendorResponseWrapper;
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
 * Created by PC1 on 7/26/2016.
 */
public class AddVendorFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.vendor_name)
    EditText vendorNameET;
    @Bind(R.id.vendor_address)
    EditText vendorAddressET;
    @Bind(R.id.vendor_type)
    Spinner vendorTypeSP;
    @Bind(R.id.vendor_description)
    EditText vendorDescriptionET;
    @Bind(R.id.vendor_contact_no)
    EditText vendorContactNoET;
    @Bind(R.id.vendor_contact_person)
    EditText vendorContactPersonET;
    @Bind(R.id.vendor_email_address)
    EditText vendorEmailAddressET;
    @Bind(R.id.vendor_notes)
    EditText vendorNotesET;
    @Bind(R.id.add_vendor)
    Button addVendorBT;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    List<String> vendorType;
    int is_company;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_vendor_fragment, container, false);
        return v;
    }

    public void init(){
        vendorType = new ArrayList<>();
        vendorType.add("Yes");
        vendorType.add("No");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, vendorType);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorTypeSP.setAdapter(typeAdapter);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        init();
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.add_vendor)
    public void addVendor(){
        if(isNetworkAvailable()){
            if(vendorTypeSP.getSelectedItem().toString().equals("Yes")){
                is_company = 1;
            }
            else{
                is_company = 0;
            }
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Adding vendor...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Observable<AddVendorResponseWrapper> observable = cloudCheetahAPIService.addVendor(vendorNameET.getText().toString(),
                    vendorAddressET.getText().toString(),
                    is_company,
                    vendorDescriptionET.getText().toString(),
                    vendorContactNoET.getText().toString(),
                    vendorContactPersonET.getText().toString(),
                    vendorEmailAddressET.getText().toString(),
                    vendorNotesET.getText().toString(),
                    userName,
                    deviceid,
                    sessionKey);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<AddVendorResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            MainActivity.popFragment();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("AddVendor", e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(AddVendorResponseWrapper addVendorResponseWrapper) {
                            Vendors vendors = addVendorResponseWrapper.getData();
                            vendors.save();
                            VendorsFragment.vendorsAdapter.addItem(vendors);
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "Please connect to a network to add vendor.", Toast.LENGTH_SHORT).show();
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
