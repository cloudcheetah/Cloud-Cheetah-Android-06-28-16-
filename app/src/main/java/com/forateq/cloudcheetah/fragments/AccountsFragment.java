package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.AccountsAdapter;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PC1 on 7/25/2016.
 */
public class AccountsFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_accounts)
    RecyclerView listViewAccounts;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    public static AccountsAdapter accountsAdapter;
    int parent_id;
    public static final String TAG = "AccountsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.accounts_fragment, container, false);
        parent_id = getArguments().getInt("parent_id");
        return v;
    }

    public void init(){
        accountsAdapter = new AccountsAdapter(Accounts.getChildAccounts(parent_id), getActivity());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listViewAccounts.setAdapter(accountsAdapter);
        listViewAccounts.setLayoutManager(mLinearLayoutManager);
        listViewAccounts.setItemAnimator(new DefaultItemAnimator());
        setSearchAccounts();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void setSearchAccounts(){
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchString = searchEditText.getText().toString();
                accountsAdapter.clearItems();
                for(Accounts accounts : Accounts.searchAccounts(searchString, parent_id)){
                    accountsAdapter.addItem(accounts);
                }
            }
        });
    }

    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.fab)
    void addNewAccount(){
        Bundle bundle = new Bundle();
        bundle.putInt("parent_id", parent_id);
        AddAccountFragment addAccountFragment = new AddAccountFragment();
        addAccountFragment.setArguments(bundle);
        MainActivity.replaceFragment(addAccountFragment, TAG);
    }

}
