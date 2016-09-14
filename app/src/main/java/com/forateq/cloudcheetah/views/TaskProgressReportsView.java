package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.forateq.cloudcheetah.adapters.TaskProgressAdapter;
import com.forateq.cloudcheetah.fragments.AddTaskProgressReportFragment;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.melnykov.fab.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This view is used to display the details of a progress report
 * Created by Vallejos Family on 6/14/2016.
 */
public class TaskProgressReportsView extends RelativeLayout {

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_task_progress_reports)
    RecyclerView listProgressReports;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    long task_offline_id;
    int task_id;
    String taskName;
    public static TaskProgressAdapter taskProgressAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String TAG = "TaskProgressReportsView";

    public TaskProgressReportsView(Context context, long task_offline_id, int task_id, String taskName) {
        super(context);
        this.task_offline_id = task_offline_id;
        this.task_id = task_id;
        this.taskName = taskName;
        init();
    }

    public TaskProgressReportsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskProgressReportsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.task_progress_reports_list_view, this);
        ButterKnife.bind(this);
        if(isNetworkAvailable()){
            taskProgressAdapter = new TaskProgressAdapter(TaskProgressReports.getProgressReports(task_id), ApplicationContext.get());
        }
        else{
            taskProgressAdapter = new TaskProgressAdapter(TaskProgressReports.getProgressOfflineReports(task_offline_id), ApplicationContext.get());
        }

        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listProgressReports.setAdapter(taskProgressAdapter);
        listProgressReports.setLayoutManager(mLinearLayoutManager);
        listProgressReports.setItemAnimator(new DefaultItemAnimator());
        setSearchForTasksReports();
    }

    @OnClick(R.id.fab)
    public void addProgressReport(){
        Bundle bundle = new Bundle();
        bundle.putString("task_offline_id", ""+task_offline_id);
        bundle.putString("task_id", ""+task_id);
        bundle.putString("task_name", taskName);
        AddTaskProgressReportFragment addTaskProgressReportFragment = new AddTaskProgressReportFragment();
        addTaskProgressReportFragment.setArguments(bundle);
        MainActivity.replaceFragment(addTaskProgressReportFragment, TAG);
    }

    public void setSearchForTasksReports(){
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

    /**
     * Checks if there is a network available before login
     *
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ApplicationContext.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
