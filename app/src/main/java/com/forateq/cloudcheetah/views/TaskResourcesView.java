package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.TaskResourceAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.AddTaskResourceFragment;
import com.forateq.cloudcheetah.models.TaskResources;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.melnykov.fab.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This view is used to display the resources of a specific task
 * Created by Vallejos Family on 5/31/2016.
 */
public class TaskResourcesView extends CardView {

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_task_resources)
    RecyclerView listTaskResources;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private LinearLayoutManager mLinearLayoutManager;
    public static TaskResourceAdapter taskResourceAdapter;
    int task_id;
    long task_offline_id;
    String project_status;
    public static final String TAG = "TaskResourcesView";

    public TaskResourcesView(Context context, int task_id, long task_offline_id, String project_status) {
        super(context);
        this.task_id = task_id;
        this.task_offline_id = task_offline_id;
        this.project_status = project_status;
        init();
    }

    public TaskResourcesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskResourcesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.task_resource_view, this);
        ButterKnife.bind(this);
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        if(!project_status.equals(AccountGeneral.STATUS_SYNC)){
            Log.e("UnSync", ""+TaskResources.getTaskResourcesOffline(task_offline_id).size());
            taskResourceAdapter = new TaskResourceAdapter(TaskResources.getTaskResourcesOffline(task_offline_id), ApplicationContext.get());
        }
        else{
            Log.e("Task Id View", ""+task_id);
            Log.e("Sync", ""+TaskResources.getTaskResourcesOnline(task_id).size());
            taskResourceAdapter = new TaskResourceAdapter(TaskResources.getTaskResourcesOnline(task_id), ApplicationContext.get());
        }
        listTaskResources.setAdapter(taskResourceAdapter);
        listTaskResources.setLayoutManager(mLinearLayoutManager);
        listTaskResources.setItemAnimator(new DefaultItemAnimator());
        fab.attachToRecyclerView(listTaskResources);
        setSearchForTaskResources();
    }

    public void setSearchForTaskResources(){
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

    @OnClick(R.id.fab)
    public void addTaskResources(){
        Bundle bundle = new Bundle();
        bundle.putString("task_id", ""+task_id);
        bundle.putString("task_offline_id", ""+task_offline_id);
        bundle.putString("project_status", project_status);
        AddTaskResourceFragment addTaskResourceFragment = new AddTaskResourceFragment();
        addTaskResourceFragment.setArguments(bundle);
        MainActivity.replaceFragment(addTaskResourceFragment, TAG);
    }
}
