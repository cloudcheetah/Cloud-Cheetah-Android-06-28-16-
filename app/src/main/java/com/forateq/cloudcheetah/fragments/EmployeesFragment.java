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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.ContactAdapter;
import com.forateq.cloudcheetah.adapters.EmployeesAdapter;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Employees;
import com.forateq.cloudcheetah.models.Users;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PC1 on 8/4/2016.
 */
public class EmployeesFragment extends Fragment {

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_employees)
    RecyclerView listEmployees;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    public static EmployeesAdapter employeesAdapter;
    public static final String TAG = "EmployeesFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.employees_fragment, container, false);
        return v;
    }

    public void init(){
        Log.e("Size", ""+ Users.getUsers().size());
        employeesAdapter = new EmployeesAdapter(Employees.getAllEmployees(), getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        listEmployees.setAdapter(employeesAdapter);
        listEmployees.setLayoutManager(mLinearLayoutManager);
        listEmployees.setItemAnimator(new DefaultItemAnimator());
        setSearchEmployees();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public EmployeesFragment() {
        super();
    }

    public void setSearchEmployees(){
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchString = searchEditText.getText().toString();
                employeesAdapter.clearItems();
                for(Employees employees : Employees.searchEmployees(searchString)){
                    employeesAdapter.addItem(employees);
                }
            }
        });
    }

    @OnClick(R.id.fab)
    void addNewEmployee(){
        Log.e(TAG, "Add new Employee");
        AddEmployeeFragment addEmployeeFragment = new AddEmployeeFragment();
        MainActivity.replaceFragment(addEmployeeFragment, TAG);
    }

    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }


}
