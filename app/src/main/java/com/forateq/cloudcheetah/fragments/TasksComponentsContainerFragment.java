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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.ProjectTasksView;
import com.forateq.cloudcheetah.views.TaskDetailsView;
import com.forateq.cloudcheetah.views.TaskResourcesView;
import com.forateq.cloudcheetah.views.TaskSubTasksView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** This fragment holds all the component fragment of the selected tasks such as resources, details etc.
 * Created by Vallejos Family on 5/30/2016.
 */
public class TasksComponentsContainerFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.ripple_drawer)
    MaterialRippleLayout rippleDrawer;
    @Bind(R.id.tasks_view_container)
    FrameLayout taskViewContainer;
    @Bind(R.id.task_details)
    LinearLayout taskDetailsLayout;
    @Bind(R.id.sub_tasks)
    LinearLayout subtasksLayout;
    @Bind(R.id.task_resources)
    LinearLayout tasksResourcesLayout;
    @Bind(R.id.delete_task)
    LinearLayout deleteTaskLayout;
    @Bind(R.id.task_drawer_layout)
    DrawerLayout taskDrawerLayout;
    long task_offline_id;
    int task_id;
    long project_offline_id;
    int project_id;
    int size;
    int position;
    public static final String COMPONENT_TASK_RESOURCES = "Task Resources";
    public static final String COMPONENT_SUB_TASKS = "Sub-Tasks";
    public static final String COMPONENT_TASK_DETAILS = "Task Details";
    private Map<String, View> taskComponentMap = new HashMap<>();
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_info_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        project_id = Integer.parseInt(getArguments().getString("project_id"));
        task_id = Integer.parseInt(getArguments().getString("task_id"));
        task_offline_id = Long.parseLong(getArguments().getString("task_offline_id"));
        project_offline_id = Integer.parseInt(getArguments().getString("project_offline_id"));
        size = getArguments().getInt("size");
        position = getArguments().getInt("position");
        taskDetailsLayout.setSelected(true);
        TaskSubTasksView taskSubTasksView = new TaskSubTasksView(ApplicationContext.get(), project_id, task_id, project_offline_id, task_offline_id);
        taskComponentMap.put(COMPONENT_SUB_TASKS, taskSubTasksView);
        TaskDetailsView taskDetailsView = new TaskDetailsView(ApplicationContext.get(), task_id, task_offline_id);
        taskComponentMap.put(COMPONENT_TASK_DETAILS, taskDetailsView);
        TaskResourcesView taskResourcesView = new TaskResourcesView(ApplicationContext.get(), task_id, task_offline_id, Projects.getProjectStatus(project_offline_id));
        taskComponentMap.put(COMPONENT_TASK_RESOURCES, taskResourcesView);
        if(!CloudCheetahApp.currentTaskComponent.equals("")){
            changeComponentView(taskComponentMap.get(CloudCheetahApp.currentTaskComponent));
        }
        else{
            changeComponentView(taskComponentMap.get(COMPONENT_TASK_DETAILS));
            CloudCheetahApp.currentTaskComponent = COMPONENT_TASK_DETAILS;
        }
        return v;
    }

    public void changeComponentView(View view){
        taskViewContainer.removeAllViews();
        taskViewContainer.addView(view);
    }

    @OnClick(R.id.sub_tasks)
    public void getSubtasks(){
        subtasksLayout.setSelected(true);
        taskDetailsLayout.setSelected(false);
        tasksResourcesLayout.setSelected(false);
        deleteTaskLayout.setSelected(false);
        taskDrawerLayout.closeDrawer(GravityCompat.END);
        changeComponentView(taskComponentMap.get(COMPONENT_SUB_TASKS));
        CloudCheetahApp.currentTaskComponent = COMPONENT_SUB_TASKS;
    }

    @OnClick(R.id.task_details)
    public void getTaskdetails(){
        subtasksLayout.setSelected(false);
        taskDetailsLayout.setSelected(true);
        tasksResourcesLayout.setSelected(false);
        deleteTaskLayout.setSelected(false);
        taskDrawerLayout.closeDrawer(GravityCompat.END);
        changeComponentView(taskComponentMap.get(COMPONENT_TASK_DETAILS));
        CloudCheetahApp.currentTaskComponent = COMPONENT_TASK_DETAILS;
    }

    @OnClick(R.id.task_resources)
    public void getTaskResources(){
        subtasksLayout.setSelected(false);
        taskDetailsLayout.setSelected(false);
        tasksResourcesLayout.setSelected(true);
        deleteTaskLayout.setSelected(false);
        taskDrawerLayout.closeDrawer(GravityCompat.END);
        changeComponentView(taskComponentMap.get(COMPONENT_TASK_RESOURCES));
        CloudCheetahApp.currentTaskComponent = COMPONENT_TASK_RESOURCES;
    }

    @OnClick(R.id.delete_task)
    public void deleteTask(){
        if(isNetworkAvailable()){
            final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                    .title("Delete")
                    .content("Are you sure you want to delete this task?")
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
                            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage("Deleting task...");
                            mProgressDialog.show();
                            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            Observable<ResponseWrapper> observable = cloudCheetahAPIService.deleteTask(task_id, userName, deviceid, sessionKey);
                            observable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .unsubscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<ResponseWrapper>() {
                                        @Override
                                        public void onCompleted() {
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                            subtasksLayout.setSelected(false);
                                            taskDetailsLayout.setSelected(false);
                                            tasksResourcesLayout.setSelected(false);
                                            deleteTaskLayout.setSelected(true);
                                            ProjectTasksView.taskAdapter.notifyItemRemoved(position);
                                            ProjectTasksView.taskAdapter.notifyItemRangeChanged(position, size);
                                            MainActivity.popFragment();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e("DeleteTask", e.getMessage(), e);
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onNext(ResponseWrapper responseWrapper) {
                                            if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                                new Delete().from(Tasks.class).where("task_id = ?", task_id).execute();
                                            }
                                        }
                                    });
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
            Toast.makeText(getActivity(), "Please connect to a network to delete the task.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.ripple_drawer)
    public void showDrawer(){
        taskDrawerLayout.openDrawer(GravityCompat.END);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CloudCheetahApp.currentTaskComponent = "";
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
