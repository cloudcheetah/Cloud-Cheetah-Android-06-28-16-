package com.forateq.cloudcheetah.views;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.NothingSelectedSpinnerAdapter;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Users;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Vallejos Family on 6/30/2016.
 */
public class ProjectUpdateView extends RelativeLayout {

    @Bind(R.id.project_name)
    EditText projectNameET;
    @Bind(R.id.project_start_date)
    EditText projectStartDateET;
    @Bind(R.id.project_end_date)
    EditText projectEnddateET;
    @Bind(R.id.budget)
    EditText projectBudgetET;
    @Bind(R.id.project_details)
    EditText projectDetailsET;
    @Bind(R.id.project_objectives)
    EditText projectObjectivesET;
    @Bind(R.id.project_sponsor)
    Spinner projectSponsorSpinner;
    @Bind(R.id.project_manager)
    Spinner projectManagerSpinner;
    @Bind(R.id.update_project)
    Button updateProjectButton;
    int project_id;
    long project_offline_id;

    public ProjectUpdateView(Context context, int project_id, long project_offline_id) {
        super(context);
        this.project_id = project_id;
        this.project_offline_id = project_offline_id;
        init();
    }

    public ProjectUpdateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProjectUpdateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.project_update_fragment, this);
        ButterKnife.bind(this);
        Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
        projectNameET.setText(projects.getName());
        projectStartDateET.setText(projects.getStart_date());
        projectStartDateET.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(projectStartDateET);
            }
        });
        projectEnddateET.setText(projects.getEnd_date());
        projectEnddateET.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(projectEnddateET);
            }
        });
        projectBudgetET.setText(""+projects.getBudget());
        projectDetailsET.setText(projects.getDescription());
        projectObjectivesET.setText(projects.getObjectives());
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Users.getUsersNames());
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectManagerSpinner.setAdapter(nameAdapter);
        projectManagerSpinner.setPrompt(projects.getProject_manager());
        projectManagerSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        nameAdapter,
                        R.layout.nothing_selected,
                        getContext()));
        projectSponsorSpinner.setAdapter(nameAdapter);
        projectSponsorSpinner.setPrompt(projects.getProject_sponsor());
        projectSponsorSpinner.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        nameAdapter,
                        R.layout.nothing_selected,
                        getContext()));
    }

    /**
     * This method is used to display and set the date of a selected the selected edittext
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
