package com.forateq.cloudcheetah.fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.RequestItems;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.Details;
import com.forateq.cloudcheetah.pojo.DetailsWrapper;
import com.forateq.cloudcheetah.pojo.PurchaseRequestItems;
import com.forateq.cloudcheetah.pojo.PurchaseRequestItemsWrapper;
import com.forateq.cloudcheetah.pojo.PurchaseRequestsResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.AddItemView;
import com.forateq.cloudcheetah.views.PurchaseRowView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
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
 * Created by Vallejos Family on 9/14/2016.
 */
public class AddPurchaseRequestFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.table_layout)
    LinearLayout tableLayout;
    @Bind(R.id.ripple_add)
    MaterialRippleLayout rippleAdd;
    @Bind(R.id.purchase_request_date)
    EditText dateET;
    @Bind(R.id.ref_no_et)
    EditText refNoET;
    @Bind(R.id.vendor_spinner)
    Spinner vendorSP;
    @Bind(R.id.remarks)
    EditText remarksET;
    @Bind(R.id.ripple_submit)
    MaterialRippleLayout submitRipple;
    @Bind(R.id.total_purchase_request_price)
    TextView totalPurchaseRequestPriceTV;
    Map<String, Resources> resourcesMap;
    int count;
    double totalPrice;
    PurchaseRequestItemsWrapper purchaseRequestItemsWrapper;
    List<PurchaseRequestItems> purchaseRequestItemsList;
    DetailsWrapper detailsWrapper;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_purchase_request_fragment, container, false);
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
        resourcesMap = getResourceMap();
        purchaseRequestItemsWrapper = new PurchaseRequestItemsWrapper();
        purchaseRequestItemsList = new ArrayList<>();
        List<Details> detailsList = new ArrayList<>();
        detailsWrapper = new DetailsWrapper();
        detailsWrapper.setDetails(detailsList);
        purchaseRequestItemsWrapper.setPurchaseRequestsList(purchaseRequestItemsList);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Vendors.getAllVendorName());
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendorSP.setAdapter(nameAdapter);
    }

    @OnClick(R.id.ripple_add)
    public void addItem(){
        final AddItemView addItemView = new AddItemView(getActivity());
        final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                .title("Add Item")
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
                        Resources resources = resourcesMap.get(addItemView.getItemNameSP().getSelectedItem().toString());
                        PurchaseRowView purchaseRowView = new PurchaseRowView(getActivity());
                        purchaseRowView.getResourceNameET().setText(resources.getName());
                        purchaseRowView.getResourceQuantityET().setText(addItemView.getQuantityET().getText().toString());
                        purchaseRowView.getResourcePriceET().setText(""+resources.getUnit_cost());
                        purchaseRowView.getTotalPriceET().setText(""+(Integer.parseInt(addItemView.getQuantityET().getText().toString()) * resources.getUnit_cost()));
                        if(count % 2 == 0){
                            purchaseRowView.getRowLayout().setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }
                        else{
                            purchaseRowView.getRowLayout().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        }
                        purchaseRowView.getRippleLayout().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        count++;
                        totalPrice += Double.parseDouble(purchaseRowView.getTotalPriceET().getText().toString());
                        totalPurchaseRequestPriceTV.setText(""+totalPrice);
                        PurchaseRequestItems purchaseRequestItems = new PurchaseRequestItems();
                        purchaseRequestItems.setResource_id(resources.getResource_id());
                        purchaseRequestItems.setResource_name(resources.getName());
                        purchaseRequestItems.setResource_price(resources.getSales_price());
                        purchaseRequestItems.setResource_quantity(Integer.parseInt(purchaseRowView.getResourceQuantityET().getText().toString()));
                        purchaseRequestItems.setResource_total_price(Double.parseDouble(purchaseRowView.getTotalPriceET().getText().toString()));
                        purchaseRequestItemsWrapper.getPurchaseRequestsList().add(purchaseRequestItems);
                        Details details = new Details();
                        details.setItem_id(resources.getResource_id());
                        details.setQty(Integer.parseInt(purchaseRowView.getResourceQuantityET().getText().toString()));
                        detailsWrapper.getDetails().add(details);
                        tableLayout.addView(purchaseRowView);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                });
        final MaterialDialog addNoteDialog = createNoteDialog.build();
        addNoteDialog.show();
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.purchase_request_date)
    public void setPurchaseDate(){
        setDate(dateET);
    }

    @OnClick(R.id.ripple_submit)
    public void submit(){
        Gson gson = new Gson();
        purchaseRequestItemsWrapper.setTotalPurchaseRequestPrice(totalPrice);
        String json = gson.toJson(purchaseRequestItemsWrapper);
        String details = gson.toJson(detailsWrapper);
        Log.e("Total", json);
        Log.e("Details", details);
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Submitting purchase request...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            int vendor_id = Vendors.getVendorId(vendorSP.getSelectedItem().toString());
            Observable<PurchaseRequestsResponseWrapper> observable = cloudCheetahAPIService.submitPurchaseRequest(userName,
                    deviceid,
                    sessionKey,
                    dateET.getText().toString(),
                    refNoET.getText().toString(),
                    vendor_id,
                    remarksET.getText().toString(),
                    details);
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
                            Log.e("Size", ""+RequestItems.getCount());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("AddAccount", e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(PurchaseRequestsResponseWrapper purchaseRequestsResponseWrapper) {
                            if(purchaseRequestsResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                for(RequestItems requestItems :purchaseRequestsResponseWrapper.getData().getDetails()){
                                    requestItems.save();
                                }
                                purchaseRequestsResponseWrapper.getData().save();
                            }
                            else{
                                Toast.makeText(getActivity(), "There is a problem submitting the purchase request please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "Please connect to a network to submit purchase request.", Toast.LENGTH_SHORT).show();
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

    public void setDate(final EditText editText){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        editText.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
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
