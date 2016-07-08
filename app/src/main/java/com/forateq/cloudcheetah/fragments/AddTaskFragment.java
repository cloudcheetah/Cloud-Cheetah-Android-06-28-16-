
package com.forateq.cloudcheetah.fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.TaskResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.ProjectTasksView;
import com.forateq.cloudcheetah.views.TaskSubTasksView;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @Bind(R.id.task_duration)
    EditText durationET;
    @Bind(R.id.task_details)
    EditText taskDetailsET;
    @Bind(R.id.person_responsible)
    Spinner personResponsibleSP;
    @Bind(R.id.ripple_add)
    MaterialRippleLayout rippleAdd;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    private int project_id;
    private int parent_task_id;
    private long project_offline_id;
    private long parent_task_offline_id;
    private String parent_task_name;
    public static final String TAG = "AddTaskFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_task_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        ArrayAdapter<String> nameAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item, Users.getUsersNames());
        nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personResponsibleSP.setAdapter(nameAdapter);
        project_offline_id = Long.parseLong(getArguments().getString("project_offline_id"));
        project_id = Integer.parseInt(getArguments().getString("project_id"));
        parent_task_id = Integer.parseInt(getArguments().getString("parent_task_id"));
        parent_task_offline_id = Long.parseLong(getArguments().getString("parent_task_offline_id"));
        parent_task_name = getArguments().getString("parent_task_name");
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

        if(isNetworkAvailable()){
            if(!Projects.getProjectStatus(project_offline_id).equals(AccountGeneral.STATUS_SYNC)){
                Tasks tasks = new Tasks();
                tasks.setName(taskNameET.getText().toString());
                tasks.setStart_date(startDateET.getText().toString());
                tasks.setEnd_date(endDateET.getText().toString());
                tasks.setBudget(Double.parseDouble(budgetET.getText().toString()));
                tasks.setDuration(Integer.parseInt(durationET.getText().toString()));
                tasks.setDescription(taskDetailsET.getText().toString());
                tasks.setPerson_responsible_id(Users.getUserId(personResponsibleSP.getSelectedItem().toString()));
                tasks.setParent_id(parent_task_id);
                tasks.setParent_offline_id(parent_task_offline_id);
                tasks.setProject_id(project_id);
                tasks.setProject_offline_id(project_offline_id);
                tasks.setLongitide(0.0);
                tasks.setLatitude(0.0);
                tasks.setTask_parent_name(parent_task_name);
                tasks.save();
                Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
                projects.setStatus(AccountGeneral.STATUS_UNSYNC);
                projects.save();
                if(parent_task_offline_id == 0){
                    ProjectTasksView.taskAdapter.addItem(tasks);
                }
                else{
                    TaskSubTasksView.subTaskAdapter.addItem(tasks);
                }
                Toast.makeText(ApplicationContext.get(), "Task successfully added.", Toast.LENGTH_SHORT).show();
                MainActivity.popFragment();
            }
            else{
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Adding task...");
                mProgressDialog.show();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                Observable<TaskResponseWrapper> observable = cloudCheetahAPIService.createTask(taskNameET.getText().toString(), startDateET.getText().toString(), endDateET.getText().toString(), budgetET.getText().toString(), taskDetailsET.getText().toString(), "0.0", "0.0", durationET.getText().toString(), Users.getUserId(personResponsibleSP.getSelectedItem().toString()), project_id, 0, userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID), sessionKey);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<TaskResponseWrapper>() {
                            @Override
                            public void onCompleted() {
                                if(mProgressDialog.isShowing()){
                                  mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG,  e.getMessage(), e);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onNext(TaskResponseWrapper taskResponseWrapper) {
                                if(taskResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                    Tasks tasks = new Tasks();
                                    tasks.setTask_id(taskResponseWrapper.getData().getId());
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
                                    tasks.setDuration(Integer.parseInt(durationET.getText().toString()));
                                    tasks.setTask_parent_name(parent_task_name);
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
                                else{

                                }
                            }
                        });
            }
        }
        else{
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
            tasks.setDuration(Integer.parseInt(durationET.getText().toString()));
            tasks.setTask_parent_name(parent_task_name);
            tasks.save();
            Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
            projects.setStatus(AccountGeneral.STATUS_UNSYNC);
            projects.save();
            if(parent_task_offline_id == 0){
                ProjectTasksView.taskAdapter.addItem(tasks);
            }
            else{
                TaskSubTasksView.subTaskAdapter.addItem(tasks);
            }
            Toast.makeText(ApplicationContext.get(), "You currently don't have a network connection all changes is saved in the device. Kindly sync the project manually once the network is connected.", Toast.LENGTH_SHORT).show();
            MainActivity.popFragment();
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
