package com.forateq.cloudcheetah.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.MyTasksAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.ProjectDetailsView;
import com.forateq.cloudcheetah.views.ProjectMembersView;
import com.forateq.cloudcheetah.views.ProjectResourcesView;
import com.forateq.cloudcheetah.views.ProjectTasksView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.projects_info_fragment, container, false);
        ButterKnife.bind(this, v);
        project_id = Integer.parseInt(getArguments().getString("project_id"));
        project_offline_id = Long.parseLong(getArguments().getString("project_offline_id"));
        projectDetailsLayout.setSelected(true);
        projectDetailsView = new ProjectDetailsView(ApplicationContext.get(), project_id, project_offline_id);
        componentViewMap.put(COMPONENT_PROJECT_DETAILS, projectDetailsView);
        projectResourcesView = new ProjectResourcesView(ApplicationContext.get(), project_id, project_offline_id);
        componentViewMap.put(COMPONENT_PROJECT_RESOURCES, projectResourcesView);
        projectMembersView = new ProjectMembersView(ApplicationContext.get(), project_id, project_offline_id);
        componentViewMap.put(COMPONENT_PROJECT_MEMBERS, projectMembersView);
        projectTasksView = new ProjectTasksView(ApplicationContext.get(), project_id, project_offline_id);
        componentViewMap.put(COMPONENT_PROJECT_TASKS, projectTasksView);
        if(!CloudCheetahApp.currentProjectComponent.equals("")){
            changeComponentView(componentViewMap.get(CloudCheetahApp.currentProjectComponent));
        }
        else{
            changeComponentView(componentViewMap.get(COMPONENT_PROJECT_DETAILS));
            CloudCheetahApp.currentProjectComponent = COMPONENT_PROJECT_DETAILS;
        }
        return v;
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
        projectDrawer.closeDrawer(GravityCompat.END);
        changeComponentView(componentViewMap.get(COMPONENT_PROJECT_RESOURCES));
        CloudCheetahApp.currentProjectComponent = COMPONENT_PROJECT_RESOURCES;
    }

    @OnClick(R.id.project_tasks)
    void showProjectTasks(){
        projectMembersLayout.setSelected(false);
        projectDetailsLayout.setSelected(false);
        projectResourcesLayout.setSelected(false);
        projectTasksLayout.setSelected(true);
        deleteProjectLayout.setSelected(false);
        projectDrawer.closeDrawer(GravityCompat.END);
        changeComponentView(componentViewMap.get(COMPONENT_PROJECT_TASKS));
        CloudCheetahApp.currentProjectComponent = COMPONENT_PROJECT_TASKS;
    }

    @OnClick(R.id.delete_project)
    void deleteProject(){
        projectMembersLayout.setSelected(false);
        projectDetailsLayout.setSelected(false);
        projectResourcesLayout.setSelected(false);
        projectTasksLayout.setSelected(false);
        deleteProjectLayout.setSelected(true);
        projectDrawer.closeDrawer(GravityCompat.END);
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
     * Created by Vallejos Family on 6/10/2016.
     */
    public static class ProgressReportFragment extends Fragment {

        @Bind(R.id.ripple_back)
        MaterialRippleLayout backRipple;
        @Bind(R.id.search)
        EditText searchEditText;
        @Bind(R.id.list_tasks)
        RecyclerView listTasks;
        public static MyTasksAdapter myTasksAdapter;
        private LinearLayoutManager mLinearLayoutManager;
        public static final String TAG = "ProgressReportFragment";


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.progress_report_fragment, container, false);
            return v;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            ButterKnife.bind(this, view);
            init();
        }

        public void init(){
            SharedPreferences prefs = ApplicationContext.get().getSharedPreferences(AccountGeneral.ACCOUNT_NAME, Context.MODE_PRIVATE);
            myTasksAdapter = new MyTasksAdapter(Tasks.getTasksByPersonResponsibleId(Users.getUserByUserId(prefs.getString(AccountGeneral.ACCOUNT_USERNAME, ""))), ApplicationContext.get());
            mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
            listTasks.setLayoutManager(mLinearLayoutManager);
            listTasks.setAdapter(myTasksAdapter);
            listTasks.setItemAnimator(new DefaultItemAnimator());
            setSearchForTasks();
        }

        public void setSearchForTasks(){
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        @OnClick(R.id.ripple_back)
        public void back(){
            MainActivity.popFragment();
        }

    }
}
