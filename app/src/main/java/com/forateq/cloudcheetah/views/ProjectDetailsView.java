package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Projects;

import butterknife.Bind;
import butterknife.ButterKnife;

/** This view is used to display the details of a specific project
 * Created by Vallejos Family on 5/19/2016.
 */
public class ProjectDetailsView extends RelativeLayout {

    @Bind(R.id.project_name)
    EditText projectNameEditText;
    @Bind(R.id.project_start_date)
    EditText projectStartDateEditText;
    @Bind(R.id.project_end_date)
    EditText projectEndDateEditText;
    @Bind(R.id.budget)
    EditText projectBudgetEditText;
    @Bind(R.id.project_details)
    EditText projectDetailsEditText;
    @Bind(R.id.project_objectives)
    EditText projectObjectivesEditText;
    @Bind(R.id.project_sponsor)
    EditText projectSponsorEditText;
    @Bind(R.id.project_manager)
    EditText projectManagerEditText;
    int project_id;
    long project_offline_id;

    public ProjectDetailsView(Context context, int project_id, long project_offline_id) {
        super(context);
        this.project_id = project_id;
        this.project_offline_id = project_offline_id;
        init();
    }

    public ProjectDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProjectDetailsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.project_details_view, this);
        ButterKnife.bind(this);
        Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
        projectNameEditText.setText(projects.getName());
        projectStartDateEditText.setText(projects.getStart_date());
        projectEndDateEditText.setText(projects.getEnd_date());
        projectBudgetEditText.setText(""+projects.getBudget());
        projectDetailsEditText.setText(projects.getDescription());
        projectObjectivesEditText.setText(projects.getObjectives());
        projectSponsorEditText.setText(projects.getProject_sponsor());
        projectManagerEditText.setText(projects.getProject_manager());
    }
}
