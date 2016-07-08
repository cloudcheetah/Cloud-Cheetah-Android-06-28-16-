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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.AddProjectMemberAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.ProjectMembers;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.ProjectJsonMembers;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** This fragment is used to display the project members of the selected project
 * Created by Vallejos Family on 5/26/2016.
 */
public class AddProjectmemberFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout rippleBack;
    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_contacts)
    RecyclerView listContacts;
    @Bind(R.id.ripple_add)
    MaterialRippleLayout rippleAdd;
    int project_id;
    long project_offline_id;
    private LinearLayoutManager mLinearLayoutManager;
    AddProjectMemberAdapter addProjectMemberAdapter;
    public static final String TAG = "AddProjectmemberFragment";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_project_member_fragment, container, false);
        ButterKnife.bind(this, v);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        project_id = Integer.parseInt(getArguments().getString("project_id"));
        project_offline_id = Long.parseLong(getArguments().getString("project_offline_id"));
        addProjectMemberAdapter = new AddProjectMemberAdapter(Users.getUsers(), project_id, project_offline_id, ApplicationContext.get());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listContacts.setAdapter(addProjectMemberAdapter);
        listContacts.setLayoutManager(mLinearLayoutManager);
        listContacts.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @OnClick(R.id.ripple_back)
    public void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.ripple_add)
    public void addMembers(){
        if(!Projects.getProjectStatus(project_offline_id).equals(AccountGeneral.STATUS_SYNC)){
            for (Map.Entry<String, Integer> entry : addProjectMemberAdapter.membersMap.entrySet())
            {
                ProjectMembers projectMembers = new ProjectMembers();
                projectMembers.setProject_id(project_id);
                projectMembers.setProject_offline_id(project_offline_id);
                projectMembers.setUser_id(entry.getValue());
                projectMembers.setProject_member_name(Users.getUser(entry.getValue()).getFull_name());
                projectMembers.save();
                System.out.println(entry.getKey() + "/" + entry.getValue());
            }
            Toast.makeText(ApplicationContext.get(), "Members successfully added.", Toast.LENGTH_SHORT).show();
            MainActivity.popFragment();
        }
        else{
            if(isNetworkAvailable()){
                final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Adding project members...");
                mProgressDialog.show();
                ProjectJsonMembers projectJsonMembers = new ProjectJsonMembers();
                projectJsonMembers.setProject_id(project_id);
                List<String> memberIds = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : addProjectMemberAdapter.membersMap.entrySet())
                {
                    memberIds.add(""+entry.getValue());
                    System.out.println(entry.getKey() + "/" + entry.getValue());
                }
                projectJsonMembers.setProject_members(memberIds);
                Gson gson = new Gson();
                String json = gson.toJson(projectJsonMembers);
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                Observable<ResponseWrapper> observable = cloudCheetahAPIService.addProjectMember(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
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
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("AddProjectMember", e.getMessage(), e);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onNext(ResponseWrapper responseWrapper) {
                                if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                    for (Map.Entry<String, Integer> entry : addProjectMemberAdapter.membersMap.entrySet())
                                    {
                                        ProjectMembers projectMembers = new ProjectMembers();
                                        projectMembers.setProject_id(project_id);
                                        projectMembers.setProject_offline_id(project_offline_id);
                                        projectMembers.setUser_id(entry.getValue());
                                        projectMembers.setProject_member_name(Users.getUser(entry.getValue()).getFull_name());
                                        projectMembers.save();
                                        System.out.println(entry.getKey() + "/" + entry.getValue());
                                    }
                                    Toast.makeText(ApplicationContext.get(), "Members successfully added.", Toast.LENGTH_SHORT).show();
                                    MainActivity.popFragment();
                                }
                            }
                        });
            }
            else{
                for (Map.Entry<String, Integer> entry : addProjectMemberAdapter.membersMap.entrySet())
                {
                    ProjectMembers projectMembers = new ProjectMembers();
                    projectMembers.setProject_id(project_id);
                    projectMembers.setProject_offline_id(project_offline_id);
                    projectMembers.setUser_id(entry.getValue());
                    projectMembers.setProject_member_name(Users.getUser(entry.getValue()).getFull_name());
                    projectMembers.save();
                }
                Projects projects = Projects.getProjectsOfflineMode(project_offline_id);
                projects.setStatus(AccountGeneral.STATUS_UNSYNC);
                projects.save();
                Toast.makeText(ApplicationContext.get(), "You currently don't have a network connection all changes is saved in the device. Kindly sync the project manually once the network is connected.", Toast.LENGTH_SHORT).show();
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
