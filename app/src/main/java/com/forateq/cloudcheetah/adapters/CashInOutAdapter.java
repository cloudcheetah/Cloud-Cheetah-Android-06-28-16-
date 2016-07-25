package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.CashFlowViewFragment;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.CashInOut;
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.TaskCashInCashOut;
import com.forateq.cloudcheetah.pojo.CashFlow;
import com.forateq.cloudcheetah.pojo.SubmitProgressReportResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.TaskCashInCashOutView;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This adapter is use to display all the cashin and cashout of a particular task
 * Created by Vallejos Family on 6/14/2016.
 */
public class CashInOutAdapter extends RecyclerView.Adapter<CashInOutAdapter.ViewHolder>{

    private List<CashInOut> cashInCashOuts;
    private Context mContext;
    private String taskName;
    public static final String TAG = "MyTaskAdapter";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    public  static final String NO_IMAGE = "Attachment";
    Gson gson;

    /**
     * This is the class constructor used to create a new instance of this adapter
     * @param cashInCashOuts
     * @param context
     */
    public CashInOutAdapter(List<CashInOut> cashInCashOuts, Context context, String taskName) {
        this.cashInCashOuts = cashInCashOuts;
        this.mContext = context;
        this.taskName = taskName;
        gson = new Gson();
        ((CloudCheetahApp) ApplicationContext.get()).getNetworkComponent().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_cash_in_out, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CashInOut cashInOut = cashInCashOuts.get(i);
        viewHolder.cashInCashOutDateTV.setText(cashInOut.getTransaction_date());
        if(cashInOut.getType_id() == 1){
            viewHolder.typeTV.setText("Cash-In");
        }
        else{
            viewHolder.typeTV.setText("Cash-Out");
        }
        viewHolder.amountTV.setText(""+cashInOut.getAmount());
        viewHolder.cashInOutOfflineId.setText(""+cashInOut.getId());
        viewHolder.cashInOutId.setText(""+cashInOut.getCashInOutId());
        if(cashInOut.is_submitted()){
            viewHolder.cashFlowStatus.setText("Submitted");
            viewHolder.submitCashFlow.setVisibility(View.GONE);
        }
        else{
            viewHolder.cashFlowStatus.setText("Not yet submitted");
        }
    }

    /**
     * This method us used to add new item in the cashin and cashout arraylist
     * @param cashInOut
     */
    public void addItem(CashInOut cashInOut){
        cashInCashOuts.add(cashInOut);
        notifyDataSetChanged();
    }

