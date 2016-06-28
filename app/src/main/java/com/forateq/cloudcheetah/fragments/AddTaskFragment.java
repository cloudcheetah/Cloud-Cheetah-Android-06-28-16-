package com.forateq.cloudcheetah.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.ProjectTasksView;
import com.forateq.cloudcheetah.views.TaskSubTasksView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This fragment is used to add new task for the selected project
 * Created by Vallejos Family on 5/25/2016.
 */
public class AddTaskFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.task_name)
    EditText taskNameET;
    @Bind(R.id.task_start_date)
    EditText startDateET;
    @Bind(R.id.task_end_date)
    EditText endDateET;
    @Bind(R.id.task_budget)
    EditText budgetET;
    @Bind(R.id.task_details)
    EditText taskDetailsET;
    @Bind(R.id.person_responsible)
    Spinner personResponsibleSP;
    @Bind(R.id.ripple_add)
    MaterialRippleLayout rippleAdd;
    private int project_id;
    private int parent_task_id;
    private long project_offline_id;
    private long parent_task_offline_id;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_task_fragment, container, false);
        ButterKnife.bind(this, v);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Users.getUsersNames());
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personResponsibleSP.setAdapter(nameAdapter);
        project_offline_id = Long.parseLong(getArguments().getString("project_offline_id"));
        project_id = Integer.parseInt(getArguments().getString("project_id"));
        parent_task_id = Integer.parseInt(getArguments().getString("parent_task_id"));
        parent_task_offline_id = Long.parseLong(getArguments().getString("parent_task_offline_id"));
        return v;
    }

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

    @OnClick(R.id.task_start_date)
    public void setStartDate(){
        setDate(startDateET);
    }

    @OnClick(R.id.task_end_date)
    public void setEndDaTe(){
        setDate(endDateET);
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.ripple_add)
    public void addTask(){
        Tasks tasks = new Tasks();
        tasks.setName(taskNameET.getText().toString());
        tasks.setStart_date(startDateET.getText().toString());
        tasks.setEnd_date(endDateET.getText().toString());
        tasks.setBudget(Double.parseDouble(budgetET.getText().toString()));
        tasks.setDescription(taskDetailsET.getText().toString());
        tasks.setPerson_responsible_id(Users.getUserId(personResponsibleSP.getSelectedItem().toString()));
        tasks.setParent_id(parent_task_id);
        tasks.setParent_offline_id(parent_task_offline_id);
        tasks.setProject_id(project_id);
        tasks.setProject_offline_id(project_offline_id);
        tasks.setLongitide(0.0);
        tasks.setLatitude(0.0);
        tasks.save();
        if(parent_task_offline_id == 0){
            ProjectTasksView.taskAdapter.addItem(tasks);
        }
        else{
            TaskSubTasksView.subTaskAdapter.addItem(tasks);
        }
        Toast.makeText(ApplicationContext.get(), "Task successfully added.", Toast.LENGTH_SHORT).show();
        MainActivity.popFragment();
    }

}
