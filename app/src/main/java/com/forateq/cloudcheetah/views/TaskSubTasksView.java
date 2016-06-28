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
import com.forateq.cloudcheetah.adapters.SubTaskAdapter;
import com.forateq.cloudcheetah.fragments.AddTaskFragment;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.melnykov.fab.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This view is used to display the subtasks of a specific task or subtask
 * Created by Vallejos Family on 5/30/2016.
 */
public class TaskSubTasksView extends CardView {

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_sub_tasks)
    RecyclerView listSubTasks;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private LinearLayoutManager mLinearLayoutManager;
    public static SubTaskAdapter subTaskAdapter;
    private int project_id;
    private int parent_id;
    private long project_offline_id;
    private long task_offline_id;
    public static final String TAG = "TaskSubTasksView";

    public TaskSubTasksView(Context context, int project_id, int parent_id, long project_offline_id, long task_offline_id) {
        super(context);
        this.project_id = project_id;
        this.parent_id = parent_id;
        this.project_offline_id = project_offline_id;
        this.task_offline_id = task_offline_id;
        init();
    }

    public TaskSubTasksView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskSubTasksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.task_subtask_view, this);
        ButterKnife.bind(this);
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        subTaskAdapter = new SubTaskAdapter(Tasks.getTasksOffline(project_offline_id, task_offline_id), ApplicationContext.get());
        listSubTasks.setAdapter(subTaskAdapter);
        listSubTasks.setLayoutManager(mLinearLayoutManager);
        listSubTasks.setItemAnimator(new DefaultItemAnimator());
        fab.attachToRecyclerView(listSubTasks);
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
    public void addTaskSubTask(){
        Bundle bundle = new Bundle();
        bundle.putString("project_id", ""+project_id);
        bundle.putString("parent_task_id", ""+parent_id);
        bundle.putString("project_offline_id", ""+project_offline_id);
        bundle.putString("parent_task_offline_id", ""+task_offline_id);
        AddTaskFragment addTaskFragment = new AddTaskFragment();
        addTaskFragment.setArguments(bundle);
        MainActivity.replaceFragment(addTaskFragment, TAG);
    }
}
