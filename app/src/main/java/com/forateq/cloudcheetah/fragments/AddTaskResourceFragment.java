package com.forateq.cloudcheetah.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.TaskResources;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.pojo.AddTaskResources;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.TaskResourcesView;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    String project_status;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_project_resource_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Resources.getAllResourcesList());
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resourceNameSP.setAdapter(nameAdapter);
        task_id = Integer.parseInt(getArguments().getString("task_id"));
        task_offline_id = Long.parseLong(getArguments().getString("task_offline_id"));
        project_status = getArguments().getString("project_status");
        return v;
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.ripple_add)
    public void addResource(){

        if(!project_status.equals(AccountGeneral.STATUS_SYNC)){
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
        else{
           if(isNetworkAvailable()){
               AddTaskResources addTaskResources = new AddTaskResources();
               addTaskResources.setTask_id(task_id);
               addTaskResources.setResource_id(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
               addTaskResources.setQty(Integer.parseInt(quantityET.getText().toString()));
               Gson gson = new Gson();
               String json = gson.toJson(addTaskResources);
               final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
               mProgressDialog.setIndeterminate(true);
               mProgressDialog.setMessage("Adding task resources...");
               mProgressDialog.show();
               final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
               String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
               String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
               Observable<ResponseWrapper> observable = cloudCheetahAPIService.addTaskResource(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                       Settings.Secure.ANDROID_ID), sessionKey, json);
               observable.subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
                       .unsubscribeOn(Schedulers.io())
                       .subscribe(new Subscriber<ResponseWrapper>() {
                           @Override
                           public void onCompleted() {
                               if(mProgressDialog.isShowing()){
                                   mProgressDialog.dismiss();
                               }
                               MainActivity.popFragment();
                           }

                           @Override
                           public void onError(Throwable e) {
                               Log.e("AddProjectResources", e.getMessage(), e);
                               if(mProgressDialog.isShowing()){
                                   mProgressDialog.dismiss();
                               }
                           }

                           @Override
                           public void onNext(ResponseWrapper responseWrapper) {
                               Log.e("Task Id", ""+task_id);
                               TaskResources taskResources = new TaskResources();
                               taskResources.setTask_offline_id(task_offline_id);
                               taskResources.setTask_id(task_id);
                               taskResources.setQuantity(Integer.parseInt(quantityET.getText().toString()));
                               taskResources.setTask_resource_id(responseWrapper.getId());
                               taskResources.setResource_id(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
                               taskResources.save();
                               Toast.makeText(ApplicationContext.get(), "Resource successfully added.", Toast.LENGTH_SHORT).show();
                               TaskResourcesView.taskResourceAdapter.addItem(taskResources);
                           }
                       });
           }
            else{
               TaskResources taskResources = new TaskResources();
               taskResources.setTask_offline_id(task_offline_id);
               taskResources.setTask_id(task_id);
               taskResources.setQuantity(Integer.parseInt(quantityET.getText().toString()));
               taskResources.setTask_resource_id(0);
               taskResources.setResource_id(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
               taskResources.save();
               Toast.makeText(ApplicationContext.get(), "Resource successfully added.", Toast.LENGTH_SHORT).show();
               TaskResourcesView.taskResourceAdapter.addItem(taskResources);
               Projects projects = Projects.getProjectsOfflineMode(Tasks.getTaskByOfflineId(task_offline_id).getProject_offline_id());
               projects.setStatus(AccountGeneral.STATUS_UNSYNC);
               projects.save();
               MainActivity.popFragment();
           }
        }


    }

    /**
     * Checks if there is a network available before login
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
