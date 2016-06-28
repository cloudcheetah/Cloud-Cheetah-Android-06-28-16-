package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.ProjectAdapter;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This fragment is used to display the projects of the current user of the app
 * Created by Vallejos Family on 5/13/2016.
 */
public class ProjectsFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_projects)
    RecyclerView listProjects;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    public static ProjectAdapter projectAdapter;
    public static final String TAG = "ProjectsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.projects_fragment, container, false);
        return v;
    }

    public void init(){
        projectAdapter = new ProjectAdapter(Projects.getProjects(), ApplicationContext.get());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listProjects.setAdapter(projectAdapter);
        listProjects.setLayoutManager(mLinearLayoutManager);
        listProjects.setItemAnimator(new DefaultItemAnimator());
        setSearchForProjects();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public ProjectsFragment() {
        super();
    }

    @OnClick(R.id.fab)
    void addNewProject(){
        Log.e(TAG, "Add new Project");
        AddProjectFragment addProjectFragment = new AddProjectFragment();
        MainActivity.replaceFragment(addProjectFragment, TAG);
    }

    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }

    public void setSearchForProjects(){
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
                projectAdapter.clearItems();
                for(Projects projects : Projects.getSearchProject(searchString)){
                    projectAdapter.addItem(projects);
                }
            }
        });
    }

}
