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
import android.widget.LinearLayout;
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
import com.forateq.cloudcheetah.models.PurchaseRequests;
import com.forateq.cloudcheetah.models.RequestItems;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.Details;
import com.forateq.cloudcheetah.pojo.DetailsWrapper;
import com.forateq.cloudcheetah.pojo.PurchaseRequestItems;
import com.forateq.cloudcheetah.pojo.PurchaseRequestItemsWrapper;
import com.forateq.cloudcheetah.pojo.PurchaseRequestsResponseWrapper;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.ActionView;
import com.forateq.cloudcheetah.views.AddItemView;
import com.forateq.cloudcheetah.views.PurchaseRowView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Vallejos Family on 9/16/2016.
 */
public class PurchaseRequestViewFragment extends Fragment {

    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.table_layout)
    LinearLayout tableLayout;
    @Bind(R.id.purchase_request_date)
    EditText dateET;
    @Bind(R.id.ref_no_et)
    EditText refNoET;
    @Bind(R.id.vendor_spinner)
    Spinner vendorSP;
    @Bind(R.id.remarks)
    EditText remarksET;
    @Bind(R.id.pr_ref_no)
    TextView refNoTV;
    @Bind(R.id.total_purchase_request_price)
    TextView totalPurchaseRequestPriceTV;
    @Bind(R.id.update_save)
    ImageView updateSaveIV;
    int count;
    double totalPrice;
    PurchaseRequestItemsWrapper purchaseRequestItemsWrapper;
    List<PurchaseRequestItems> purchaseRequestItemsList;
    Map<String, Resources> resourcesMap;
    DetailsWrapper detailsWrapper;
    int purchase_request_id;
    PurchaseRequests purchaseRequests;
    boolean isEditEnabled;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.purchase_request_view_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        purchase_request_id = getArguments().getInt("purchase_request_id");
        purchaseRequests = PurchaseRequests.getPurchaseRequestById(purchase_request_id);
        init();
    }

    public void init(){
        disableEdit();
        resourcesMap = getResourceMap();
        purchaseRequestItemsWrapper = new PurchaseRequestItemsWrapper();
        purchaseRequestItemsList = new ArrayList<>();
        List<Details> detailsList = new ArrayList<>();
        detailsWrapper = new DetailsWrapper();
        detailsWrapper.setDetails(detailsList);
        purchaseRequestItemsWrapper.setPurchaseRequestsList(purchaseRequestItemsList);
        refNoTV.setText(purchaseRequests.getTrans_no());
        dateET.setText(purchaseRequests.getTrans_date());
        refNoET.setText(purchaseRequests.getRef_no());
        remarksET.setText(purchaseRequests.getRemarks());
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Vendors.getAllVendorName());
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSP.setAdapter(nameAdapter);
        int vendorPosition = nameAdapter.getPosition(Vendors.getVendorName(purchaseRequests.getVendor_id()));
        vendorSP.setSelection(vendorPosition);
        for(RequestItems requestItems : RequestItems.getItems(purchase_request_id)){
            final PurchaseRowView purchaseRowView = new PurchaseRowView(getActivity());
            purchaseRowView.getResourcePriceET().setText(""+requestItems.getCost_price());
            purchaseRowView.getResourceNameET().setText(""+requestItems.getItem_name());
            purchaseRowView.getResourceQuantityET().setText(""+requestItems.getQty());
            purchaseRowView.getTotalPriceET().setText(""+requestItems.getNet_amount());
            if(count % 2 == 0){
                purchaseRowView.getRowLayout().setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            else{
                purchaseRowView.getRowLayout().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            count++;
            totalPrice += requestItems.getNet_amount();
            tableLayout.addView(purchaseRowView);
            final Details details = new Details();
            details.setItem_id(requestItems.getItem_id());
            details.setQty(Integer.parseInt(purchaseRowView.getResourceQuantityET().getText().toString()));
            detailsWrapper.getDetails().add(details);
            purchaseRowView.getRippleLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isEditEnabled){
                        final ActionView actionView = new ActionView(getActivity());
                        final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                                .titleColorRes(R.color.colorText)
                                .backgroundColorRes(R.color.colorPrimary)
                                .widgetColorRes(R.color.colorText)
                                .customView(actionView, true);
                        final MaterialDialog addNoteDialog = createNoteDialog.build();
                        actionView.getRippleDelete().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tableLayout.removeView(purchaseRowView);
                                detailsWrapper.getDetails().remove(details);
                                totalPrice -= Double.parseDouble(purchaseRowView.getTotalPriceET().getText().toString());
                                totalPurchaseRequestPriceTV.setText(""+totalPrice);
                                addNoteDialog.dismiss();
                            }
                        });
                        actionView.getRippleEdit().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AddItemView addItemView = new AddItemView(getActivity(), details);
                                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                                        .title("Edit Item")
                                        .titleColorRes(R.color.colorText)
                                        .backgroundColorRes(R.color.colorPrimary)
                                        .widgetColorRes(R.color.colorText)
                                        .customView(addItemView, true)
                                        .positiveText("Ok")
                                        .positiveColorRes(R.color.colorText)
                                        .negativeText("Cancel")
                                        .negativeColorRes(R.color.colorText)
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog dialog) {
                                                super.onPositive(dialog);
                                                totalPrice -= Double.parseDouble(purchaseRowView.getTotalPriceET().getText().toString());
                                                Resources resources = resourcesMap.get(addItemView.getItemNameSP().getSelectedItem().toString());
                                                details.setQty(Integer.parseInt(addItemView.getQuantityET().getText().toString()));
                                                details.setItem_id(resources.getResource_id());
                                                purchaseRowView.getResourceNameET().setText(resources.getName());
                                                purchaseRowView.getResourceQuantityET().setText(addItemView.getQuantityET().getText().toString());
                                                purchaseRowView.getResourcePriceET().setText(""+resources.getUnit_cost());
                                                purchaseRowView.getTotalPriceET().setText(""+(Integer.parseInt(addItemView.getQuantityET().getText().toString()) * resources.getUnit_cost()));
                                                totalPrice += Double.parseDouble(purchaseRowView.getTotalPriceET().getText().toString());
                                                totalPurchaseRequestPriceTV.setText(""+totalPrice);
                                            }

                                            @Override
                                            public void onNegative(MaterialDialog dialog) {
                                                super.onNegative(dialog);
                                            }
                                        });
                                final MaterialDialog addNoteDialog = createNoteDialog.build();
                                addNoteDialog.show();
                            }
                        });
                        addNoteDialog.show();
                    }
                }
            });
        }
        totalPurchaseRequestPriceTV.setText(""+totalPrice);
    }

    public void enableEdit(){
        dateET.setEnabled(true);
        refNoET.setEnabled(true);
        remarksET.setEnabled(true);
        vendorSP.setEnabled(true);
        tableLayout.setEnabled(true);
        isEditEnabled = true;
        updateSaveIV.setImageResource(R.mipmap.ic_save_white_24dp);
    }

    public void disableEdit(){
        dateET.setEnabled(false);
        refNoET.setEnabled(false);
        remarksET.setEnabled(false);
        vendorSP.setEnabled(false);
        tableLayout.setEnabled(false);
        isEditEnabled = false;
        updateSaveIV.setImageResource(R.mipmap.ic_mode_edit_white_24dp);
    }

    @OnClick(R.id.ripple_update)
    public void update(){
        if(isEditEnabled){
            Log.e("Saved", "saved");
            if(isNetworkAvailable()){
                Gson gson = new Gson();
                String details = gson.toJson(detailsWrapper);
                Log.e("Details", details);
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Updating purchase request...");
                mProgressDialog.show();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                int vendor_id = Vendors.getVendorId(vendorSP.getSelectedItem().toString());
                Observable<PurchaseRequestsResponseWrapper> observable = cloudCheetahAPIService.updatePurchaseRequest(purchase_request_id,
                        userName,
                        deviceid,
                        sessionKey,
                        dateET.getText().toString(),
                        refNoET.getText().toString(),
                        vendor_id,
                        remarksET.getText().toString(),
                        details,
                        AccountGeneral.METHOD_PUT);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<PurchaseRequestsResponseWrapper>() {
                            @Override
                            public void onCompleted() {
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                                MainActivity.popFragment();
                                disableEdit();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("UpdateRequest", e.getMessage(), e);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onNext(PurchaseRequestsResponseWrapper purchaseRequestsResponseWrapper) {
                                if(purchaseRequestsResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                    new Delete().from(PurchaseRequests.class).where("purchase_request_id = ?", purchase_request_id).execute();
                                    new Delete().from(RequestItems.class).where("master_id = ?", purchase_request_id).execute();
                                    for(RequestItems requestItems :purchaseRequestsResponseWrapper.getData().getDetails()){
                                        requestItems.save();
                                    }
                                    purchaseRequestsResponseWrapper.getData().save();
                                }
                                else{
                                    Toast.makeText(getActivity(), "There is a problem updating the purchase request. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(getActivity(), "Please connect to a network to update the purchase request.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            enableEdit();
            Log.e("Update", "update");
        }
    }

    public Map<String, Resources> getResourceMap(){
        List<Resources> listResources = Resources.getAllResources();
        Map<String, Resources> resourcesMap = new HashMap<>();
        for(Resources resources : listResources){
            resourcesMap.put(resources.getName(), resources);
        }
        return  resourcesMap;
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.ripple_copy)
    public void copy(){
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_copy", true);
        bundle.putInt("purchase_request_id", purchase_request_id);
        AddPurchaseRequestFragment addPurchaseRequestFragment = new AddPurchaseRequestFragment();
        addPurchaseRequestFragment.setArguments(bundle);
        MainActivity.replaceFragment(addPurchaseRequestFragment, "PurchaseView");
    }

    @OnClick(R.id.ripple_approve)
    public void approve(){
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Approving purchase request...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Observable<ResponseWrapper> observable = cloudCheetahAPIService.approvePurchaseRequest(userName, deviceid, sessionKey, purchase_request_id);
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
                            Log.e("ApproveRequest", e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(ResponseWrapper responseWrapper) {
                            if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                Toast.makeText(getActivity(), "Purchase request successfuly approved.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "There is a problem approving the purchase request. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "Please connect to a network to approve this purchase request.", Toast.LENGTH_SHORT).show();
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
