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
import android.widget.LinearLayout;

import com.activeandroid.query.Delete;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.MyTasks;
import com.forateq.cloudcheetah.models.ProjectMembers;
import com.forateq.cloudcheetah.models.ProjectResources;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.MyTasksResponseWrapper;
import com.forateq.cloudcheetah.pojo.Projects;
import com.forateq.cloudcheetah.pojo.ProjectListResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** This fragment is used to display all the ERP components of the app
 * Created by Vallejos Family on 5/11/2016.
 */
public class ERPFragment extends Fragment {

    @Bind(R.id.projects)
    LinearLayout projectsLayout;
    @Bind(R.id.task_progress)
    LinearLayout taskInProgressLayout;
    @Bind(R.id.accounting_banking)
    LinearLayout accountingLayout;
    @Bind(R.id.payees)
    LinearLayout vendorsLayout;
    @Bind(R.id.labor)
    LinearLayout employeesLayout;
    @Bind(R.id.resources)
    LinearLayout resourcesLayout;
    @Bind(R.id.payers)
    LinearLayout customerSalesLayout;
    @Bind(R.id.reports)
    LinearLayout reportsLayout;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    public static final String TAG = "ERPFragment";
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.erp_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        return v;
    }

    public ERPFragment() {
        super();
    }

    @OnClick(R.id.projects)
    public void getProjects(){
        Log.e(TAG, "Getting projects...");
        mProgressDialog.show();
        if(isNetworkAvailable()){
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String projectTimeStamp = sharedPreferences.getString(AccountGeneral.PROJECT_TIMESTAMP, "");
            Log.e("Credentials", sessionKey+" "+userName+" "+Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID) + " "+projectTimeStamp);
            Observable<ProjectListResponseWrapper> observable = cloudCheetahAPIService.getAllProjects(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID), sessionKey, projectTimeStamp);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<ProjectListResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            ProjectsFragment projectsFragment = new ProjectsFragment();
                            MainActivity.replaceFragment(projectsFragment, TAG);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG,  e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(ProjectListResponseWrapper projectListResponseWrapper) {
                            if(projectListResponseWrapper.getResponse().getStatus_code() == 1007){
                                for(Projects projects : projectListResponseWrapper.getData()){
                                    Log.e("Project Name", ""+projects.getName());
                                    Log.e("Project Id", ""+projects.getId());
                                    com.forateq.cloudcheetah.models.Projects projectsDB = com.forateq.cloudcheetah.models.Projects.getProjectById(projects.getId());
                                    if(projectsDB != null){
                                        projectsDB.setName(projects.getName());
                                        projectsDB.setProject_manager(projects.getProject_manager());
                                        projectsDB.setProject_sponsor(projects.getProject_sponsor());
                                        projectsDB.setObjectives(projects.getObjectives());
                                        projectsDB.setDescription(projects.getDescription());
                                        projectsDB.setBudget(projects.getBudget());
                                        projectsDB.setEnd_date(projects.getEnd_date());
                                        projectsDB.setStart_date(projects.getStart_date());
                                        projectsDB.setLatitude(projects.getLatitude());
                                        projectsDB.setLongitide(projects.getLongitude());
                                        projectsDB.setProject_id(projects.getId());
                                        projectsDB.setStatus(AccountGeneral.STATUS_SYNC);
                                        projectsDB.setProject_tasks_timestamp("");
                                        new Delete().from(ProjectMembers.class).where("project_id = ?", projects.getId()).execute();
                                        for(com.forateq.cloudcheetah.pojo.ProjectMembers projectMembers : projects.getMembers()){
                                            ProjectMembers newProjectMembers = new ProjectMembers();
                                            newProjectMembers.setUser_id(projectMembers.getMember_id());
                                            newProjectMembers.setProject_member_name(Users.getUser(projectMembers.getMember_id()).getFull_name());
                                            newProjectMembers.setProject_id(projectMembers.getProject_id());
                                            newProjectMembers.save();
                                        }
                                        new Delete().from(ProjectResources.class).where("project_id = ?", projects.getId()).execute();
                                        for(com.forateq.cloudcheetah.pojo.ProjectResources projectResources : projects.getResources()){
                                            ProjectResources newProjectResources = new ProjectResources();
                                            newProjectResources.setResource_id(projectResources.getResource_id());
                                            newProjectResources.setProject_id(projectResources.getProject_id());
                                            newProjectResources.setQuantity(projectResources.getQty());
                                            newProjectResources.setProject_resource_id(projectResources.getId());
                                            newProjectResources.setResource_name(Resources.getResource(projectResources.getResource_id()).getName());
                                            newProjectResources.save();
                                        }
                                        projectsDB.save();
                                    }
                                    else{
                                        com.forateq.cloudcheetah.models.Projects newProjects = new com.forateq.cloudcheetah.models.Projects();
                                        newProjects.setName(projects.getName());
                                        newProjects.setProject_manager(projects.getProject_manager());
                                        newProjects.setProject_sponsor(projects.getProject_sponsor());
                                        newProjects.setObjectives(projects.getObjectives());
                                        newProjects.setDescription(projects.getDescription());
                                        newProjects.setBudget(projects.getBudget());
                                        newProjects.setEnd_date(projects.getEnd_date());
                                        newProjects.setStart_date(projects.getStart_date());
                                        newProjects.setLatitude(projects.getLatitude());
                                        newProjects.setLongitide(projects.getLongitude());
                                        newProjects.setProject_id(projects.getId());
                                        newProjects.setStatus(AccountGeneral.STATUS_SYNC);
                                        newProjects.setProject_tasks_timestamp("");
                                        for(com.forateq.cloudcheetah.pojo.ProjectMembers projectMembers : projects.getMembers()){
                                            ProjectMembers newProjectMembers = new ProjectMembers();
                                            newProjectMembers.setUser_id(projectMembers.getMember_id());
                                            newProjectMembers.setProject_id(projectMembers.getProject_id());
                                            newProjectMembers.setProject_member_name(Users.getUser(projectMembers.getMember_id()).getFull_name());
                                            newProjectMembers.save();
                                        }
                                        for(com.forateq.cloudcheetah.pojo.ProjectResources projectResources : projects.getResources()){
                                            ProjectResources newProjectResources = new ProjectResources();
                                            newProjectResources.setResource_id(projectResources.getResource_id());
                                            newProjectResources.setProject_id(projectResources.getProject_id());
                                            newProjectResources.setQuantity(projectResources.getQty());
                                            newProjectResources.setProject_resource_id(projectResources.getId());
                                            newProjectResources.setResource_name(Resources.getResource(projectResources.getResource_id()).getName());
                                            newProjectResources.save();
                                        }
                                        newProjects.save();
                                    }
                                }
                            }
                            else{
                                Log.e("Status Code", ""+projectListResponseWrapper.getResponse().getStatus_code());
                            }
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(AccountGeneral.PROJECT_TIMESTAMP, projectListResponseWrapper.getTimestamp());
                            editor.apply();
                        }
                    });
        }
        else{
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
            ProjectsFragment projectsFragment = new ProjectsFragment();
            MainActivity.replaceFragment(projectsFragment, TAG);
        }
    }

    @OnClick(R.id.task_progress)
    public void getTasks(){
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Getting my tasks...");
            mProgressDialog.show();
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String myTaskTimeStamp = sharedPreferences.getString(AccountGeneral.MY_TASKS_TIMESTAMP, "");
            Observable<MyTasksResponseWrapper> observable = cloudCheetahAPIService.getMyTasks(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID), sessionKey, myTaskTimeStamp);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<MyTasksResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            SharedPreferences prefs = ApplicationContext.get().getSharedPreferences(AccountGeneral.ACCOUNT_NAME, Context.MODE_PRIVATE);
                            Log.e(TAG, prefs.getString(AccountGeneral.ACCOUNT_USERNAME, ""));
                            ProgressReportFragment progressReportFragment = new ProgressReportFragment();
                            MainActivity.replaceFragment(progressReportFragment, TAG);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("GettingTasks", e.getMessage(), e);
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onNext(MyTasksResponseWrapper myTasksResponseWrapper) {
                            Log.e("Size", ""+myTasksResponseWrapper.getData().size());
                            for(MyTasks myTasks : myTasksResponseWrapper.getData()){
                                if(MyTasks.getMyTask(myTasks.getTaskId()) == null){
                                    myTasks.save();
                                }
                            }

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(AccountGeneral.MY_TASKS_TIMESTAMP, myTasksResponseWrapper.getTimestamp());
                            editor.apply();
                        }
                    });
        }
        else{
            Log.e(TAG, "Getting tasks...");
            SharedPreferences prefs = ApplicationContext.get().getSharedPreferences(AccountGeneral.ACCOUNT_NAME, Context.MODE_PRIVATE);
            Log.e(TAG, prefs.getString(AccountGeneral.ACCOUNT_USERNAME, ""));
            ProgressReportFragment progressReportFragment = new ProgressReportFragment();
            MainActivity.replaceFragment(progressReportFragment, TAG);
        }

    }

    @OnClick(R.id.accounting_banking)
    public void getAccounts(){
        Log.e(TAG, "Getting accounts...");
        AccountsFragment accountsFragment = new AccountsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("parent_id", 0);
        accountsFragment.setArguments(bundle);
        MainActivity.replaceFragment(accountsFragment, TAG);
    }

    @OnClick(R.id.payees)
    public void getVendors(){
        Log.e(TAG, "Getting vendors...");
        VendorsFragment vendorsFragment = new VendorsFragment();
        MainActivity.replaceFragment(vendorsFragment, TAG);
    }

    @OnClick(R.id.labor)
    public void getEmployees(){
        Log.e(TAG, "Getting employees...");
    }

    @OnClick(R.id.resources)
    public void getResource(){
        Log.e(TAG, "Getting resources...");
        InventoryFragment inventoryFragment = new InventoryFragment();
        MainActivity.replaceFragment(inventoryFragment, TAG);
    }

    @OnClick(R.id.payers)
    public void getSales(){
        Log.e(TAG, "Getting sales...");
        CustomersFragment customersFragment = new CustomersFragment();
        MainActivity.replaceFragment(customersFragment, TAG);
    }

    @OnClick(R.id.reports)
    public void getReports(){
        Log.e(TAG, "Getting reports...");
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
