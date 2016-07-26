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
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.AddCustomerWrapper;
import com.forateq.cloudcheetah.pojo.AddVendorResponseWrapper;
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
 * Created by PC1 on 7/26/2016.
 */
public class VendorViewFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.ripple_update)
    MaterialRippleLayout rippleUpdate;
    @Bind(R.id.ripple_delete)
    MaterialRippleLayout rippleDelete;
    @Bind(R.id.vendor_label)
    TextView vendorLabelTV;
    @Bind(R.id.update_save)
    ImageView updateSaveIV;
    int vendor_id;
    int position;
    int size;
    boolean isSave;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
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
    Vendors vendors;
    List<String> vendorType;
    int is_company;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vendor_view_fragment, container, false);
        vendor_id = getArguments().getInt("vendor_id");
        position = getArguments().getInt("position");
        size = getArguments().getInt("size");
        vendors = Vendors.getVendorById(vendor_id);
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
        vendorNameET.setText(vendors.getName());
        vendorAddressET.setText(vendors.getAddress());
        vendorType = new ArrayList<>();
        vendorType.add("Yes");
        vendorType.add("No");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, vendorType);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorTypeSP.setAdapter(typeAdapter);
        if(vendors.is_company()){
            vendorTypeSP.setSelection(0);
        }
        else{
            vendorTypeSP.setSelection(1);
        }
        vendorDescriptionET.setText(vendors.getDescription());
        vendorContactNoET.setText(vendors.getContact_no());
        vendorContactPersonET.setText(vendors.getContact_person());
        vendorEmailAddressET.setText(vendors.getEmail_address());
        vendorNotesET.setText(vendors.getNotes());
        vendorLabelTV.setText(vendors.getName());
        disAbleEdit();
    }

    public void enableEdit(){
        vendorNameET.setEnabled(true);
        vendorAddressET.setEnabled(true);
        vendorTypeSP.setEnabled(true);
        vendorDescriptionET.setEnabled(true);
        vendorContactNoET.setEnabled(true);
        vendorContactPersonET.setEnabled(true);
        vendorEmailAddressET.setEnabled(true);
        vendorNotesET.setEnabled(true);
    }

    public void disAbleEdit(){
        vendorNameET.setEnabled(false);
        vendorAddressET.setEnabled(false);
        vendorTypeSP.setEnabled(false);
        vendorDescriptionET.setEnabled(false);
        vendorContactNoET.setEnabled(false);
        vendorContactPersonET.setEnabled(false);
        vendorEmailAddressET.setEnabled(false);
        vendorNotesET.setEnabled(false);
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.ripple_update)
    public void update() {
        if (isSave) {
            if (isNetworkAvailable()) {
                if(vendorTypeSP.getSelectedItem().toString().equals("Yes")){
                    is_company = 1;
                }
                else{
                    is_company = 0;
                }
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Updating vendor...");
                mProgressDialog.show();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                Observable<AddVendorResponseWrapper> observable = cloudCheetahAPIService.updateVendor(vendor_id,
                        vendorNameET.getText().toString(),
                        vendorAddressET.getText().toString(),
                        is_company,
                        vendorDescriptionET.getText().toString(),
                        vendorContactNoET.getText().toString(),
                        vendorContactPersonET.getText().toString(),
                        vendorEmailAddressET.getText().toString(),
                        vendorNotesET.getText().toString(),
                        userName,
                        deviceid,
                        sessionKey,
                        AccountGeneral.METHOD_PUT);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<AddVendorResponseWrapper>() {
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
                                Log.e("UpdateVendor", e.getMessage(), e);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onNext(AddVendorResponseWrapper addVendorResponseWrapper) {
                                Vendors newVendors = addVendorResponseWrapper.getData();
                                vendors.setName(newVendors.getName());
                                vendors.setNotes(newVendors.getNotes());
                                vendors.setAddress(newVendors.getAddress());
                                vendors.setDescription(newVendors.getDescription());
                                vendors.setContact_no(newVendors.getContact_no());
                                vendors.setContact_person(newVendors.getContact_person());
                                vendors.setEmail_address(newVendors.getEmail_address());
                                vendors.setIs_company(newVendors.is_company());
                                vendors.save();
                                VendorsFragment.vendorsAdapter.notifyDataSetChanged();
                            }
                        });

            } else {
                Toast.makeText(getActivity(), "Please connect to a network to update vendor.", Toast.LENGTH_SHORT).show();
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
                    .content("Are you sure you want to delete this vendor?")
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
                                mProgressDialog.setMessage("Deleting vendor...");
                                mProgressDialog.show();
                                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                                String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                Observable<ResponseWrapper> observable = cloudCheetahAPIService.deleteVendor(vendor_id,
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
                                                Log.e("DeleteVendor", e.getMessage(), e);
                                                if(mProgressDialog.isShowing()){
                                                    mProgressDialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onNext(ResponseWrapper responseWrapper) {
                                                if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                                    Toast.makeText(getActivity(), "Vendor deleted successfully.", Toast.LENGTH_SHORT).show();
                                                    new Delete().from(Vendors.class).where("vendor_id = ?", vendor_id).execute();
                                                    VendorsFragment.vendorsAdapter.notifyItemRemoved(position);
                                                    VendorsFragment.vendorsAdapter.notifyItemRangeChanged(position, size);
                                                }
                                                else{
                                                    Toast.makeText(getActivity(), "There is a problem deleting the vendor. Please contact the administrator. Error code: "+responseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(getActivity(), "Please connect to the internet to delete vendor.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please connect to a network to delete vendor.", Toast.LENGTH_SHORT).show();
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
