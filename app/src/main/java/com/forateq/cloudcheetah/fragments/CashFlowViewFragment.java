package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.CashInOut;
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PC1 on 7/15/2016.
 */
public class CashFlowViewFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.task_name)
    EditText taskNameET;
    @Bind(R.id.transaction_date)
    EditText transactionDateET;
    @Bind(R.id.location)
    EditText locationET;
    @Bind(R.id.description)
    EditText descriptionET;
    @Bind(R.id.amount)
    EditText amountET;
    @Bind(R.id.invoice_number)
    EditText invoiceNumberET;
    @Bind(R.id.payer_label)
    TextView payersTV;
    @Bind(R.id.payers)
    EditText payersET;
    @Bind(R.id.payees_label)
    TextView payeesTV;
    @Bind(R.id.payees)
    EditText payeesET;
    @Bind(R.id.quantity)
    EditText quantityET;
    @Bind(R.id.receipt_number)
    EditText receiptNumberET;
    @Bind(R.id.accounts)
    EditText accountsET;
    @Bind(R.id.items)
    EditText itemsET;
    @Bind(R.id.type)
    EditText typeET;
    @Bind(R.id.attachment_one)
    TextView attachment1TV;
    @Bind(R.id.attachment_two)
    TextView attachment2TV;
    @Bind(R.id.attachment_three)
    TextView attachment3TV;
    int cashFlowId;
    long cashFlowOfflineId;
    String taskName;
    CashInOut cashInOut;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cash_flow_view_fragment, container, false);
        cashFlowId = Integer.parseInt(getArguments().getString("cash_flow_id"));
        cashFlowOfflineId = Long.parseLong(getArguments().getString("cash_flow_offline_id"));
        taskName = getArguments().getString("task_name");
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void init(){
        cashInOut = CashInOut.getCashInOut(cashFlowOfflineId);
        taskNameET.setText(taskName);
        transactionDateET.setText(cashInOut.getTransaction_date());
        locationET.setText(cashInOut.getLocation());
        descriptionET.setText(cashInOut.getDescription());
        amountET.setText(""+cashInOut.getAmount());
        invoiceNumberET.setText(""+cashInOut.getInvoice_no());
        quantityET.setText(""+cashInOut.getQty());
        receiptNumberET.setText(""+cashInOut.getReceipt_no());
        accountsET.setText(Accounts.getAccountName(cashInOut.getAccount_id()));
        itemsET.setText(Resources.getResource(cashInOut.getItem_id()).getName());
        if(cashInOut.getType_id() == 1){
            typeET.setText("Cash-In");
            payersTV.setVisibility(View.VISIBLE);
            payersET.setVisibility(View.VISIBLE);
            payersET.setText(Customers.getCustomerName(cashInOut.getPayer_id()));
        }
        else{
            typeET.setText("Cash-Out");
            payeesTV.setVisibility(View.VISIBLE);
            payeesET.setVisibility(View.VISIBLE);
            payeesET.setText(Vendors.getVendorName(cashInOut.getPayer_id()));
        }
        attachment1TV.setText(cashInOut.getAttachment_1());
        attachment2TV.setText(cashInOut.getAttachment_2());
        attachment3TV.setText(cashInOut.getAttachment_3());
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.attachment_one)
    public void showAttachment1(){
        if(!attachment1TV.getText().toString().equals("")){
            if(cashInOut.is_submitted()){
                Log.e("URL", cashInOut.getAttachment_1());
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load("http://"+cashInOut.getAttachment_1()).placeholder( R.drawable.progress_animation ).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 1")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
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
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load(new File(attachment1TV.getText().toString())).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 1")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        });
                final MaterialDialog addNoteDialog = createNoteDialog.build();
                addNoteDialog.show();
            }
        }
    }

    @OnClick(R.id.attachment_two)
    public void showAttachment2(){
        if(!attachment2TV.getText().toString().equals("")){
            if(cashInOut.is_submitted()){
                Log.e("URL", cashInOut.getAttachment_2());
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load("http://"+cashInOut.getAttachment_2()).placeholder( R.drawable.progress_animation ).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 2")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
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
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load(new File(attachment2TV.getText().toString())).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 2")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        });
                final MaterialDialog addNoteDialog = createNoteDialog.build();
                addNoteDialog.show();
            }
        }
    }

    @OnClick(R.id.attachment_three)
    public void showAttachment3(){
        if(!attachment3TV.getText().toString().equals("")){
            if(cashInOut.is_submitted()){
                final ImageView imageView = new ImageView(getActivity());
                Log.e("URL", cashInOut.getAttachment_3());
                Picasso.with(ApplicationContext.get()).load("http://"+cashInOut.getAttachment_3()).placeholder( R.drawable.progress_animation ).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 3")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
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
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load(new File(attachment3TV.getText().toString())).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 3")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        });
                final MaterialDialog addNoteDialog = createNoteDialog.build();
                addNoteDialog.show();
            }
        }
    }


}
