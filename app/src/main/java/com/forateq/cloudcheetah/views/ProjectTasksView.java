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
import android.widget.EditText;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.TaskAdapter;
import com.forateq.cloudcheetah.fragments.AddTaskFragment;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.melnykov.fab.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This view is used to display all the tasks of a specific project
 * Created by Vallejos Family on 5/19/2016.
 */
public class ProjectTasksView extends CardView {

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_tasks)
    RecyclerView listTasks;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private LinearLayoutManager mLinearLayoutManager;
    public static TaskAdapter taskAdapter;
    private int project_id;
    private long project_offline_id;
    public static final String TAG = "ProjectTasksView";

    public ProjectTasksView(Context context, int project_id, long project_offline_id) {
        super(context);
        this.project_id = project_id;
        this.project_offline_id = project_offline_id;
        init();
    }

    public ProjectTasksView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProjectTasksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.project_tasks_view, this);
        ButterKnife.bind(this);
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        taskAdapter = new TaskAdapter(Tasks.getTasksOffline(project_offline_id, 0), ApplicationContext.get());
        listTasks.setAdapter(taskAdapter);
        listTasks.setLayoutManager(mLinearLayoutManager);
        listTasks.setItemAnimator(new DefaultItemAnimator());
        fab.attachToRecyclerView(listTasks);
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

    @OnClick(R.id.fab)
    public void addTask(){
        Bundle bundle = new Bundle();
        bundle.putString("project_id", ""+project_id);
        bundle.putString("parent_task_id", ""+0);
        bundle.putString("project_offline_id", ""+project_offline_id);
        bundle.putString("parent_task_offline_id", ""+0);
        AddTaskFragment addTaskFragment = new AddTaskFragment();
        addTaskFragment.setArguments(bundle);
        MainActivity.replaceFragment(addTaskFragment, TAG);
    }
}
