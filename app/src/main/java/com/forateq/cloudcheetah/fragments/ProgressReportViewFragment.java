package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.pojo.AddResource;
import com.forateq.cloudcheetah.pojo.AddResourceWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.ResourceRowView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This fragment is used to display the selected progress report
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
    @Bind(R.id.attachment_one)
    TextView attachment1TV;
    @Bind(R.id.attachment_two)
    TextView attachment2TV;
    @Bind(R.id.attachment_three)
    TextView attachment3TV;
    int taskProgressId;
    long taskProgressOfflineId;
    String taskStatus;
    TaskProgressReports taskProgressReports;
    Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_progress_report_view, container, false);
        taskStatus = getArguments().getString("status");
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

    public ProgressReportViewFragment() {
        super();
    }


    public void init() {
        gson = new Gson();
        addResourceIV.setVisibility(View.GONE);
        if (taskStatus.equals("Submitted")) {
            taskProgressReports = TaskProgressReports.getProgressReportById(taskProgressId);
        } else {
            taskProgressReports = TaskProgressReports.getProgressOfflineReportById(taskProgressOfflineId);
        }
        taskStatusET.setText(taskProgressReports.getTask_status());
        reportDateET.setText(taskProgressReports.getReport_date());
        hoursWorkET.setText("" + taskProgressReports.getHours_work());
        percentCompletionET.setText("" + taskProgressReports.getPercent_completion());
        taskActionET.setText(taskProgressReports.getTask_action());
        taskNameET.setText(taskProgressReports.getTask_name());
        taskNotesET.setText(taskProgressReports.getNotes());
        taskIssuesET.setText(taskProgressReports.getConcerns_issues());
        changeRequestET.setText(taskProgressReports.getChange_request());
        attachment1TV.setText(taskProgressReports.getAttachment_1());
        attachment2TV.setText(taskProgressReports.getAttachment_2());
        attachment3TV.setText(taskProgressReports.getAttachment_3());
        addReportButton.setVisibility(View.GONE);
        AddResourceWrapper addResourceWrapper = gson.fromJson(taskProgressReports.getResources_used(), AddResourceWrapper.class);
        for (AddResource addResource : addResourceWrapper.getResourceList()) {
            ResourceRowView resourceRowView = new ResourceRowView(getActivity());
            resourceRowView.getResourceNameTV().setText(addResource.getResourceName());
            resourceRowView.getResourceQuantityTV().setText("" + addResource.getResourceQuantity());
            resourceContainerLayout.addView(resourceRowView);
        }
    }

    @OnClick(R.id.ripple_back)
    public void back() {
        MainActivity.popFragment();
    }

    @OnClick(R.id.attachment_one)
    public void showAttachment1() {
        if (!attachment1TV.getText().toString().equals("")) {
            if (taskProgressReports.is_submitted()) {
                Log.e("URL", taskProgressReports.getAttachment_1());
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load("http://" + taskProgressReports.getAttachment_1()).placeholder(R.drawable.progress_animation).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 1")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        });
                final MaterialDialog addNoteDialog = createNoteDialog.build();
                addNoteDialog.show();
            } else {
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load(new File(attachment1TV.getText().toString())).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 1")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        });
                final MaterialDialog addNoteDialog = createNoteDialog.build();
                addNoteDialog.show();
            }
        }
    }

    @OnClick(R.id.attachment_two)
    public void showAttachment2() {
        if (!attachment2TV.getText().toString().equals("")) {
            if (taskProgressReports.is_submitted()) {
                Log.e("URL", taskProgressReports.getAttachment_2());
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load("http://" + taskProgressReports.getAttachment_2()).placeholder(R.drawable.progress_animation).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 2")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        });
                final MaterialDialog addNoteDialog = createNoteDialog.build();
                addNoteDialog.show();
            } else {
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load(new File(attachment2TV.getText().toString())).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 2")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        });
                final MaterialDialog addNoteDialog = createNoteDialog.build();
                addNoteDialog.show();
            }
        }
    }

    @OnClick(R.id.attachment_three)
    public void showAttachment3() {
        if (!attachment3TV.getText().toString().equals("")) {
            if (taskProgressReports.is_submitted()) {
                final ImageView imageView = new ImageView(getActivity());
                Log.e("URL", taskProgressReports.getAttachment_3());
                Picasso.with(ApplicationContext.get()).load("http://" + taskProgressReports.getAttachment_3()).placeholder(R.drawable.progress_animation).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 3")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        });
                final MaterialDialog addNoteDialog = createNoteDialog.build();
                addNoteDialog.show();
            } else {
                final ImageView imageView = new ImageView(getActivity());
                Picasso.with(ApplicationContext.get()).load(new File(attachment3TV.getText().toString())).resize(500, 500)
                        .centerCrop().into(imageView);
                final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                        .title("Attachment 3")
                        .titleColorRes(R.color.colorText)
                        .backgroundColorRes(R.color.colorPrimary)
                        .widgetColorRes(R.color.colorText)
                        .customView(imageView, true)
                        .positiveText("Ok")
                        .positiveColorRes(R.color.colorText)
                        .negativeColorRes(R.color.colorText)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        });
                final MaterialDialog addNoteDialog = createNoteDialog.build();
                addNoteDialog.show();
            }
        }
    }

}