    /**
     * This method is used to clear all the items in the cashin cashout arraylist
     */
    public void clearItems(){
        cashInCashOuts.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cashInCashOuts == null ? 0 : cashInCashOuts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView cashInCashOutDateTV;
        public TextView typeTV;
        public TextView amountTV;
        public TextView cashInOutOfflineId;
        public TextView cashInOutId;
        public TextView cashFlowStatus;
        public ImageView submitCashFlow;
        public MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            cashInCashOutDateTV = (TextView) itemView.findViewById(R.id.cash_in_out_date);
            typeTV = (TextView) itemView.findViewById(R.id.type);
            amountTV = (TextView) itemView.findViewById(R.id.amount);
            cashInOutOfflineId = (TextView) itemView.findViewById(R.id.cash_in_out_offline_id);
            cashInOutId = (TextView) itemView.findViewById(R.id.cash_in_out_id);
            cashFlowStatus = (TextView) itemView.findViewById(R.id.cash_flow_status);
            submitCashFlow = (ImageView) itemView.findViewById(R.id.submit_cash_flow);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("cash_flow_id", cashInOutId.getText().toString());
                    bundle.putString("cash_flow_offline_id", cashInOutOfflineId.getText().toString());
                    bundle.putString("task_name", taskName);
                    CashFlowViewFragment cashFlowViewFragment = new CashFlowViewFragment();
                    cashFlowViewFragment.setArguments(bundle);
                    MainActivity.replaceFragment(cashFlowViewFragment, "CashInOutAdapter");
                }
            });
            submitCashFlow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()){
                        Toast.makeText(ApplicationContext.get(), "Submitting cash-in/cash-out...", Toast.LENGTH_SHORT).show();
                        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                        String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                        String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                        String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        final CashInOut cashInOut = CashInOut.getCashInOut(Long.parseLong(cashInOutOfflineId.getText().toString()));
                        CashFlow cashFlow = new CashFlow();
                        cashFlow.setAccount_id(cashInOut.getAccount_id());
                        cashFlow.setAmount(cashInOut.getAmount());
                        cashFlow.setDescription(cashInOut.getDescription());
                        cashFlow.setInvoice_no(cashInOut.getInvoice_no());
                        cashFlow.setItem_id(cashInOut.getItem_id());
                        cashFlow.setLocation(cashInOut.getLocation());
                        cashFlow.setPayer_id(cashInOut.getPayer_id());
                        cashFlow.setQty(cashInOut.getQty());
                        cashFlow.setReceipt_no(cashInOut.getReceipt_no());
                        cashFlow.setTransaction_date(cashInOut.getTransaction_date());
                        cashFlow.setType_id(cashInOut.getType_id());
                        cashFlow.setTask_id(cashInOut.getTask_id());
                        cashFlow.setTask_offline_id(cashInOut.getTask_offline_id());
                        String json = gson.toJson(cashFlow);
                        MultipartBuilder multipartBuilder = new MultipartBuilder();
                        Log.e("Attachment 1", cashInOut.getAttachment_1());
                        if(!cashInOut.getAttachment_1().equals(NO_IMAGE) && cashInOut.getAttachment_1() != null){
                            File file = new File(cashInOut.getAttachment_1());
                            RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                            multipartBuilder.addFormDataPart("image_1", file.getName(), image);
                        }
                        Log.e("Attachment 2", cashInOut.getAttachment_2());
                        if(!cashInOut.getAttachment_2().equals(NO_IMAGE) && cashInOut.getAttachment_2() != null){
                            File file = new File(cashInOut.getAttachment_2());
                            RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                            multipartBuilder.addFormDataPart("image_2", file.getName(), image);
                        }
                        Log.e("Attachment 3", cashInOut.getAttachment_3());
                        if(!cashInOut.getAttachment_3().equals(NO_IMAGE) && cashInOut.getAttachment_3() != null){
                            File file = new File(cashInOut.getAttachment_3());
                            RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                            multipartBuilder.addFormDataPart("image_3", file.getName(), image);
                        }
                        RequestBody fileRequestBody = multipartBuilder.build();
                        Observable<SubmitProgressReportResponseWrapper> observable = cloudCheetahAPIService.addCashFlow(userName, deviceid, sessionKey, json, fileRequestBody);
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<SubmitProgressReportResponseWrapper>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e("AddCashFlow", e.getMessage(), e);

                                    }

                                    @Override
                                    public void onNext(SubmitProgressReportResponseWrapper submitProgressReportResponseWrapper) {
                                        if(submitProgressReportResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                            Toast.makeText(ApplicationContext.get(), "Cash-In/Cash-Out sumitted successfully.", Toast.LENGTH_SHORT).show();
                                            cashInOut.setAttachment_1(submitProgressReportResponseWrapper.getData().getImage_1());
                                            cashInOut.setAttachment_2(submitProgressReportResponseWrapper.getData().getImage_2());
                                            cashInOut.setAttachment_3(submitProgressReportResponseWrapper.getData().getImage_3());
                                            cashInOut.setIs_submitted(true);
                                            cashInOut.save();
                                            notifyDataSetChanged();
                                        }
                                        else{
                                            Toast.makeText(ApplicationContext.get(), "There is a problem submitting the Cash-In/Cash-Out please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(ApplicationContext.get(), "Please connect to a network to submit cash-in/cash-out.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    /**
     * Checks if there is a network available before login
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ApplicationContext.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
