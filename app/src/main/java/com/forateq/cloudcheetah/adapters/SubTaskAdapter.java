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
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.TasksComponentsContainerFragment;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.TaskResources;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.pojo.SingleTaskResponseWrapper;
import com.forateq.cloudcheetah.pojo.SubTasks;
import com.forateq.cloudcheetah.pojo.TaskData;
import com.forateq.cloudcheetah.pojo.TaskListResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**This adapter is used to display all the subtasks of a specific task or subtasks
 * Created by Vallejos Family on 5/30/2016.
 */
public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.ViewHolder>{

    private List<Tasks> listTasks;
    private Context mContext;
    public static final String TAG = "TaskAdapter";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;

    /**
     * This is the constructor of the class used to create new instance of this adapter
     * @param listTasks
     * @param context
     */
    public SubTaskAdapter(List<Tasks> listTasks, Context context) {
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
        final Tasks tasks = listTasks.get(i);
        viewHolder.taskName.setText(tasks.getName());
        viewHolder.projectId.setText(""+tasks.getProject_id());
        viewHolder.parentTaskId.setText(""+tasks.getParent_id());
        viewHolder.taskId.setText(""+tasks.getTask_id());
        viewHolder.projectOfflineId.setText(""+tasks.getProject_offline_id());
        viewHolder.taskOfflineId.setText(""+tasks.getId());
    }

    /**
     * This method is used to add new item in the arraylists of subtasks
     * @param tasks
     */
    public void addItem(Tasks tasks){
        listTasks.add(tasks);
        notifyDataSetChanged();
    }

    /**
     * This method is used to remove a specific item in the arraylist of subtasks
     * @param task_id
     */
    public void removeItem(int task_id){
        for(Tasks tasks:listTasks){
            if(tasks.getProject_id() == task_id){
                listTasks.remove(tasks);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * This method is used to clear all the items in the arraylist of subtasks
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
                        Log.e("Task Id Sub Task", ""+taskId.getText().toString());
                        Toast.makeText(ApplicationContext.get(), "Getting subtasks...", Toast.LENGTH_LONG).show();
                        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                        String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                        String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                        Observable<SingleTaskResponseWrapper> observable = cloudCheetahAPIService.getSubTasks(Integer.parseInt(taskId.getText().toString()), userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                Settings.Secure.ANDROID_ID), sessionKey);
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<SingleTaskResponseWrapper>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e("SubTaskAdapter",  e.getMessage(), e);

                                    }

                                    @Override
                                    public void onNext(SingleTaskResponseWrapper singleTaskResponseWrapper) {
                                        Gson gson = new Gson();
                                        String json = gson.toJson(singleTaskResponseWrapper);
                                        Log.e("Json", json);
                                        if(singleTaskResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                            Log.e("Subtask Id", ""+singleTaskResponseWrapper.getData().getId());
                                                Tasks tasks = Tasks.getTaskById(singleTaskResponseWrapper.getData().getId());
                                                    Log.e("Task Name", singleTaskResponseWrapper.getData().getName());
                                                    Log.e("Parent Task Id", ""+singleTaskResponseWrapper.getData().getParent_id());
                                                    tasks.setName(singleTaskResponseWrapper.getData().getName());
                                                    tasks.setStart_date(singleTaskResponseWrapper.getData().getStart_date());
                                                    tasks.setEnd_date(singleTaskResponseWrapper.getData().getEnd_date());
                                                    tasks.setBudget(singleTaskResponseWrapper.getData().getBudget());
                                                    tasks.setDescription(singleTaskResponseWrapper.getData().getDescription());
                                                    tasks.setLatitude(singleTaskResponseWrapper.getData().getLatitude());
                                                    tasks.setLongitide(singleTaskResponseWrapper.getData().getLongitude());
                                                    tasks.setDuration(singleTaskResponseWrapper.getData().getDuration());
                                                    tasks.setProject_id(singleTaskResponseWrapper.getData().getProject_id());
                                                    tasks.setPerson_responsible_id(singleTaskResponseWrapper.getData().getPerson_responsible_id());
                                                    tasks.setParent_id(singleTaskResponseWrapper.getData().getParent_id());
                                                    tasks.save();
                                                    for(SubTasks subtasks : singleTaskResponseWrapper.getData().getSubtasks()){
                                                        Tasks subT = Tasks.getTaskById(subtasks.getId());
                                                        if(subT != null){
                                                            subT.setName(subtasks.getName());
                                                            subT.setStart_date(subtasks.getStart_date());
                                                            subT.setEnd_date(subtasks.getEnd_date());
                                                            subT.setBudget(subtasks.getBudget());
                                                            subT.setDescription(subtasks.getDescription());
                                                            subT.setLatitude(subtasks.getLatitude());
                                                            subT.setLongitide(subtasks.getLongitude());
                                                            subT.setDuration(subtasks.getDuration());
                                                            subT.setProject_id(subtasks.getProject_id());
                                                            subT.setProject_offline_id(Long.parseLong(projectOfflineId.getText().toString()));
                                                            subT.setPerson_responsible_id(subtasks.getPerson_responsible_id());
                                                            subT.setParent_id(subtasks.getParent_id());
                                                            subT.save();
                                                        }
                                                        else{
                                                            Tasks newSubT = new Tasks();
                                                            newSubT.setTask_id(subtasks.getId());
                                                            newSubT.setName(subtasks.getName());
                                                            newSubT.setStart_date(subtasks.getStart_date());
                                                            newSubT.setEnd_date(subtasks.getEnd_date());
                                                            newSubT.setBudget(subtasks.getBudget());
                                                            newSubT.setDescription(subtasks.getDescription());
                                                            newSubT.setLatitude(subtasks.getLatitude());
                                                            newSubT.setLongitide(subtasks.getLongitude());
                                                            newSubT.setDuration(subtasks.getDuration());
                                                            newSubT.setProject_id(subtasks.getProject_id());
                                                            newSubT.setProject_offline_id(Long.parseLong(projectOfflineId.getText().toString()));
                                                            newSubT.setPerson_responsible_id(subtasks.getPerson_responsible_id());
                                                            newSubT.setParent_id(subtasks.getParent_id());
                                                            newSubT.save();
                                                        }
                                                    }
                                                    for(com.forateq.cloudcheetah.pojo.TaskResources taskResources : singleTaskResponseWrapper.getData().getResources()){
                                                        TaskResources taskResourcesModel = TaskResources.getTaskResourceById(taskResources.getId());
                                                        if(taskResourcesModel != null){
                                                            taskResourcesModel.setQuantity(taskResources.getQty());
                                                            taskResourcesModel.setResource_id(taskResources.getResource_id());
                                                            taskResourcesModel.setTask_id(taskResources.getTask_id());
                                                            taskResourcesModel.setTask_resource_id(taskResources.getId());
                                                            taskResourcesModel.save();
                                                        }
                                                        else{
                                                            TaskResources newTaskResourcesModel = new TaskResources();
                                                            newTaskResourcesModel.setQuantity(taskResources.getQty());
                                                            newTaskResourcesModel.setResource_id(taskResources.getResource_id());
                                                            newTaskResourcesModel.setTask_id(taskResources.getTask_id());
                                                            newTaskResourcesModel.setTask_resource_id(taskResources.getId());
                                                            newTaskResourcesModel.save();
                                                        }
                                                    }
                                        }
                                        else{
                                            Toast.makeText(ApplicationContext.get(), "Status Code "+singleTaskResponseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                                        }
                                        Bundle bundle = new Bundle();
                                        bundle.putString("task_id", taskId.getText().toString());
                                        bundle.putString("project_id", projectId.getText().toString());
                                        bundle.putString("project_offline_id", ""+projectOfflineId.getText().toString());
                                        bundle.putString("task_offline_id", ""+taskOfflineId.getText().toString());
                                        TasksComponentsContainerFragment tasksComponentsContainerFragment = new TasksComponentsContainerFragment();
                                        tasksComponentsContainerFragment.setArguments(bundle);
                                        MainActivity.replaceFragment(tasksComponentsContainerFragment, TAG);
                                    }
                                });
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putString("task_id", taskId.getText().toString());
                        bundle.putString("project_id", projectId.getText().toString());
                        bundle.putString("project_offline_id", ""+projectOfflineId.getText().toString());
                        bundle.putString("task_offline_id", ""+taskOfflineId.getText().toString());
                        TasksComponentsContainerFragment tasksComponentsContainerFragment = new TasksComponentsContainerFragment();
                        tasksComponentsContainerFragment.setArguments(bundle);
                        MainActivity.replaceFragment(tasksComponentsContainerFragment, TAG);
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
