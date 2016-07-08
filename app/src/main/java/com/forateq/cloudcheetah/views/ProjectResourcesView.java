package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.ProjectResourcesAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.AddResourceFragment;
import com.forateq.cloudcheetah.models.ProjectResources;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This view is used to display the resources of a specific project
 * Created by Vallejos Family on 5/19/2016.
 */
public class ProjectResourcesView extends RelativeLayout{

    @Bind(R.id.list_resources)
    RecyclerView listResources;
    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private LinearLayoutManager mLinearLayoutManager;
    public static ProjectResourcesAdapter projectResourcesAdapter;
    private int project_id;
    private long project_offline_id;
    public static final String TAG = "ProjectResourcesView";


    public ProjectResourcesView(Context context, int project_id, long project_offline_id) {
        super(context);
        this.project_id = project_id;
        this.project_offline_id = project_offline_id;
        init();
    }

    public ProjectResourcesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProjectResourcesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.project_resources_view, this);
        ButterKnife.bind(this);
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        if(!Projects.getProjectStatus(project_offline_id).equals(AccountGeneral.STATUS_SYNC)){
            projectResourcesAdapter = new ProjectResourcesAdapter(ProjectResources.getResourcesOffline(project_offline_id), ApplicationContext.get());
        }
        else{
            List<ProjectResources> projectResourcesList = ProjectResources.getResources(project_id);
            for(ProjectResources projectResources : projectResourcesList){
                projectResources.setProject_offline_id(project_offline_id);
                projectResources.save();
            }
            projectResourcesAdapter = new ProjectResourcesAdapter(projectResourcesList, ApplicationContext.get());
        }
        listResources.setAdapter(projectResourcesAdapter);
        listResources.setLayoutManager(mLinearLayoutManager);
        listResources.setItemAnimator(new DefaultItemAnimator());
        fab.attachToRecyclerView(listResources);
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
                String searchString = searchEditText.getText().toString();
                projectResourcesAdapter.clearItems();
                if(!Projects.getProjectStatus(project_offline_id).equals(AccountGeneral.STATUS_SYNC)){
                    for(ProjectResources projectResources : ProjectResources.getSearchProjectResourcesOffline(searchString, project_offline_id)){
                        projectResourcesAdapter.addItem(projectResources);
                    }
                }
                else{
                    for(ProjectResources projectResources : ProjectResources.getSearchProjectResourcesOnline(searchString, project_id)){
                        projectResourcesAdapter.addItem(projectResources);
                    }
                }
            }
        });
    }

    @OnClick(R.id.fab)
    public void addResource(){
        Bundle bundle = new Bundle();
        bundle.putString("project_id", ""+project_id);
        bundle.putString("project_offline_id", ""+project_offline_id);
        AddResourceFragment addResourceFragment = new AddResourceFragment();
        addResourceFragment.setArguments(bundle);
        MainActivity.replaceFragment(addResourceFragment, TAG);
    }
}
