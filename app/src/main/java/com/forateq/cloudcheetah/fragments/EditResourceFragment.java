package com.forateq.cloudcheetah.fragments;

/**
 * Created by PC1 on 7/12/2016.
 */

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
import com.forateq.cloudcheetah.models.ProjectResources;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.views.ProjectResourcesView;
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
 * This fragment is used to edit project resources for a fragment or task
 * Created by Vallejos Family on 5/26/2016.
 */
public class EditResourceFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.resource_name)
    Spinner resourceNameSP;
    @Bind(R.id.quantity)
    EditText quantityET;
    @Bind(R.id.ripple_add)
    MaterialRippleLayout rippleAdd;
    int project_id;
    long project_offline_id;
    int project_resource_id;
    int resource_id;
    ProjectResources projectResources;
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
        project_id = Integer.parseInt(getArguments().getString("project_id"));
        project_offline_id = Long.parseLong(getArguments().getString("project_offline_id"));
        project_resource_id = Integer.parseInt(getArguments().getString("project_resource_id"));
        resource_id = Integer.parseInt(getArguments().getString("resource_id"));
        projectResources = ProjectResources.getProjectResource(project_resource_id);
        quantityET.setText(""+projectResources.getQuantity());
        int selectionPositionResource = nameAdapter.getPosition(projectResources.getResource_name());
        resourceNameSP.setSelection(selectionPositionResource);
        resourceNameSP.setEnabled(false);
        return v;
    }

    @OnClick(R.id.ripple_add)
    public void addResource(){
        if(Resources.getResource(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString())).getOn_hand_qty() < Integer.parseInt(quantityET.getText().toString())){
            Toast.makeText(ApplicationContext.get(), "Insufficient quantity for item "+ resourceNameSP.getSelectedItem().toString() + "on-hand quantity is "+Resources.getResource(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString())).getOn_hand_qty(), Toast.LENGTH_SHORT).show();
        }
        else{
            if(!Projects.getProjectStatus(project_offline_id).equals(AccountGeneral.STATUS_SYNC)){
                Resources resources = Resources.getResource(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
                resources.setOn_hand_qty(resources.getOn_hand_qty() + projectResources.getQuantity());
                resources.save();
                resources.setOn_hand_qty(resources.getOn_hand_qty() - Integer.parseInt(quantityET.getText().toString()));
                resources.save();
                projectResources.setProject_id(project_id);
                projectResources.setProject_offline_id(project_offline_id);
                projectResources.setQuantity(Integer.parseInt(quantityET.getText().toString()));
                projectResources.setResource_id(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
                projectResources.setResource_name(resourceNameSP.getSelectedItem().toString());
                projectResources.save();
                ProjectResourcesView.projectResourcesAdapter.addItem(projectResources);
                Toast.makeText(ApplicationContext.get(), "Resource successfully added.", Toast.LENGTH_SHORT).show();
                MainActivity.popFragment();
            }
            else{
                if(isNetworkAvailable()){
                    com.forateq.cloudcheetah.pojo.ProjectResources pojoProjectResources = new com.forateq.cloudcheetah.pojo.ProjectResources();
                    pojoProjectResources.setProject_id(project_id);
                    pojoProjectResources.setResource_id(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
                    pojoProjectResources.setQty(Integer.parseInt(quantityET.getText().toString()));
                    pojoProjectResources.setId(project_resource_id);
                    Gson gson = new Gson();
                    String json = gson.toJson(pojoProjectResources);
                    Log.e("Json", json);
                    final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Updating project resources...");
                    mProgressDialog.show();
                    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                    String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                    String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                    Observable<ResponseWrapper> observable = cloudCheetahAPIService.addProjectResource(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
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
                                    Resources resources = Resources.getResource(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
                                    resources.setOn_hand_qty(resources.getOn_hand_qty() + projectResources.getQuantity());
                                    resources.save();
                                    resources.setOn_hand_qty(resources.getOn_hand_qty() - Integer.parseInt(quantityET.getText().toString()));
                                    resources.save();
                                    projectResources.setProject_id(project_id);
                                    projectResources.setProject_offline_id(project_offline_id);
                                    projectResources.setQuantity(Integer.parseInt(quantityET.getText().toString()));
                                    projectResources.setResource_name(resourceNameSP.getSelectedItem().toString());
                                    projectResources.setResource_id(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
                                    projectResources.setProject_resource_id(responseWrapper.getId());
                                    projectResources.save();
                                    ProjectResourcesView.projectResourcesAdapter.addItem(projectResources);
                                    Toast.makeText(ApplicationContext.get(), "Resource successfully updated.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Resources resources = Resources.getResource(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
                    resources.setOn_hand_qty(resources.getOn_hand_qty() + projectResources.getQuantity());
                    resources.save();
                    resources.setOn_hand_qty(resources.getOn_hand_qty() - Integer.parseInt(quantityET.getText().toString()));
                    resources.save();
                    projectResources.setProject_id(project_id);
                    projectResources.setProject_offline_id(project_offline_id);
                    projectResources.setQuantity(Integer.parseInt(quantityET.getText().toString()));
                    projectResources.setResource_id(Resources.getAllResourceId(resourceNameSP.getSelectedItem().toString()));
                    projectResources.setResource_name(resourceNameSP.getSelectedItem().toString());
                    projectResources.save();
                    Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
                    projects.setStatus(AccountGeneral.STATUS_UNSYNC);
                    projects.save();
                    Toast.makeText(ApplicationContext.get(), "You currently don't have a network connection all changes is saved in the device. Kindly sync the project manually once the network is connected.", Toast.LENGTH_SHORT).show();
                    MainActivity.popFragment();
                }
            }
        }
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
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

