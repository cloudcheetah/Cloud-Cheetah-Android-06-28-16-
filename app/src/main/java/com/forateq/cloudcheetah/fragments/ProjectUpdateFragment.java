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
import android.widget.Button;
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
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.ProjectResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Vallejos Family on 6/30/2016.
 */
public class ProjectUpdateFragment extends Fragment {

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
    @Bind(R.id.update_project_button)
    Button updateProjectButton;
    @Bind(R.id.ripple_back_to_details)
    MaterialRippleLayout rippleLayout;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    int project_id;
    long project_offline_id;
    Projects projects;
    public static final String TAG = "ProjectUpdateFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.project_update_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        project_id = Integer.parseInt(getArguments().getString("project_id"));
        project_offline_id = Long.parseLong(getArguments().getString("project_offline_id"));
        projects = Projects.getProjectsOfflineMode(project_offline_id);
        projectNameET.setText(projects.getName());
        projectStartDateET.setText(projects.getStart_date());
        projectStartDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(projectStartDateET);
            }
        });
        projectEnddateET.setText(projects.getEnd_date());
        projectEnddateET.setOnClickListener(new View.OnClickListener() {
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
        int selectionPositionManager= nameAdapter.getPosition(projects.getProject_manager());
        projectManagerSpinner.setSelection(selectionPositionManager);
        projectSponsorSpinner.setAdapter(nameAdapter);
        int selectionPositionSponsor= nameAdapter.getPosition(projects.getProject_sponsor());
        projectSponsorSpinner.setSelection(selectionPositionSponsor);
        return v;
    }

    @OnClick(R.id.ripple_back_to_details)
    void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.project_start_date)
    void setStartDate(){
        setDate(projectStartDateET);
    }

    @OnClick(R.id.project_end_date)
    void setEndDate(){
        setDate(projectEnddateET);
    }

    @OnClick(R.id.update_project_button)
    void updateProject(){
        Log.e("Update", "Clicked");
        if(!Projects.getProjectStatus(project_offline_id).equals(AccountGeneral.STATUS_SYNC)){
            Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
            projects.setName(projectNameET.getText().toString());
            projects.setLongitide(0.0);
            projects.setLatitude(0.0);
            projects.setStart_date(projectStartDateET.getText().toString());
            projects.setEnd_date(projectEnddateET.getText().toString());
            projects.setBudget(Double.parseDouble(projectBudgetET.getText().toString()));
            projects.setDescription(projectDetailsET.getText().toString());
            projects.setObjectives(projectObjectivesET.getText().toString());
            projects.setProject_manager(projectManagerSpinner.getSelectedItem().toString());
            projects.setProject_sponsor(projectSponsorSpinner.getSelectedItem().toString());
            projects.setStatus(AccountGeneral.STATUS_UNSYNC);
            projects.save();
            MainActivity.popFragment();
            MainActivity.popFragment();
        }
        else{
            if(isNetworkAvailable()){
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Updating...");
                mProgressDialog.show();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                Observable<ProjectResponseWrapper> observable = cloudCheetahAPIService.updateProject(project_id, projectNameET.getText().toString(), projectStartDateET.getText().toString(), projectEnddateET.getText().toString(), Double.parseDouble(projectBudgetET.getText().toString()), projectDetailsET.getText().toString(), projectObjectivesET.getText().toString(), "0.0", "0.0", Users.getUserId(projectManagerSpinner.getSelectedItem().toString()), Users.getUserId(projectSponsorSpinner.getSelectedItem().toString()), userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                        Settings.Secure.ANDROID_ID), sessionKey, AccountGeneral.METHOD_PUT);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<ProjectResponseWrapper>() {
                            @Override
                            public void onCompleted() {
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                                MainActivity.popFragment();
                                MainActivity.popFragment();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG,  e.getMessage(), e);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onNext(ProjectResponseWrapper projectResponseWrapper) {
                                if(projectResponseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                    Projects projects = Projects.getProjectById(project_id);
                                    projects.setName(projectNameET.getText().toString());
                                    projects.setLongitide(0.0);
                                    projects.setLatitude(0.0);
                                    projects.setStart_date(projectStartDateET.getText().toString());
                                    projects.setEnd_date(projectEnddateET.getText().toString());
                                    projects.setBudget(Double.parseDouble(projectBudgetET.getText().toString()));
                                    projects.setDescription(projectDetailsET.getText().toString());
                                    projects.setObjectives(projectObjectivesET.getText().toString());
                                    projects.setProject_manager(projectManagerSpinner.getSelectedItem().toString());
                                    projects.setProject_sponsor(projectSponsorSpinner.getSelectedItem().toString());
                                    projects.setStatus(AccountGeneral.STATUS_SYNC);
                                    projects.save();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(ApplicationContext.get(),"You currently don't have a network connection. Changes have been saved in offline mode.", Toast.LENGTH_SHORT).show();
                Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
                projects.setName(projectNameET.getText().toString());
                projects.setLongitide(0.0);
                projects.setLatitude(0.0);
                projects.setStart_date(projectStartDateET.getText().toString());
                projects.setEnd_date(projectEnddateET.getText().toString());
                projects.setBudget(Double.parseDouble(projectBudgetET.getText().toString()));
                projects.setDescription(projectDetailsET.getText().toString());
                projects.setObjectives(projectObjectivesET.getText().toString());
                projects.setProject_manager(projectManagerSpinner.getSelectedItem().toString());
                projects.setProject_sponsor(projectSponsorSpinner.getSelectedItem().toString());
                projects.setStatus(AccountGeneral.STATUS_UNSYNC);
                projects.save();
                MainActivity.popFragment();
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
