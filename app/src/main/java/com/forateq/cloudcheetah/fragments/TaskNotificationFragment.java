package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.CustomersAdapter;
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PC1 on 8/27/2016.
 */
public class TaskNotificationFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
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
    Tasks tasks;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_notification_fragment, container, false);
        task_id = getArguments().getInt("task_id");
        tasks = Tasks.getTaskById(task_id);
        return v;
    }

    public void init(){
        taskNameET.setText(tasks.getName());
        taskStartDateET.setText(tasks.getStart_date());
        taskEndDateET.setText(tasks.getEnd_date());
        taskBudgetET.setText(""+tasks.getBudget());
        taskDetailsET.setText(tasks.getDescription());
        personResponsibleET.setText(Users.getUser(tasks.getPerson_responsible_id()).getFull_name());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }


    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }

}
