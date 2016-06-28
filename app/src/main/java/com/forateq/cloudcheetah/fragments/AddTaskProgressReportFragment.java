package com.forateq.cloudcheetah.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.pojo.AddResource;
import com.forateq.cloudcheetah.pojo.AddResourceWrapper;
import com.forateq.cloudcheetah.views.AddResourceView;
import com.forateq.cloudcheetah.views.ResourceRowView;
import com.forateq.cloudcheetah.views.TaskProgressReportsView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This fragment is used to add new Progress Report for a specific task
 * Created by Vallejos Family on 6/10/2016.
 */
public class AddTaskProgressReportFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.task_name)
    EditText taskNameET;
    @Bind(R.id.task_status)
    EditText taskStatusET;
    @Bind(R.id.report_date)
    EditText taskReportDateET;
    @Bind(R.id.hours_work)
    EditText hoursWorkET;
    @Bind(R.id.percent_completion)
    EditText percentCompletionET;
    @Bind(R.id.resource_container)
    LinearLayout resourceContainer;
    @Bind(R.id.task_action)
    Spinner actionSpinner;
    @Bind(R.id.task_notes)
    EditText taskNotesET;
    @Bind(R.id.task_issues)
    EditText taskIssuesET;
    @Bind(R.id.change_request)
    EditText changeRequestET;
    @Bind(R.id.add_progress_report)
    Button addProgressButton;
    @Bind(R.id.add_resource)
    ImageView addResourceIV;
    String taskName;
    long task_offline_id;
    int task_id;
    List<AddResource> addResourceList;
    Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_task_progress_report_fragment, container, false);
        ButterKnife.bind(this, v);
        List<String> actionList = new ArrayList<>();
        actionList.add("Start");
        actionList.add("Hold");
        actionList.add("Resume");
        actionList.add("Cancel");
        actionList.add("Complete");
        addResourceList = new ArrayList<>();
        taskName = getArguments().getString("task_name");
        task_id = Integer.parseInt(getArguments().getString("task_id"));
        task_offline_id = Long.parseLong(getArguments().getString("task_offline_id"));
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, actionList);
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskNameET.setText(taskName);
        actionSpinner.setAdapter(nameAdapter);
        gson = new Gson();
        return v;
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.report_date)
    public void setReportDate(){
        setDate(taskReportDateET);
    }

    @OnClick(R.id.add_resource)
    public void addResource(){
        final AddResourceView addResourceView = new AddResourceView(getActivity(), task_offline_id, task_id);
        final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                .title("Add resource")
                .titleColorRes(R.color.colorText)
                .backgroundColorRes(R.color.colorPrimary)
                .widgetColorRes(R.color.colorText)
                .customView(addResourceView, true)
                .positiveText("Ok")
                .positiveColorRes(R.color.colorText)
                .negativeText("Cancel")
                .negativeColorRes(R.color.colorText)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        ResourceRowView resourceRowView = new ResourceRowView(getActivity());
                        resourceRowView.getResourceNameTV().setText(addResourceView.getResourceNameSP().getSelectedItem().toString());
                        resourceRowView.getResourceQuantityTV().setText(addResourceView.getResourceQtyET().getText().toString());
                        AddResource addResource = new AddResource();
                        addResource.setResourceName(addResourceView.getResourceNameSP().getSelectedItem().toString());
                        addResource.setResourceQuantity(Integer.parseInt(addResourceView.getResourceQtyET().getText().toString()));
                        addResourceList.add(addResource);
                        resourceContainer.addView(resourceRowView);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                });
        final MaterialDialog addNoteDialog = createNoteDialog.build();
        addNoteDialog.show();
    }

    @OnClick(R.id.add_progress_report)
    public void addProgressReport(){
        TaskProgressReports taskProgressReports = new TaskProgressReports();
        taskProgressReports.setTask_offline_id(task_offline_id);
        taskProgressReports.setTask_id(0);
        taskProgressReports.setTask_name(taskNameET.getText().toString());
        taskProgressReports.setTask_status(taskStatusET.getText().toString());
        taskProgressReports.setChange_request(changeRequestET.getText().toString());
        taskProgressReports.setConcerns_issues(taskIssuesET.getText().toString());
        taskProgressReports.setHours_work(Integer.parseInt(hoursWorkET.getText().toString()));
        taskProgressReports.setNotes(taskNotesET.getText().toString());
        taskProgressReports.setPercent_completion(Integer.parseInt(percentCompletionET.getText().toString()));
        taskProgressReports.setReport_date(taskReportDateET.getText().toString());
        AddResourceWrapper addResourceWrapper = new AddResourceWrapper();
        addResourceWrapper.setResourceList(addResourceList);
        taskProgressReports.setResources_used(gson.toJson(addResourceWrapper));
        taskProgressReports.setTask_action(actionSpinner.getSelectedItem().toString());
        taskProgressReports.save();
        Toast.makeText(getActivity(), "Progress report successfully added.", Toast.LENGTH_SHORT).show();
        TaskProgressReportsView.taskProgressAdapter.addItem(taskProgressReports);
        MainActivity.popFragment();
    }

    /**
     * This method is used to set and select the date for the selected edittext in the form
     * @param editText
     */
    public void setDate(final EditText editText){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        editText.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

}
