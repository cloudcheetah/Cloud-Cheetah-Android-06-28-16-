package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.TaskDetailsView;
import com.forateq.cloudcheetah.views.TaskResourcesView;
import com.forateq.cloudcheetah.views.TaskSubTasksView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    public static final String COMPONENT_TASK_RESOURCES = "Task Resources";
    public static final String COMPONENT_SUB_TASKS = "Sub-Tasks";
    public static final String COMPONENT_TASK_DETAILS = "Task Details";
    private Map<String, View> taskComponentMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_info_fragment, container, false);
        ButterKnife.bind(this, v);
        project_id = Integer.parseInt(getArguments().getString("project_id"));
        task_id = Integer.parseInt(getArguments().getString("task_id"));
        task_offline_id = Long.parseLong(getArguments().getString("task_offline_id"));
        project_offline_id = Integer.parseInt(getArguments().getString("project_offline_id"));
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
        subtasksLayout.setSelected(false);
        taskDetailsLayout.setSelected(false);
        tasksResourcesLayout.setSelected(false);
        deleteTaskLayout.setSelected(true);
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
}
