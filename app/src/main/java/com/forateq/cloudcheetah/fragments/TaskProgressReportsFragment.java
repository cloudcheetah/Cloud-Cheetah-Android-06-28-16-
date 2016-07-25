package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.views.TaskCashInCashOutView;
import com.forateq.cloudcheetah.views.TaskProgressReportsView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This fragment is used to display all the progress reports of a specific task
 * Created by Vallejos Family on 6/10/2016.
 */
public class TaskProgressReportsFragment extends Fragment {

    @Bind(R.id.progress_ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.material_progress_drawer)
    MaterialRippleLayout rippleDrawer;
    @Bind(R.id.progress_drawer_layout)
    DrawerLayout progressDrawerLayout;
    @Bind(R.id.progress_reports_list)
    LinearLayout progressReportsLayout;
    @Bind(R.id.task_progress_reports_container)
    LinearLayout taskProgressReportsContainer;
    @Bind(R.id.cash_in_out)
    LinearLayout cashInOutLayout;
    public static final String TAG = "TaskProgressReportsFragment";
    long task_offline_id;
    int task_id;
    String taskName;
    public static final String PROGRESS_REPORTS_COMPONENT = "progress_reports";
    public static final String CASH_FLOW_COMPONENT = "cash_flow";
    Map<String, View> reportsComponentMap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_progress_reports_fragment, container, false);
        task_offline_id = Long.parseLong(getArguments().getString("task_offline_id"));
        task_id = Integer.parseInt(getArguments().getString("task_id"));
        taskName = getArguments().getString("task_name");
        reportsComponentMap = new HashMap<>();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void init(){
        TaskProgressReportsView taskProgressReportsView = new TaskProgressReportsView(getActivity(), task_offline_id, task_id, taskName);
        reportsComponentMap.put(PROGRESS_REPORTS_COMPONENT, taskProgressReportsView);
        TaskCashInCashOutView taskCashInCashOutView = new TaskCashInCashOutView(getActivity(), task_offline_id, task_id, taskName);
        reportsComponentMap.put(CASH_FLOW_COMPONENT, taskCashInCashOutView);
        if(CloudCheetahApp.currentReportComponent.equals("")){
            changeComponentView(taskProgressReportsView);
        }
        else{
            changeComponentView(reportsComponentMap.get(CloudCheetahApp.currentReportComponent));
        }

    }

    @OnClick(R.id.progress_ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.material_progress_drawer)
    public void openDrawer(){
        progressDrawerLayout.openDrawer(GravityCompat.END);
    }

    @OnClick(R.id.progress_reports_list)
    public void showProgressReports(){
        taskProgressReportsContainer.removeAllViews();
        TaskProgressReportsView taskProgressReportsView = new TaskProgressReportsView(getActivity(), task_offline_id, task_id, taskName);
        taskProgressReportsContainer.addView(taskProgressReportsView);
        progressReportsLayout.setSelected(true);
        cashInOutLayout.setSelected(false);
        progressDrawerLayout.closeDrawer(GravityCompat.END);
        CloudCheetahApp.currentReportComponent = PROGRESS_REPORTS_COMPONENT;
    }

    @OnClick(R.id.cash_in_out)
    public void showCashInOut(){
        taskProgressReportsContainer.removeAllViews();
        TaskCashInCashOutView taskCashInCashOutView = new TaskCashInCashOutView(getActivity(), task_offline_id, task_id, taskName);
        taskProgressReportsContainer.addView(taskCashInCashOutView);
        progressReportsLayout.setSelected(false);
        cashInOutLayout.setSelected(true);
        progressDrawerLayout.closeDrawer(GravityCompat.END);
        CloudCheetahApp.currentReportComponent = CASH_FLOW_COMPONENT;
    }

    void changeComponentView(View view){
        taskProgressReportsContainer.removeAllViews();
        taskProgressReportsContainer.addView(view);
    }

}
