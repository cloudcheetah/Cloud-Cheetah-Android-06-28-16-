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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.AccountsAdapter;
import com.forateq.cloudcheetah.adapters.CustomersAdapter;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PC1 on 7/26/2016.
 */
public class CustomersFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_customers)
    RecyclerView listCustomers;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String TAG = "CustomersFragment";
    public static CustomersAdapter customersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.customers_fragment, container, false);
        return v;
    }

    public void init(){
        customersAdapter = new CustomersAdapter(Customers.getCustomers(), getActivity());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listCustomers.setAdapter(customersAdapter);
        listCustomers.setLayoutManager(mLinearLayoutManager);
        listCustomers.setItemAnimator(new DefaultItemAnimator());
        setSearchCustomers();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void setSearchCustomers(){
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
                customersAdapter.clearItems();
                for(Customers customers : Customers.getSearchCustomer(searchString)){
                    customersAdapter.addItem(customers);
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
        AddCustomerFragment addCustomerFragment = new AddCustomerFragment();
        MainActivity.replaceFragment(addCustomerFragment, TAG);
    }

}
