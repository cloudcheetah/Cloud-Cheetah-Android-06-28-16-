package com.forateq.cloudcheetah.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    public static final String TAG = "ERPFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.erp_fragment, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    public ERPFragment() {
        super();
    }

    @OnClick(R.id.projects)
    public void getProjects(){
        Log.e(TAG, "Getting projects...");
        ProjectsFragment projectsFragment = new ProjectsFragment();
        MainActivity.replaceFragment(projectsFragment, TAG);
    }

    @OnClick(R.id.task_progress)
    public void getTasks(){
        Log.e(TAG, "Getting tasks...");
        SharedPreferences prefs = ApplicationContext.get().getSharedPreferences(AccountGeneral.ACCOUNT_NAME, Context.MODE_PRIVATE);
        Log.e(TAG, prefs.getString(AccountGeneral.ACCOUNT_USERNAME, ""));
        ProjectsComponentsContainerFragment.ProgressReportFragment progressReportFragment = new ProjectsComponentsContainerFragment.ProgressReportFragment();
        MainActivity.replaceFragment(progressReportFragment, TAG);
    }

    @OnClick(R.id.accounting_banking)
    public void getAccounts(){
        Log.e(TAG, "Getting accounts...");
    }

    @OnClick(R.id.payees)
    public void getVendors(){
        Log.e(TAG, "Getting vendors...");
    }

    @OnClick(R.id.labor)
    public void getEmployees(){
        Log.e(TAG, "Getting employees...");
    }

    @OnClick(R.id.resources)
    public void getResource(){
        Log.e(TAG, "Getting resources...");
    }

    @OnClick(R.id.payers)
    public void getSales(){
        Log.e(TAG, "Getting sales...");
    }

    @OnClick(R.id.reports)
    public void getReports(){
        Log.e(TAG, "Getting reports...");
    }


}
