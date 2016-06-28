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
import com.forateq.cloudcheetah.adapters.ProjectMembersAdapter;
import com.forateq.cloudcheetah.fragments.AddProjectmemberFragment;
import com.forateq.cloudcheetah.models.ProjectMembers;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.melnykov.fab.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This view is used to display the members of a specific project
 * Created by Vallejos Family on 5/19/2016.
 */
public class ProjectMembersView extends RelativeLayout {

    @Bind(R.id.list_members)
    RecyclerView listMembers;
    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private LinearLayoutManager mLinearLayoutManager;
    private ProjectMembersAdapter projectMembersAdapter;
    private int project_id;
    private long project_offline_id;
    public static final String TAG = "ProjectMembersView";

    public ProjectMembersView(Context context, int project_id, long project_offline_id) {
        super(context);
        this.project_id = project_id;
        this.project_offline_id = project_offline_id;
        init();
    }

    public ProjectMembersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProjectMembersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.project_members_view, this);
        ButterKnife.bind(this);
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        projectMembersAdapter = new ProjectMembersAdapter(ProjectMembers.getProjectOfflineMembers(project_offline_id), ApplicationContext.get());
        listMembers.setLayoutManager(mLinearLayoutManager);
        listMembers.setAdapter(projectMembersAdapter);
        listMembers.setItemAnimator(new DefaultItemAnimator());
        fab.attachToRecyclerView(listMembers);
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
            }
        });
    }

    @OnClick(R.id.fab)
    public void addMembers(){
        Bundle bundle = new Bundle();
        bundle.putString("project_id", ""+project_id);
        bundle.putString("project_offline_id", ""+project_offline_id);
        AddProjectmemberFragment addProjectmemberFragment = new AddProjectmemberFragment();
        addProjectmemberFragment.setArguments(bundle);
        MainActivity.replaceFragment(addProjectmemberFragment, TAG);
    }
}
