package com.forateq.cloudcheetah.adapters;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.activeandroid.query.Delete;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.TaskProgressReportsFragment;
import com.forateq.cloudcheetah.models.MyTasks;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.pojo.TaskProgressReportsResponseWrapper;
import com.forateq.cloudcheetah.pojo.TaskProgressResponse;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This adapter is use to display the tasks of the current user of the app
 *
 * Created by Vallejos Family on 6/10/2016.
 */
public class MyTasksAdapter extends RecyclerView.Adapter<MyTasksAdapter.ViewHolder>{

    private List<MyTasks> listTasks;
    private Context mContext;
    public static final String TAG = "MyTaskAdapter";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;

    /**
     * This is the constructor of the class used to create a new instance of this adapter
     * @param listTasks
     * @param context
     */
    public MyTasksAdapter(List<MyTasks> listTasks, Context context) {
        this.listTasks = listTasks;
        this.mContext = context;
        ((CloudCheetahApp) ApplicationContext.get()).getNetworkComponent().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_task, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final MyTasks myTasks = listTasks.get(i);
        viewHolder.taskName.setText(myTasks.getName());
        viewHolder.projectId.setText(""+myTasks.getProject_id());
        viewHolder.parentTaskId.setText(""+myTasks.getParent_id());
        viewHolder.taskId.setText(""+myTasks.getTaskId());
        viewHolder.taskOfflineId.setText(""+myTasks.getId());
    }

    /**
     * This method is used to add new item in the arraylist of tasks
     * @param myTasks
     */
    public void addItem(MyTasks myTasks){
        listTasks.add(myTasks);
        notifyDataSetChanged();
    }



    /**
     * This method is used to clear the items in the arraylist of tasks
     */
    public void clearItems(){
        listTasks.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listTasks == null ? 0 : listTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView taskName;
        public TextView taskId;
        public TextView projectId;
        public TextView parentTaskId;
        public TextView projectOfflineId;
        public TextView taskOfflineId;
        public MaterialRippleLayout rippleLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            taskId = (TextView) itemView.findViewById(R.id.task_id);
            projectId = (TextView) itemView.findViewById(R.id.project_id);
            parentTaskId = (TextView) itemView.findViewById(R.id.parent_task_id);
            projectOfflineId = (TextView) itemView.findViewById(R.id.project_offline_id);
            taskOfflineId = (TextView) itemView.findViewById(R.id.task_offline_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkAvailable()){
                        Log.e("Task_id", ""+taskId.getText().toString());
                        final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage("Getting progress reports...");
                        mProgressDialog.show();
                        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                        String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                        String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                        String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        Observable<TaskProgressReportsResponseWrapper> observable = cloudCheetahAPIService.getTaskUpdates(userName, deviceid, sessionKey, Integer.parseInt(taskId.getText().toString()));
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<TaskProgressReportsResponseWrapper>() {
                                    @Override
                                    public void onCompleted() {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("task_offline_id", taskOfflineId.getText().toString());
                                        bundle.putString("task_id", taskId.getText().toString());
                                        bundle.putString("task_name", taskName.getText().toString());
                                        TaskProgressReportsFragment taskProgressReportsFragment = new TaskProgressReportsFragment();
                                        taskProgressReportsFragment.setArguments(bundle);
                                        MainActivity.replaceFragment(taskProgressReportsFragment, TAG);
                                        if (mProgressDialog.isShowing()) {
                                            mProgressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e("GettingProgress", e.getMessage(), e);
                                        if (mProgressDialog.isShowing()) {
                                            mProgressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onNext(TaskProgressReportsResponseWrapper taskProgressReportsResponseWrapper) {
                                        Gson gson = new GsonBuilder()
                                                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                                                .create();
                                        Log.e("Json", gson.toJson(taskProgressReportsResponseWrapper));
                                        new Delete().from(TaskProgressReports.class).where("task_id = ?", taskId.getText().toString()).execute();
                                        Log.e("ReportSize", ""+taskProgressReportsResponseWrapper.getData().size());
                                        for(TaskProgressResponse taskProgressResponse : taskProgressReportsResponseWrapper.getData()){
                                            TaskProgressReports taskProgressReports = new TaskProgressReports();
                                            taskProgressReports.setTask_progress_id(taskProgressResponse.getId());
                                            taskProgressReports.setTask_name(taskName.getText().toString());
                                            taskProgressReports.setTask_id(taskProgressResponse.getTask_id());
                                            taskProgressReports.setReport_date(taskProgressResponse.getCreated_at());
                                            taskProgressReports.setPercent_completion(taskProgressResponse.getPercentage_completion());
                                            taskProgressReports.setTask_status(taskProgressResponse.getTask_status());
                                            taskProgressReports.setHours_work(taskProgressResponse.getHours_worked());
                                            taskProgressReports.setResources_used(taskProgressResponse.getResources_used());
                                            taskProgressReports.setTask_action(taskProgressResponse.getAction());
                                            taskProgressReports.setNotes(taskProgressResponse.getNotes());
                                            taskProgressReports.setConcerns_issues(taskProgressResponse.getConcerns());
                                            taskProgressReports.setChange_request(taskProgressResponse.getRequests());
                                            taskProgressReports.setAttachment_1(taskProgressResponse.getImage_1());
                                            taskProgressReports.setAttachment_2(taskProgressResponse.getImage_2());
                                            taskProgressReports.setAttachment_3(taskProgressResponse.getImage_3());
                                            taskProgressReports.setIs_submitted(true);
                                            taskProgressReports.save();
                                        }
                                    }
                                });
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putString("task_offline_id", taskOfflineId.getText().toString());
                        bundle.putString("task_id", taskId.getText().toString());
                        bundle.putString("task_name", taskName.getText().toString());
                        TaskProgressReportsFragment taskProgressReportsFragment = new TaskProgressReportsFragment();
                        taskProgressReportsFragment.setArguments(bundle);
                        MainActivity.replaceFragment(taskProgressReportsFragment, TAG);
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
