package com.forateq.cloudcheetah.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CameraActivity;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.CashInOut;
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.TaskCashInCashOut;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.AddResourceWrapper;
import com.forateq.cloudcheetah.pojo.CashFlow;
import com.forateq.cloudcheetah.pojo.ProcessCashIn;
import com.forateq.cloudcheetah.pojo.SubmitProgressReportResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.TaskCashInCashOutView;
import com.forateq.cloudcheetah.views.TaskProgressReportsView;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by PC1 on 7/15/2016.
 */
public class AddCashFlowFragment extends Fragment {

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
    @Bind(R.id.payers)
    Spinner payersSP;
    @Bind(R.id.payer_label)
    TextView payerTV;
    @Bind(R.id.payees)
    Spinner payeesSP;
    @Bind(R.id.payee_label)
    TextView payeeTV;
    @Bind(R.id.quantity)
    EditText quantityET;
    @Bind(R.id.receipt_number)
    EditText receiptNumberET;
    @Bind(R.id.accounts)
    Spinner accountsSP;
    @Bind(R.id.type)
    Spinner typeSP;
    @Bind(R.id.items)
    Spinner itemsSP;
    @Bind(R.id.attachment_one)
    TextView attachmentTV1;
    @Bind(R.id.attachment_two)
    TextView attachmentTV2;
    @Bind(R.id.attachment_three)
    TextView attachmentTV3;
    @Bind(R.id.add_attachment_1)
    Button attachmentBT1;
    @Bind(R.id.add_attachment_2)
    Button attachmentBT2;
    @Bind(R.id.add_attachment_3)
    Button attachmentBT3;
    @Bind(R.id.add_cash_in_out)
    Button addCashFlowBT;
    public static final String TEXT_VIEW_ONE = "textview_1";
    public static final String TEXT_VIEW_TWO = "textview_2";
    public static final String TEXT_VIEW_THREE = "textview_3";
    Map<String, TextView> textViewMap;
    Map<String, Integer> typeMap;
    static final int REQUEST_TAKE_PHOTO = 11111;
    String taskName;
    long task_offline_id;
    int task_id;
    Gson gson;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    String currentPhotoPath;
    String currentAttachment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_cash_flow_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        List<String> typeList = new ArrayList<>();
        typeList.add("Cash-In");
        typeList.add("Cash-Out");
        typeMap = new HashMap<>();
        typeMap.put("Cash-In", 1);
        typeMap.put("Cash-Out", 2);
        taskName = getArguments().getString("task_name");
        task_id = Integer.parseInt(getArguments().getString("task_id"));
        task_offline_id = Long.parseLong(getArguments().getString("task_offline_id"));
        ArrayAdapter<String> typeAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, typeList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSP.setAdapter(typeAdapter);
        ArrayAdapter<String> accountsAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Accounts.getAccountsName());
        accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountsSP.setAdapter(accountsAdapter);
        ArrayAdapter<String> payersAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Customers.getCustomersName());
        payersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payersSP.setAdapter(payersAdapter);
        ArrayAdapter<String> payeesAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Vendors.getAllVendorName());
        payeesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payeesSP.setAdapter(payeesAdapter);
        ArrayAdapter itemsAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Resources.getAllResourcesList());
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsSP.setAdapter(itemsAdapter);
        taskNameET.setText(taskName);
        gson = new Gson();
        textViewMap = new HashMap<>();
        textViewMap.put(TEXT_VIEW_ONE, attachmentTV1);
        textViewMap.put(TEXT_VIEW_TWO, attachmentTV2);
        textViewMap.put(TEXT_VIEW_THREE, attachmentTV3);
        return v;
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.transaction_date)
    public void setReportDate(){
        setDate(transactionDateET);
    }

    @OnItemSelected(R.id.type)
    void onItemSelected(int position) {
        if(position == 0){
            payeeTV.setVisibility(View.GONE);
            payeesSP.setVisibility(View.GONE);
            payerTV.setVisibility(View.VISIBLE);
            payersSP.setVisibility(View.VISIBLE);
        }
        else{
            payeeTV.setVisibility(View.VISIBLE);
            payeesSP.setVisibility(View.VISIBLE);
            payerTV.setVisibility(View.GONE);
            payersSP.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.add_cash_in_out)
    public void addCashFlow(){
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Submitting cash-in/cash-out report...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            CashFlow cashFlow = new CashFlow();
            cashFlow.setAccount_id(Accounts.getAccountId(accountsSP.getSelectedItem().toString()));
            cashFlow.setAmount(Double.parseDouble(amountET.getText().toString()));
            cashFlow.setDescription(descriptionET.getText().toString());
            cashFlow.setInvoice_no(Integer.parseInt(invoiceNumberET.getText().toString()));
            cashFlow.setItem_id(Resources.getAllResourceId(itemsSP.getSelectedItem().toString()));
            cashFlow.setLocation(locationET.getText().toString());
            if(typeSP.getSelectedItem().toString().equals("Cash-In")){
                cashFlow.setPayer_id(Customers.getCustomerId(payersSP.getSelectedItem().toString()));
            }
            else{
                cashFlow.setPayer_id(Vendors.getVendorId(payeesSP.getSelectedItem().toString()));
            }
            cashFlow.setQty(Integer.parseInt(quantityET.getText().toString()));
            cashFlow.setReceipt_no(Integer.parseInt(receiptNumberET.getText().toString()));
            cashFlow.setTransaction_date(transactionDateET.getText().toString());
            cashFlow.setType_id(typeMap.get(typeSP.getSelectedItem().toString()));
            cashFlow.setTask_id(task_id);
            cashFlow.setTask_offline_id(task_offline_id);
            String json = gson.toJson(cashFlow);
            MultipartBuilder multipartBuilder = new MultipartBuilder();
            int count = 0;
            for (Map.Entry<String, TextView> entry : textViewMap.entrySet()) {
                Log.e("True/False", ""+!entry.getValue().getText().toString().equals("Attachment"));
                if(!entry.getValue().getText().toString().equals("Attachment")){
                    count++;
                    File file = new File(entry.getValue().getText().toString());
                    RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                    String param = "image_"+count;
                    Log.e("Param", param);
                    multipartBuilder.addFormDataPart(param, file.getName(), image);
                }
            }
            RequestBody fileRequestBody = multipartBuilder.build();
            Observable<SubmitProgressReportResponseWrapper> observable = cloudCheetahAPIService.addCashFlow(userName, deviceid, sessionKey, json, fileRequestBody);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<SubmitProgressReportResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            MainActivity.popFragment();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("AddCashFlow", e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(SubmitProgressReportResponseWrapper submitProgressReportResponseWrapper) {
                            if(submitProgressReportResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                Toast.makeText(ApplicationContext.get(), "Cash-In/Cash-Out sumitted successfully.", Toast.LENGTH_SHORT).show();
                                CashInOut cashInOut = new CashInOut();
                                cashInOut.setId(submitProgressReportResponseWrapper.getData().getId());
                                cashInOut.setType_id(typeMap.get(typeSP.getSelectedItem().toString()));
                                cashInOut.setTransaction_date(transactionDateET.getText().toString());
                                cashInOut.setLocation(locationET.getText().toString());
                                cashInOut.setDescription(descriptionET.getText().toString());
                                cashInOut.setAmount(Double.parseDouble(amountET.getText().toString()));
                                cashInOut.setInvoice_no(Integer.parseInt(invoiceNumberET.getText().toString()));
                                if(typeSP.getSelectedItem().toString().equals("Cash-In")){
                                    cashInOut.setPayer_id(Customers.getCustomerId(payersSP.getSelectedItem().toString()));
                                }
                                else{
                                    cashInOut.setPayer_id(Vendors.getVendorId(payeesSP.getSelectedItem().toString()));
                                }
                                cashInOut.setPayer_id(Customers.getCustomerId(payersSP.getSelectedItem().toString()));
                                cashInOut.setQty(Integer.parseInt(quantityET.getText().toString()));
                                cashInOut.setReceipt_no(Integer.parseInt(receiptNumberET.getText().toString()));
                                cashInOut.setAccount_id(Accounts.getAccountId(accountsSP.getSelectedItem().toString()));
                                cashInOut.setItem_id(Resources.getAllResourceId(itemsSP.getSelectedItem().toString()));
                                cashInOut.setAttachment_1(submitProgressReportResponseWrapper.getData().getImage_1());
                                cashInOut.setAttachment_2(submitProgressReportResponseWrapper.getData().getImage_2());
                                cashInOut.setAttachment_3(submitProgressReportResponseWrapper.getData().getImage_3());
                                cashInOut.setIs_submitted(true);
                                cashInOut.setTask_offline_id(task_offline_id);
                                cashInOut.save();
                                TaskCashInCashOutView.cashInOutAdapter.addItem(cashInOut);
                            }
                            else{
                                Toast.makeText(ApplicationContext.get(), "There is a problem submitting the Cash-In/Cash-Out please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            CashInOut cashInOut = new CashInOut();
            cashInOut.setType_id(typeMap.get(typeSP.getSelectedItem().toString()));
            cashInOut.setTransaction_date(transactionDateET.getText().toString());
            cashInOut.setLocation(locationET.getText().toString());
            cashInOut.setDescription(descriptionET.getText().toString());
            cashInOut.setAmount(Double.parseDouble(amountET.getText().toString()));
            cashInOut.setInvoice_no(Integer.parseInt(invoiceNumberET.getText().toString()));
            if(typeSP.getSelectedItem().toString().equals("Cash-In")){
                cashInOut.setPayer_id(Customers.getCustomerId(payersSP.getSelectedItem().toString()));
            }
            else{
                cashInOut.setPayer_id(Vendors.getVendorId(payeesSP.getSelectedItem().toString()));
            }
            cashInOut.setQty(Integer.parseInt(quantityET.getText().toString()));
            cashInOut.setReceipt_no(Integer.parseInt(receiptNumberET.getText().toString()));
            cashInOut.setAccount_id(Accounts.getAccountId(accountsSP.getSelectedItem().toString()));
            cashInOut.setItem_id(Resources.getAllResourceId(itemsSP.getSelectedItem().toString()));
            cashInOut.setAttachment_1(attachmentTV1.getText().toString());
            cashInOut.setAttachment_2(attachmentTV2.getText().toString());
            cashInOut.setAttachment_3(attachmentTV3.getText().toString());
            cashInOut.setTask_offline_id(task_offline_id);
            cashInOut.setIs_submitted(false);
            cashInOut.save();
            TaskCashInCashOutView.cashInOutAdapter.addItem(cashInOut);
            MainActivity.popFragment();
        }
    }

    @OnClick(R.id.add_attachment_1)
    public void addAttachmentOne(){
        dispatchTakePictureIntent();
        currentAttachment = TEXT_VIEW_ONE;
    }

    @OnClick(R.id.add_attachment_2)
    public void addAttachmentTwo(){
        dispatchTakePictureIntent();
        currentAttachment = TEXT_VIEW_TWO;
    }

    @OnClick(R.id.add_attachment_3)
    public void addAttachmentThree(){
        dispatchTakePictureIntent();
        currentAttachment = TEXT_VIEW_THREE;
    }

    @OnClick(R.id.attachment_one)
    public void viewAttachmentOne(){
        if(!attachmentTV1.getText().toString().equals("Attachment")){
            final ImageView imageView = new ImageView(getActivity());
            Picasso.with(ApplicationContext.get()).load(new File(attachmentTV1.getText().toString())).resize(200, 200)
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

    @OnClick(R.id.attachment_two)
    public void viewAttachmentTwo(){
        if(!attachmentTV2.getText().toString().equals("Attachment")){
            final ImageView imageView = new ImageView(getActivity());
            Picasso.with(ApplicationContext.get()).load(new File(attachmentTV2.getText().toString())).resize(200, 200)
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

    @OnClick(R.id.attachment_three)
    public void viewAttachmentThree(){
        if(!attachmentTV3.getText().toString().equals("Attachment")){
            final ImageView imageView = new ImageView(getActivity());
            Picasso.with(ApplicationContext.get()).load(new File(attachmentTV3.getText().toString())).resize(200, 200)
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

    /**
     * This method is used to set and select the date for the selected edittext in the form
     * @param editText
     */
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

    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        CameraActivity activity = (CameraActivity)getActivity();
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go.
            // If you don't do this, you may get a crash in some devices.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("Error", ex.getMessage(), ex);
                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/CloudCheetah/Pictures");
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        CameraActivity activity = (CameraActivity)getActivity();
        Uri contentUri = Uri.fromFile(image);
        activity.setCurrentPhotoPath("file:" + image.getPath());
        activity.setCapturedImageURI(contentUri);
        return image;
    }

    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        CameraActivity activity = (CameraActivity)getActivity();
        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == MainActivity.RESULT_OK) {
            addPhotoToGallery();
            CameraActivity activity = (CameraActivity)getActivity();

            // Show the full sized image.
            Log.e("Photo Path", activity.getCurrentPhotoPath());
            textViewMap.get(currentAttachment).setText(activity.getCurrentPhotoPath());
            currentPhotoPath = activity.getCurrentPhotoPath();
        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
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
