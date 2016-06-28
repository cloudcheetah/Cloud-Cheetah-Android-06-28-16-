package com.forateq.cloudcheetah.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Users;

import butterknife.Bind;
import butterknife.ButterKnife;

/** This view is used to display the details of a specific task
 * Created by Vallejos Family on 5/30/2016.
 */
public class TaskDetailsView extends RelativeLayout {

    @Bind(R.id.task_name)
    EditText taskNameET;
    @Bind(R.id.task_start_date)
    EditText taskStartDateET;
    @Bind(R.id.task_end_date)
    EditText taskEndDateET;
    @Bind(R.id.task_budget)
    EditText taskBudgetET;
    @Bind(R.id.task_details)
    EditText taskDetailsET;
    @Bind(R.id.person_responsible)
    EditText personResponsibleET;
    int task_id;
    long task_offline_id;

    public TaskDetailsView(Context context, int task_id, long task_offline_id) {
        super(context);
        this.task_id = task_id;
        this.task_offline_id = task_offline_id;
        init();
    }

    public TaskDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskDetailsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.task_details_view, this);
        ButterKnife.bind(this);
        Tasks tasks = Tasks.getTaskByOfflineId(task_offline_id);
        taskNameET.setText(tasks.getName());
        taskStartDateET.setText(tasks.getStart_date());
        taskEndDateET.setText(tasks.getEnd_date());
        taskBudgetET.setText(""+tasks.getBudget());
        taskDetailsET.setText(tasks.getDescription());
        personResponsibleET.setText(Users.getUser(tasks.getPerson_responsible_id()).getFull_name());
    }
}
