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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.ProjectMembers;
import com.forateq.cloudcheetah.models.ProjectResources;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.TaskResources;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.ProjectBatchMembers;
import com.forateq.cloudcheetah.pojo.ProjectBatchProcess;
import com.forateq.cloudcheetah.pojo.ProjectBatchResources;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.pojo.SubTasks;
import com.forateq.cloudcheetah.pojo.TaskBatchProcess;
import com.forateq.cloudcheetah.pojo.TaskBatchResources;
import com.forateq.cloudcheetah.pojo.TaskData;
import com.forateq.cloudcheetah.pojo.TaskListResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.ProjectDetailsView;
import com.forateq.cloudcheetah.views.ProjectMembersView;
import com.forateq.cloudcheetah.views.ProjectResourcesView;
import com.forateq.cloudcheetah.views.ProjectTasksView;
import com.google.gson.Gson;

import java.util.ArrayList;
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

/** This fragment holds the components fragment of a project such as resources, members, details etc.
 * Created by Vallejos Family on 5/12/2016.
 */
public class ProjectsComponentsContainerFragment extends Fragment {

    @Bind(R.id.back)
    ImageView backButton;
    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleLayout;
    public static final String TAG = "ProjectsComponentsContainerFragment";
    @Bind(R.id.ripple_drawer)
    MaterialRippleLayout rippleDrawer;
    @Bind(R.id.project_drawer_layout)
    DrawerLayout projectDrawer;
    @Bind(R.id.projects_view_container)
    public FrameLayout projectComponentsContainer;
    @Bind(R.id.project_members)
    LinearLayout projectMembersLayout;
    @Bind(R.id.project_details)
    LinearLayout projectDetailsLayout;
    @Bind(R.id.project_tasks)
    LinearLayout projectTasksLayout;
    @Bind(R.id.project_resources)
    LinearLayout projectResourcesLayout;
    @Bind(R.id.delete_project)
    LinearLayout deleteProjectLayout;
    @Bind(R.id.submit_project)
    LinearLayout submitProjectLayout;
    @Bind(R.id.update_project)
    LinearLayout updateProjectLayout;
    int project_id;
    long project_offline_id;
    public static final String COMPONENT_PROJECT_MEMBERS = "Project Members";
    public static final String COMPONENT_PROJECT_TASKS = "Project Tasks";
    public static final String COMPONENT_PROJECT_RESOURCES = "Project Resources";
    public static final String COMPONENT_PROJECT_DETAILS = "Project Details";
    private Map<String, View> componentViewMap = new HashMap<>();
    private ProjectDetailsView projectDetailsView;
    private ProjectResourcesView projectResourcesView;
    private ProjectMembersView projectMembersView;
    private ProjectTasksView projectTasksView;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    Observable<ResponseWrapper> observable;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.projects_info_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        project_id = Integer.parseInt(getArguments().getString("project_id"));
        project_offline_id = Long.parseLong(getArguments().getString("project_offline_id"));
        if(Projects.getProjectStatus(project_offline_id).equals(AccountGeneral.STATUS_SYNC)){
            submitProjectLayout.setVisibility(View.GONE);
        }
        projectDetailsView = new ProjectDetailsView(ApplicationContext.get(), project_id, project_offline_id);
        componentViewMap.put(COMPONENT_PROJECT_DETAILS, projectDetailsView);
        projectResourcesView = new ProjectResourcesView(ApplicationContext.get(), project_id, project_offline_id);
        componentViewMap.put(COMPONENT_PROJECT_RESOURCES, projectResourcesView);
        projectMembersView = new ProjectMembersView(ApplicationContext.get(), project_id, project_offline_id);
        componentViewMap.put(COMPONENT_PROJECT_MEMBERS, projectMembersView);
        projectTasksView = new ProjectTasksView(ApplicationContext.get(), project_id, project_offline_id, Projects.getProjectStatus(project_offline_id));
        componentViewMap.put(COMPONENT_PROJECT_TASKS, projectTasksView);
        if(!CloudCheetahApp.currentProjectComponent.equals("")){
            changeComponentView(componentViewMap.get(CloudCheetahApp.currentProjectComponent));
            switch (CloudCheetahApp.currentProjectComponent){
                case COMPONENT_PROJECT_DETAILS:{
                    projectDetailsLayout.setSelected(true);
                    break;
                }
                case COMPONENT_PROJECT_MEMBERS:{
                    projectMembersLayout.setSelected(true);
                    break;
                }
                case COMPONENT_PROJECT_RESOURCES:{
                    projectResourcesLayout.setSelected(true);
                    break;
                }
                case COMPONENT_PROJECT_TASKS:{
                    projectTasksLayout.setSelected(true);
                    break;
                }
            }
        }
        else{
            showProjectDetails();
        }
    }

    @OnClick(R.id.ripple_back)
    void ripple(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.ripple_drawer)
    void showDrawer(){
        projectDrawer.openDrawer(GravityCompat.END);
    }

    public FrameLayout getProjectCommponentsContainer() {
        return projectComponentsContainer;
    }

    @OnClick(R.id.project_members)
    void showProjectMembers(){
        projectMembersLayout.setSelected(true);
        projectDetailsLayout.setSelected(false);
        projectResourcesLayout.setSelected(false);
        projectTasksLayout.setSelected(false);
        deleteProjectLayout.setSelected(false);
        updateProjectLayout.setSelected(false);
        submitProjectLayout.setSelected(false);
        projectDrawer.closeDrawer(GravityCompat.END);
        changeComponentView(componentViewMap.get(COMPONENT_PROJECT_MEMBERS));
        CloudCheetahApp.currentProjectComponent = COMPONENT_PROJECT_MEMBERS;
    }

    @OnClick(R.id.project_details)
    void showProjectDetails(){
        projectMembersLayout.setSelected(false);
        projectDetailsLayout.setSelected(true);
        projectResourcesLayout.setSelected(false);
        projectTasksLayout.setSelected(false);
        deleteProjectLayout.setSelected(false);
        updateProjectLayout.setSelected(false);
        submitProjectLayout.setSelected(false);
        projectDrawer.closeDrawer(GravityCompat.END);
        changeComponentView(componentViewMap.get(COMPONENT_PROJECT_DETAILS));
        CloudCheetahApp.currentProjectComponent = COMPONENT_PROJECT_DETAILS;
    }

    @OnClick(R.id.project_resources)
    void showProjectResources(){
        projectMembersLayout.setSelected(false);
        projectDetailsLayout.setSelected(false);
        projectResourcesLayout.setSelected(true);
        projectTasksLayout.setSelected(false);
        deleteProjectLayout.setSelected(false);
        updateProjectLayout.setSelected(false);
        submitProjectLayout.setSelected(false);
        projectDrawer.closeDrawer(GravityCompat.END);
        changeComponentView(componentViewMap.get(COMPONENT_PROJECT_RESOURCES));
        CloudCheetahApp.currentProjectComponent = COMPONENT_PROJECT_RESOURCES;
    }

    @OnClick(R.id.project_tasks)
    void showProjectTasks(){
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Getting tasks...");
            mProgressDialog.show();
            Log.e("Project Id", ""+project_id);
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            Observable<TaskListResponseWrapper> observable = cloudCheetahAPIService.getAllTasks(project_id, userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID), sessionKey, Projects.getProjectById(project_id).getProject_tasks_timestamp());
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<TaskListResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            projectMembersLayout.setSelected(false);
                            projectDetailsLayout.setSelected(false);
                            projectResourcesLayout.setSelected(false);
                            projectTasksLayout.setSelected(true);
                            deleteProjectLayout.setSelected(false);
                            updateProjectLayout.setSelected(false);
                            submitProjectLayout.setSelected(false);
                            projectDrawer.closeDrawer(GravityCompat.END);
                            componentViewMap.remove(COMPONENT_PROJECT_TASKS);
                            projectTasksView = new ProjectTasksView(ApplicationContext.get(), project_id, project_offline_id, Projects.getProjectStatus(project_offline_id));
                            componentViewMap.put(COMPONENT_PROJECT_TASKS, projectTasksView);
                            changeComponentView(componentViewMap.get(COMPONENT_PROJECT_TASKS));
                            CloudCheetahApp.currentProjectComponent = COMPONENT_PROJECT_TASKS;
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("ProjectComponent",  e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(TaskListResponseWrapper taskListResponseWrapper) {
                            Gson gson = new Gson();
                            String json = gson.toJson(taskListResponseWrapper);
                            Log.e("Json", json);
                            if(taskListResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                for(TaskData taskData : taskListResponseWrapper.getData()){
                                    Tasks tasks = Tasks.getTaskById(taskData.getId());
                                    if(tasks != null){
                                        Log.e("Task Name", taskData.getName());
                                        Log.e("Parent Task Id", ""+taskData.getParent_id());
                                        tasks.setName(taskData.getName());
                                        tasks.setProject_offline_id(project_offline_id);
                                        tasks.setStart_date(taskData.getStart_date());
                                        tasks.setEnd_date(taskData.getEnd_date());
                                        tasks.setBudget(taskData.getBudget());
                                        tasks.setDescription(taskData.getDescription());
                                        tasks.setLatitude(taskData.getLatitude());
                                        tasks.setLongitide(taskData.getLongitude());
                                        tasks.setDuration(taskData.getDuration());
                                        tasks.setProject_id(taskData.getProject_id());
                                        tasks.setPerson_responsible_id(taskData.getPerson_responsible_id());
                                        tasks.setParent_id(taskData.getParent_id());
                                        tasks.save();
                                        for(SubTasks subtasks : taskData.getSubtasks()){
                                            Tasks subT = Tasks.getTaskById(subtasks.getId());
                                            if(subT != null){
                                                subT.setName(subtasks.getName());
                                                subT.setTask_id(subtasks.getId());
                                                subT.setStart_date(subtasks.getStart_date());
                                                subT.setEnd_date(subtasks.getEnd_date());
                                                subT.setBudget(subtasks.getBudget());
                                                subT.setDescription(subtasks.getDescription());
                                                subT.setLatitude(subtasks.getLatitude());
                                                subT.setLongitide(subtasks.getLongitude());
                                                subT.setDuration(subtasks.getDuration());
                                                subT.setProject_id(subtasks.getProject_id());
                                                subT.setProject_offline_id(project_offline_id);
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
                                                newSubT.setProject_offline_id(project_offline_id);
                                                newSubT.setPerson_responsible_id(subtasks.getPerson_responsible_id());
                                                newSubT.setParent_id(subtasks.getParent_id());
                                                newSubT.save();
                                            }
                                        }
                                        for(com.forateq.cloudcheetah.pojo.TaskResources taskResources : taskData.getResources()){
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
                                        Log.e("New Task Name", taskData.getName());
                                        Log.e("New Parent Task Id", ""+taskData.getParent_id());
                                        Tasks newTasks = new Tasks();
                                        newTasks.setName(taskData.getName());
                                        newTasks.setStart_date(taskData.getStart_date());
                                        newTasks.setEnd_date(taskData.getEnd_date());
                                        newTasks.setBudget(taskData.getBudget());
                                        newTasks.setDescription(taskData.getDescription());
                                        newTasks.setLatitude(taskData.getLatitude());
                                        newTasks.setLongitide(taskData.getLongitude());
                                        newTasks.setDuration(taskData.getDuration());
                                        newTasks.setProject_id(taskData.getProject_id());
                                        newTasks.setProject_offline_id(project_offline_id);
                                        newTasks.setPerson_responsible_id(taskData.getPerson_responsible_id());
                                        newTasks.setParent_id(taskData.getParent_id());
                                        newTasks.setTask_id(taskData.getId());
                                        newTasks.save();
                                        for(SubTasks subtasks : taskData.getSubtasks()){
                                            Tasks subT = Tasks.getTaskById(subtasks.getId());
                                            if(subT != null){
                                                subT.setTask_id(subtasks.getId());
                                                subT.setName(subtasks.getName());
                                                subT.setStart_date(subtasks.getStart_date());
                                                subT.setEnd_date(subtasks.getEnd_date());
                                                subT.setBudget(subtasks.getBudget());
                                                subT.setDescription(subtasks.getDescription());
                                                subT.setLatitude(subtasks.getLatitude());
                                                subT.setLongitide(subtasks.getLongitude());
                                                subT.setDuration(subtasks.getDuration());
                                                subT.setProject_id(subtasks.getProject_id());
                                                subT.setProject_offline_id(project_offline_id);
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
                                                newSubT.setProject_offline_id(project_offline_id);
                                                newSubT.setPerson_responsible_id(subtasks.getPerson_responsible_id());
                                                newSubT.setParent_id(subtasks.getParent_id());
                                                newSubT.save();
                                            }
                                        }
                                        for(com.forateq.cloudcheetah.pojo.TaskResources taskResources : taskData.getResources()){
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
                                    Projects projects = Projects.getProjectById(project_id);
                                    projects.setProject_tasks_timestamp(taskListResponseWrapper.getTimestamp());
                                    projects.save();
                                }
                            }
                            else{
                                Toast.makeText(ApplicationContext.get(), "Status Code "+taskListResponseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            projectMembersLayout.setSelected(false);
            projectDetailsLayout.setSelected(false);
            projectResourcesLayout.setSelected(false);
            projectTasksLayout.setSelected(true);
            deleteProjectLayout.setSelected(false);
            updateProjectLayout.setSelected(false);
            submitProjectLayout.setSelected(false);
            projectDrawer.closeDrawer(GravityCompat.END);
            componentViewMap.remove(COMPONENT_PROJECT_TASKS);
            projectTasksView = new ProjectTasksView(ApplicationContext.get(), project_id, project_offline_id, Projects.getProjectStatus(project_offline_id));
            componentViewMap.put(COMPONENT_PROJECT_TASKS, projectTasksView);
            changeComponentView(componentViewMap.get(COMPONENT_PROJECT_TASKS));
            CloudCheetahApp.currentProjectComponent = COMPONENT_PROJECT_TASKS;
        }
    }

    @OnClick(R.id.delete_project)
    void deleteProject(){
        projectMembersLayout.setSelected(false);
        projectDetailsLayout.setSelected(false);
        projectResourcesLayout.setSelected(false);
        projectTasksLayout.setSelected(false);
        deleteProjectLayout.setSelected(true);
        updateProjectLayout.setSelected(false);
        submitProjectLayout.setSelected(false);
        projectDrawer.closeDrawer(GravityCompat.END);
    }

    @OnClick(R.id.update_project)
    void updateProject(){
        projectMembersLayout.setSelected(false);
        projectDetailsLayout.setSelected(false);
        projectResourcesLayout.setSelected(false);
        projectTasksLayout.setSelected(false);
        deleteProjectLayout.setSelected(false);
        updateProjectLayout.setSelected(true);
        submitProjectLayout.setSelected(false);
        projectDrawer.closeDrawer(GravityCompat.END);
        Bundle bundle = new Bundle();
        bundle.putString("project_id", ""+project_id);
        bundle.putString("project_offline_id",""+project_offline_id);
        ProjectUpdateFragment projectUpdateFragment = new ProjectUpdateFragment();
        projectUpdateFragment.setArguments(bundle);
        MainActivity.replaceFragment(projectUpdateFragment, TAG);
    }

    @OnClick(R.id.submit_project)
    void submitProject(){
        projectMembersLayout.setSelected(false);
        projectDetailsLayout.setSelected(false);
        projectResourcesLayout.setSelected(false);
        projectTasksLayout.setSelected(false);
        deleteProjectLayout.setSelected(false);
        updateProjectLayout.setSelected(false);
        submitProjectLayout.setSelected(true);
        projectDrawer.closeDrawer(GravityCompat.END);
        Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
        ProjectBatchProcess projectBatchProcess = new ProjectBatchProcess();
        projectBatchProcess.setName(projects.getName());
        projectBatchProcess.setObjectives(projects.getObjectives());
        projectBatchProcess.setDescription(projects.getDescription());
        projectBatchProcess.setBudget(projects.getBudget());
        projectBatchProcess.setId(project_id);
        projectBatchProcess.setStart_date(projects.getStart_date());
        projectBatchProcess.setEnd_date(projects.getEnd_date());
        projectBatchProcess.setProject_manager_id(Users.getUserId(projects.getProject_manager()));
        projectBatchProcess.setProject_sponsor_id(Users.getUserId(projects.getProject_sponsor()));
        List<TaskBatchProcess> taskBatchProcessList = new ArrayList<>();
        List<Tasks> tasksList = Tasks.getTasksByOfflineId(project_offline_id);
        for(Tasks tasks : tasksList){
            TaskBatchProcess taskBatchProcess = new TaskBatchProcess();
            taskBatchProcess.setTask_offline_id(tasks.getId());
            taskBatchProcess.setTask_parent_offline_id(tasks.getParent_offline_id());
            taskBatchProcess.setParent_task_name(tasks.getTask_parent_name());
            taskBatchProcess.setName(tasks.getName());
            taskBatchProcess.setStart_date(tasks.getStart_date());
            taskBatchProcess.setEnd_date(tasks.getEnd_date());
            taskBatchProcess.setBudget(tasks.getBudget());
            taskBatchProcess.setDescription(tasks.getDescription());
            taskBatchProcess.setPerson_responsible_id(tasks.getPerson_responsible_id());
            taskBatchProcess.setDuration(tasks.getDuration());
            taskBatchProcess.setId(tasks.getTask_id());
            List<TaskResources> taskResourcesList = TaskResources.getTaskResourcesOffline(tasks.getId());
            List<TaskBatchResources> taskBatchResourcesList = new ArrayList<>();
            for(TaskResources taskResources : taskResourcesList){
                TaskBatchResources taskBatchResources = new TaskBatchResources();
                taskBatchResources.setQuantity(taskResources.getQuantity());
                taskBatchResources.setResource_id(taskResources.getResource_id());
                taskBatchResources.setTask_offline_id(taskResources.getTask_offline_id());
                taskBatchResources.setId(taskResources.getTask_id());
                taskBatchResourcesList.add(taskBatchResources);
            }
            taskBatchProcess.setTask_resources(taskBatchResourcesList);
            taskBatchProcessList.add(taskBatchProcess);
        }
        projectBatchProcess.setTasks(taskBatchProcessList);
        List<ProjectBatchMembers> projectBatchMembersList = new ArrayList<>();
        List<ProjectMembers> projectMembersList = ProjectMembers.getProjectOfflineMembers(project_offline_id);
        for(ProjectMembers projectMembers : projectMembersList){
            ProjectBatchMembers projectBatchMembers = new ProjectBatchMembers();
            projectBatchMembers.setUser_id(projectMembers.getUser_id());
            projectBatchMembersList.add(projectBatchMembers);
        }
        projectBatchProcess.setProject_members(projectBatchMembersList);
        List<ProjectBatchResources> projectBatchResourcesList = new ArrayList<>();
        List<ProjectResources> projectResourcesList = ProjectResources.getResourcesOffline(project_offline_id);
        for(ProjectResources projectResources : projectResourcesList){
            ProjectBatchResources projectBatchResources = new ProjectBatchResources();
            projectBatchResources.setResource_id(projectResources.getResource_id());
            projectBatchResources.setQuantity(projectResources.getQuantity());
            projectBatchResourcesList.add(projectBatchResources);
        }
        projectBatchProcess.setProject_resources(projectBatchResourcesList);
        Gson gson = new Gson();
        String json = gson.toJson(projectBatchProcess);
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Processing...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            if(project_id == 0){
                observable = cloudCheetahAPIService.processOfflineProject(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID), sessionKey, json);
            }
            else{
                observable = cloudCheetahAPIService.updateOfflineProject(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID), sessionKey, json);
            }
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<ResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            MainActivity.popFragment();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ResponseWrapper responseWrapper) {
                            if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
                                projects.setProject_id(responseWrapper.getProject_id());
                                projects.setStatus(AccountGeneral.STATUS_SYNC);
                                projects.save();
                                List<Tasks> tasksList = Tasks.getTasksByOfflineId(project_offline_id);
                                for(Tasks tasks : tasksList){
                                    new Delete().from(TaskResources.class).where("task_offline_id = ?", tasks.getId()).execute();
                                }
                                new Delete().from(Tasks.class).where("project_offline_id = ?", project_offline_id).execute();
                                List<ProjectMembers> projectMembersList = ProjectMembers.getProjectOfflineMembers(project_offline_id);
                                for(ProjectMembers projectMembers : projectMembersList){
                                    projectMembers.setProject_id(responseWrapper.getProject_id());
                                    projectMembers.save();
                                }
                                List<ProjectResources> projectResourcesList = ProjectResources.getResourcesOffline(project_offline_id);
                                for(ProjectResources projectResources : projectResourcesList){
                                    projectResources.setProject_id(responseWrapper.getProject_id());
                                    projectResources.save();
                                }
                                ProjectsFragment.projectAdapter.notifyDataSetChanged();
                                Toast.makeText(ApplicationContext.get(), "Project successfully submitted.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(ApplicationContext.get(), "Operation failed please connect to a network .", Toast.LENGTH_SHORT).show();
        }
        Log.e("Json", json);
    }

    public void changeComponentView(View view){
        projectComponentsContainer.removeAllViews();
        projectComponentsContainer.addView(view);
    }

    public ProjectsComponentsContainerFragment() {
        super();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CloudCheetahApp.currentProjectComponent = "";
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
