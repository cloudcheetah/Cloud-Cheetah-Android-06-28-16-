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
import android.widget.LinearLayout;
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
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.pojo.AddResource;
import com.forateq.cloudcheetah.pojo.AddResourceWrapper;
import com.forateq.cloudcheetah.pojo.SubmitProgressReportResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.AddResourceView;
import com.forateq.cloudcheetah.views.ResourceRowView;
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
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** This fragment is used to add new Progress Report for a specific task
 * Created by Vallejos Family on 6/10/2016.
 */
public class AddTaskProgressReportFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.task_name)
    EditText taskNameET;
    @Bind(R.id.task_status)
    EditText taskStatusET;
    @Bind(R.id.report_date)
    EditText taskReportDateET;
    @Bind(R.id.hours_work)
    EditText hoursWorkET;
    @Bind(R.id.percent_completion)
    EditText percentCompletionET;
    @Bind(R.id.resource_container)
    LinearLayout resourceContainer;
    @Bind(R.id.task_action)
    Spinner actionSpinner;
    @Bind(R.id.task_notes)
    EditText taskNotesET;
    @Bind(R.id.task_issues)
    EditText taskIssuesET;
    @Bind(R.id.change_request)
    EditText changeRequestET;
    @Bind(R.id.add_progress_report)
    Button addProgressButton;
    @Bind(R.id.add_resource)
    ImageView addResourceIV;
    @Bind(R.id.attachment_one)
    TextView attachmentOneTV;
    @Bind(R.id.attachment_two)
    TextView attachmentTwoTV;
    @Bind(R.id.attachment_three)
    TextView attachmentThreeTV;
    @Bind(R.id.add_attachment_1)
    Button addAttachmentOneBT;
    @Bind(R.id.add_attachment_2)
    Button addAttachmentTwoBT;
    @Bind(R.id.add_attachment_3)
    Button addAttachmentThreeBT;
    String taskName;
    long task_offline_id;
    int task_id;
    List<AddResource> addResourceList;
    Gson gson;
    static final int REQUEST_TAKE_PHOTO = 11111;
    String currentPhotoPath;
    String currentAttachment;
    public static final String TEXT_VIEW_ONE = "textview_1";
    public static final String TEXT_VIEW_TWO = "textview_2";
    public static final String TEXT_VIEW_THREE = "textview_3";
    Map<String, TextView> textViewMap;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    private String imageFileName;
    private File storageDir;
    private Map<String, Integer> actionMap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_task_progress_report_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        actionMap = new HashMap();
        actionMap.put("", 0);
        actionMap.put("On-Hold", 4);
        actionMap.put("Resume", 5);
        actionMap.put("Cancelled", -1);
        storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/CloudCheetah/Pictures");
        List<String> actionList = new ArrayList<>();
        actionList.add("");
        actionList.add("On-Hold");
        actionList.add("Resume");
        actionList.add("Cancelled");
        addResourceList = new ArrayList<>();
        taskName = getArguments().getString("task_name");
        task_id = Integer.parseInt(getArguments().getString("task_id"));
        task_offline_id = Long.parseLong(getArguments().getString("task_offline_id"));
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, actionList);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskNameET.setText(taskName);
        actionSpinner.setAdapter(nameAdapter);
        gson = new Gson();
        textViewMap = new HashMap<>();
        textViewMap.put(TEXT_VIEW_ONE, attachmentOneTV);
        textViewMap.put(TEXT_VIEW_TWO, attachmentTwoTV);
        textViewMap.put(TEXT_VIEW_THREE, attachmentThreeTV);
        return v;
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.report_date)
    public void setReportDate(){
        setDate(taskReportDateET);
    }

    @OnClick(R.id.add_resource)
    public void addResource(){
        final AddResourceView addResourceView = new AddResourceView(getActivity(), task_offline_id, task_id);
        final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                .title("Add resource")
                .titleColorRes(R.color.colorText)
                .backgroundColorRes(R.color.colorPrimary)
                .widgetColorRes(R.color.colorText)
                .customView(addResourceView, true)
                .positiveText("Ok")
                .positiveColorRes(R.color.colorText)
                .negativeText("Cancel")
                .negativeColorRes(R.color.colorText)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        ResourceRowView resourceRowView = new ResourceRowView(getActivity());
                        resourceRowView.getResourceNameTV().setText(addResourceView.getResourceNameSP().getSelectedItem().toString());
                        resourceRowView.getResourceQuantityTV().setText(addResourceView.getResourceQtyET().getText().toString());
                        AddResource addResource = new AddResource();
                        addResource.setResourceName(addResourceView.getResourceNameSP().getSelectedItem().toString());
                        addResource.setResourceQuantity(Integer.parseInt(addResourceView.getResourceQtyET().getText().toString()));
                        addResource.setId(Resources.getAllResourceId(addResourceView.getResourceNameSP().getSelectedItem().toString()));
                        addResourceList.add(addResource);
                        resourceContainer.addView(resourceRowView);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                });
        final MaterialDialog addNoteDialog = createNoteDialog.build();
        addNoteDialog.show();
    }

    @OnClick(R.id.add_progress_report)
    public void addProgressReport(){
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Submitting progress report...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            String body_task_id = ""+task_id;
            String report_date = taskReportDateET.getText().toString();
            String percentage_completion = percentCompletionET.getText().toString();
            String task_status = taskStatusET.getText().toString();
            String hours_worked = hoursWorkET.getText().toString();
            AddResourceWrapper addResourceWrapper = new AddResourceWrapper();
            addResourceWrapper.setResourceList(addResourceList);
            String resources_used = gson.toJson(addResourceWrapper);
            int  action = actionMap.get(actionSpinner.getSelectedItem().toString());
            String notes = taskNotesET.getText().toString();
            String concerns = taskIssuesET.getText().toString();
            String requests = changeRequestET.getText().toString();
            MultipartBuilder multipartBuilder = new MultipartBuilder();
            int count = 0;
            for (Map.Entry<String, TextView> entry : textViewMap.entrySet()) {
                Log.e("True/False", ""+!entry.getValue().getText().toString().equals("Attachment"));
                if(!entry.getValue().getText().toString().equals("Attachment")){
                    count++;
                    File file = new File(storageDir, entry.getValue().getText().toString());
                    RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                    String param = "image_"+count;
                    Log.e("Param", param);
                    multipartBuilder.addFormDataPart(param, file.getName(), image);
                }
            }
            RequestBody fileRequestBody = multipartBuilder.build();
            Observable<SubmitProgressReportResponseWrapper> observable = cloudCheetahAPIService
                    .addProgressReport(userName,
                            deviceid,
                            sessionKey,
                            body_task_id,
                            report_date,
                            percentage_completion,
                            task_status,
                            hours_worked,
                            resources_used,
                            action,
                            notes,
                            concerns,
                            requests,
                            fileRequestBody);
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
                            Log.e("AddProgressReport", e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(SubmitProgressReportResponseWrapper submitProgressReportResponseWrapper) {
                            if(submitProgressReportResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                Toast.makeText(ApplicationContext.get(), "Progress report sumitted successfully.", Toast.LENGTH_SHORT).show();
                                TaskProgressReports taskProgressReports = new TaskProgressReports();
                                taskProgressReports.setTask_progress_id(submitProgressReportResponseWrapper.getData().getId());
                                taskProgressReports.setTask_offline_id(task_offline_id);
                                taskProgressReports.setTask_id(task_id);
                                taskProgressReports.setTask_name(taskNameET.getText().toString());
                                taskProgressReports.setTask_status(taskStatusET.getText().toString());
                                taskProgressReports.setChange_request(changeRequestET.getText().toString());
                                taskProgressReports.setConcerns_issues(taskIssuesET.getText().toString());
                                taskProgressReports.setHours_work(Integer.parseInt(hoursWorkET.getText().toString()));
                                taskProgressReports.setNotes(taskNotesET.getText().toString());
                                taskProgressReports.setPercent_completion(Integer.parseInt(percentCompletionET.getText().toString()));
                                taskProgressReports.setReport_date(taskReportDateET.getText().toString());
                                AddResourceWrapper addResourceWrapper = new AddResourceWrapper();
                                addResourceWrapper.setResourceList(addResourceList);
                                taskProgressReports.setResources_used(gson.toJson(addResourceWrapper));
                                taskProgressReports.setTask_action(actionMap.get(actionSpinner.getSelectedItem().toString()));
                                taskProgressReports.setIs_submitted(true);
                                taskProgressReports.setAttachment_1(submitProgressReportResponseWrapper.getData().getImage_1());
                                taskProgressReports.setAttachment_2(submitProgressReportResponseWrapper.getData().getImage_2());
                                taskProgressReports.setAttachment_3(submitProgressReportResponseWrapper.getData().getImage_3());
                                taskProgressReports.save();
                                TaskProgressReportsView.taskProgressAdapter.addItem(taskProgressReports);
                            }
                            else{
                                Toast.makeText(ApplicationContext.get(), "There is a problem submitting the progress report please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            TaskProgressReports taskProgressReports = new TaskProgressReports();
            taskProgressReports.setTask_offline_id(task_offline_id);
            taskProgressReports.setTask_id(task_id);
            taskProgressReports.setTask_name(taskNameET.getText().toString());
            taskProgressReports.setTask_status(taskStatusET.getText().toString());
            taskProgressReports.setChange_request(changeRequestET.getText().toString());
            taskProgressReports.setConcerns_issues(taskIssuesET.getText().toString());
            taskProgressReports.setHours_work(Integer.parseInt(hoursWorkET.getText().toString()));
            taskProgressReports.setNotes(taskNotesET.getText().toString());
            taskProgressReports.setPercent_completion(Integer.parseInt(percentCompletionET.getText().toString()));
            taskProgressReports.setReport_date(taskReportDateET.getText().toString());
            AddResourceWrapper addResourceWrapper = new AddResourceWrapper();
            addResourceWrapper.setResourceList(addResourceList);
            taskProgressReports.setResources_used(gson.toJson(addResourceWrapper));
            taskProgressReports.setTask_action(actionMap.get(actionSpinner.getSelectedItem().toString()));
            taskProgressReports.setIs_submitted(false);
            taskProgressReports.setAttachment_1(new File(storageDir, attachmentOneTV.getText().toString()).getAbsolutePath());
            taskProgressReports.setAttachment_2(new File(storageDir, attachmentTwoTV.getText().toString()).getAbsolutePath());
            taskProgressReports.setAttachment_3(new File(storageDir, attachmentThreeTV.getText().toString()).getAbsolutePath());
            taskProgressReports.save();
            Toast.makeText(getActivity(), "Progress report successfully added.", Toast.LENGTH_SHORT).show();
            TaskProgressReportsView.taskProgressAdapter.addItem(taskProgressReports);
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
        if(!attachmentOneTV.getText().toString().equals("Attachment")){
            final ImageView imageView = new ImageView(getActivity());
            Picasso.with(ApplicationContext.get()).load(new File(storageDir, attachmentOneTV.getText().toString()).getAbsolutePath()).resize(200, 200)
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
        if(!attachmentTwoTV.getText().toString().equals("Attachment")){
            final ImageView imageView = new ImageView(getActivity());
            Picasso.with(ApplicationContext.get()).load(new File(storageDir, attachmentTwoTV.getText().toString()).getAbsolutePath()).resize(200, 200)
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
        if(!attachmentThreeTV.getText().toString().equals("Attachment")){
            final ImageView imageView = new ImageView(getActivity());
            Picasso.with(ApplicationContext.get()).load(new File(storageDir, attachmentThreeTV.getText().toString()).getAbsolutePath()).resize(200, 200)
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
        imageFileName = "JPEG_" + timeStamp + "_"+".jpg";
        File image = new File(storageDir, imageFileName);
        try {
            // Make sure the Pictures directory exists.
            storageDir.mkdirs();
            image.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        CameraActivity activity = (CameraActivity)getActivity();
        Uri contentUri = Uri.fromFile(image);
        activity.setCurrentPhotoPath("file://" + image.getPath());
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
            textViewMap.get(currentAttachment).setText(imageFileName);
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
