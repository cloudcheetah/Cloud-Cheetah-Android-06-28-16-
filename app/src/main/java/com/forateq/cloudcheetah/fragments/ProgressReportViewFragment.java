package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.pojo.AddResource;
import com.forateq.cloudcheetah.pojo.AddResourceWrapper;
import com.forateq.cloudcheetah.views.ResourceRowView;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This fragment is used to display the selected progress report
 * Created by Vallejos Family on 6/13/2016.
 */
public class ProgressReportViewFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.task_name)
    EditText taskNameET;
    @Bind(R.id.task_status)
    EditText taskStatusET;
    @Bind(R.id.report_date)
    EditText reportDateET;
    @Bind(R.id.hours_work)
    EditText hoursWorkET;
    @Bind(R.id.percent_completion)
    EditText percentCompletionET;
    @Bind(R.id.add_resource)
    ImageView addResourceIV;
    @Bind(R.id.resource_container)
    LinearLayout resourceContainerLayout;
    @Bind(R.id.task_action)
    EditText taskActionET;
    @Bind(R.id.task_notes)
    EditText taskNotesET;
    @Bind(R.id.task_issues)
    EditText taskIssuesET;
    @Bind(R.id.change_request)
    EditText changeRequestET;
    @Bind(R.id.add_progress_report)
    Button addReportButton;
    int taskProgressId;
    long taskProgressOfflineId;
    TaskProgressReports taskProgressReports;
    Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_progress_report_view, container, false);
        taskProgressId = Integer.parseInt(getArguments().getString("task_progress_id"));
        taskProgressOfflineId = Long.parseLong(getArguments().getString("task_progress_offline_id"));
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void init(){
        gson = new Gson();
        addResourceIV.setVisibility(View.GONE);
        taskProgressReports = TaskProgressReports.getProgressOfflineReportById(taskProgressOfflineId);
        taskStatusET.setText(taskProgressReports.getTask_status());
        reportDateET.setText(taskProgressReports.getReport_date());
        hoursWorkET.setText(""+taskProgressReports.getHours_work());
        percentCompletionET.setText(""+taskProgressReports.getPercent_completion());
        taskActionET.setText(taskProgressReports.getTask_action());
        taskNameET.setText(taskProgressReports.getTask_name());
        taskNotesET.setText(taskProgressReports.getNotes());
        taskIssuesET.setText(taskProgressReports.getConcerns_issues());
        changeRequestET.setText(taskProgressReports.getChange_request());
        addReportButton.setVisibility(View.GONE);
        AddResourceWrapper addResourceWrapper = gson.fromJson(taskProgressReports.getResources_used(), AddResourceWrapper.class);
        for(AddResource addResource : addResourceWrapper.getResourceList()){
            ResourceRowView resourceRowView = new ResourceRowView(getActivity());
            resourceRowView.getResourceNameTV().setText(addResource.getResourceName());
            resourceRowView.getResourceQuantityTV().setText(""+addResource.getResourceQuantity());
            resourceContainerLayout.addView(resourceRowView);
        }
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

}
