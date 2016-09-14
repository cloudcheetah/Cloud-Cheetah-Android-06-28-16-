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
import com.forateq.cloudcheetah.fragments.ProgressReportViewFragment;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.pojo.SubmitProgressReportResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
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

/** This adapter is used to display all the task progress report of a selected task
 * Created by Vallejos Family on 6/10/2016.
 */
public class TaskProgressAdapter extends RecyclerView.Adapter<TaskProgressAdapter.ViewHolder>{

    private List<TaskProgressReports> listTaskReports;
    private Context mContext;
    public static final String TAG = "TaskAdapter";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    public  static final String NO_IMAGE = "/storage/emulated/0/Pictures/CloudCheetah/Pictures/Attachment";
    /**
     * This is the constructor of the class used to create new instance of this adapter
     * @param listTaskReports
     * @param context
     */
    public TaskProgressAdapter(List<TaskProgressReports> listTaskReports, Context context) {
        this.listTaskReports = listTaskReports;
        this.mContext = context;
        ((CloudCheetahApp) ApplicationContext.get()).getNetworkComponent().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_progress_report, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final TaskProgressReports taskProgressReports = listTaskReports.get(i);
        viewHolder.taskProgressOfflineId.setText(""+taskProgressReports.getId());
        viewHolder.taskProgressId.setText(""+taskProgressReports.getTask_progress_id());
        viewHolder.progressReportDate.setText(taskProgressReports.getReport_date());
        viewHolder.percentage.setText(taskProgressReports.getPercent_completion() + "%");
        if(taskProgressReports.is_submitted()){
            viewHolder.taskStatus.setText("Submitted");
            viewHolder.submitReport.setVisibility(View.GONE);
        }
        else{
            viewHolder.taskStatus.setText("Not yet submitted.");
        }
    }

    /**
     * This method is used to add new progress report in the arraylist of progress reports
     * @param taskProgressReports
     */
    public void addItem(TaskProgressReports taskProgressReports){
        listTaskReports.add(taskProgressReports);
        notifyDataSetChanged();
    }

    /**
     * This method is used to clear all the items in the arraylist of progress reports
     */
    public void clearItems(){
        listTaskReports.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listTaskReports == null ? 0 : listTaskReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView progressReportDate;
        public TextView taskProgressId;
        public TextView taskProgressOfflineId;
        public TextView taskStatus;
        public TextView percentage;
        public MaterialRippleLayout rippleLayout;
        public ImageView submitReport;
        public ViewHolder(View itemView) {
            super(itemView);
            progressReportDate = (TextView) itemView.findViewById(R.id.task_date);
            taskProgressId = (TextView) itemView.findViewById(R.id.task_progress_id);
            taskProgressOfflineId = (TextView) itemView.findViewById(R.id.task_progress_offline_id);
            taskStatus = (TextView) itemView.findViewById(R.id.task_status);
            percentage = (TextView) itemView.findViewById(R.id.percentage);
            submitReport = (ImageView) itemView.findViewById(R.id.submit_report);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("status", taskStatus.getText().toString());
                    bundle.putString("task_progress_id", taskProgressId.getText().toString());
                    bundle.putString("task_progress_offline_id", taskProgressOfflineId.getText().toString());
                    ProgressReportViewFragment progressReportViewFragment = new ProgressReportViewFragment();
                    progressReportViewFragment.setArguments(bundle);
                    MainActivity.replaceFragment(progressReportViewFragment, TAG);
                }
            });
            submitReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()){
                        Toast.makeText(ApplicationContext.get(), "Submitting report...", Toast.LENGTH_SHORT).show();
                        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                        String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                        String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                        String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        final TaskProgressReports taskProgressReports = TaskProgressReports.getProgressOfflineReportById(Long.parseLong(taskProgressOfflineId.getText().toString()));
                        MultipartBuilder multipartBuilder = new MultipartBuilder();
                        if(!taskProgressReports.getAttachment_1().equals(NO_IMAGE) && taskProgressReports.getAttachment_1() != null){
                            File file = new File(taskProgressReports.getAttachment_1());
                            RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                            multipartBuilder.addFormDataPart("image_1", file.getName(), image);
                        }
                        if(!taskProgressReports.getAttachment_2().equals(NO_IMAGE) && taskProgressReports.getAttachment_2() != null){
                            File file = new File(taskProgressReports.getAttachment_2());
                            RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                            multipartBuilder.addFormDataPart("image_2", file.getName(), image);
                        }
                        if(!taskProgressReports.getAttachment_3().equals(NO_IMAGE) && taskProgressReports.getAttachment_3() != null){
                            File file = new File(taskProgressReports.getAttachment_3());
                            RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
                            multipartBuilder.addFormDataPart("image_3", file.getName(), image);
                        }
                        RequestBody fileRequestBody = multipartBuilder.build();
                        Observable<SubmitProgressReportResponseWrapper> observable = cloudCheetahAPIService
                                .addProgressReport(userName,
                                        deviceid,
                                        sessionKey,
                                        ""+taskProgressReports.getTask_id(),
                                        taskProgressReports.getReport_date(),
                                        ""+taskProgressReports.getPercent_completion(),
                                        taskProgressReports.getTask_status(),
                                        ""+taskProgressReports.getHours_work(),
                                        taskProgressReports.getResources_used(),
                                        taskProgressReports.getTask_action(),
                                        taskProgressReports.getNotes(),
                                        taskProgressReports.getConcerns_issues(),
                                        taskProgressReports.getChange_request(),
                                        fileRequestBody);
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<SubmitProgressReportResponseWrapper>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e("AddProgressReport", e.getMessage(), e);
                                    }

                                    @Override
                                    public void onNext(SubmitProgressReportResponseWrapper submitProgressReportResponseWrapper) {
                                        if(submitProgressReportResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                            Toast.makeText(ApplicationContext.get(), "Progress report sumitted successfully.", Toast.LENGTH_SHORT).show();
                                            taskProgressReports.setIs_submitted(true);
                                            taskProgressReports.setAttachment_1(submitProgressReportResponseWrapper.getData().getImage_1());
                                            taskProgressReports.setAttachment_2(submitProgressReportResponseWrapper.getData().getImage_2());
                                            taskProgressReports.setAttachment_3(submitProgressReportResponseWrapper.getData().getImage_3());
                                            taskProgressReports.save();
                                            notifyDataSetChanged();
                                        }
                                        else{
                                            Toast.makeText(ApplicationContext.get(), "There is a problem submitting the progress report please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else{
                        Toast.makeText(ApplicationContext.get(), "Please connect to a network to submit the report.", Toast.LENGTH_SHORT).show();
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
