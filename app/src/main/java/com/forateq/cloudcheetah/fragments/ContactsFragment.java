package com.forateq.cloudcheetah.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.ContactAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** This fragment is used to display all the contacts of the current user of the app
 * Created by Vallejos Family on 5/11/2016.
 */
public class ContactsFragment extends Fragment {

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_contacts)
    RecyclerView listContacts;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    ContactAdapter contactAdapter;
    public static final String TAG = "ProjectsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contacts_fragment, container, false);
        return v;
    }

    public void init(){
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
        boolean isLogin = sharedPreferences.getBoolean(AccountGeneral.LOGIN_STATUS, false);
        if(isLogin){
            contactAdapter = new ContactAdapter(Users.getUsers(), getActivity());
            mLinearLayoutManager = new LinearLayoutManager(getActivity());
            listContacts.setAdapter(contactAdapter);
            listContacts.setLayoutManager(mLinearLayoutManager);
            listContacts.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public ContactsFragment() {
        super();
    }

    @OnClick(R.id.fab)
    void addNewProject(){
        Log.e(TAG, "Add new Project");
    }


}
