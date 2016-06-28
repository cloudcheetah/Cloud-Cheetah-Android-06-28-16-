package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.AddProjectMemberAdapter;
import com.forateq.cloudcheetah.models.ProjectMembers;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_project_member_fragment, container, false);
        ButterKnife.bind(this, v);
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
        for (Map.Entry<String, Integer> entry : addProjectMemberAdapter.membersMap.entrySet())
        {
            ProjectMembers projectMembers = new ProjectMembers();
            projectMembers.setProject_id(project_id);
            projectMembers.setProject_offline_id(project_offline_id);
            projectMembers.setUser_id(entry.getValue());
            projectMembers.save();
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
        Toast.makeText(ApplicationContext.get(), "Members successfully added.", Toast.LENGTH_SHORT).show();
        MainActivity.popFragment();
    }

}
