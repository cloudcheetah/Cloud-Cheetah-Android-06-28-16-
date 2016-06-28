package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.TaskResources;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.TaskResourcesView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This fragment is used to add new task resource for a specific task
 * Created by Vallejos Family on 5/31/2016.
 */
public class AddTaskResourceFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.resource_name)
    Spinner resourceNameSP;
    @Bind(R.id.quantity)
    EditText quantityET;
    @Bind(R.id.ripple_add)
    MaterialRippleLayout rippleAdd;
    int task_id;
    long task_offline_id;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_project_resource_fragment, container, false);
        ButterKnife.bind(this, v);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Resources.getAllResourcesList());
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resourceNameSP.setAdapter(nameAdapter);
        task_id = Integer.parseInt(getArguments().getString("task_id"));
        task_offline_id = Long.parseLong(getArguments().getString("task_offline_id"));
        return v;
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.ripple_add)
    public void addResource(){
        TaskResources taskResources = new TaskResources();
        taskResources.setTask_offline_id(task_offline_id);
        taskResources.setTask_id(task_id);
        taskResources.setQuantity(Integer.parseInt(quantityET.getText().toString()));
        taskResources.setTask_resource_id(0);
        taskResources.setResource_id(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
        taskResources.save();
        Toast.makeText(ApplicationContext.get(), "Resource successfully added.", Toast.LENGTH_SHORT).show();
        TaskResourcesView.taskResourceAdapter.addItem(taskResources);
        MainActivity.popFragment();
    }

}
