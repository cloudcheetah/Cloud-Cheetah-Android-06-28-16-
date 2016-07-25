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
import com.forateq.cloudcheetah.adapters.InventoryItemsAdapter;
import com.forateq.cloudcheetah.adapters.ProjectAdapter;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PC1 on 7/12/2016.
 */
public class InventoryFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_inventory_items)
    RecyclerView listInventoryItems;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    public static InventoryItemsAdapter inventoryItemsAdapter;
    public static final String TAG = "InventoryFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.inventory_fragment, container, false);
        return v;
    }

    public void init(){
        inventoryItemsAdapter = new InventoryItemsAdapter(Resources.getAllResources(), ApplicationContext.get());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listInventoryItems.setAdapter(inventoryItemsAdapter);
        listInventoryItems.setLayoutManager(mLinearLayoutManager);
        listInventoryItems.setItemAnimator(new DefaultItemAnimator());
        setSearchForProjects();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void setSearchForProjects(){
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
                inventoryItemsAdapter.clearItems();
                for(Resources resources : Resources.getSearchResources(searchString)){
                    inventoryItemsAdapter.addItem(resources);
                }

            }
        });
    }

    @OnClick(R.id.fab)
    void addNewResource(){
        Log.e(TAG, "Add new Resource");
        AddInventoryItemFragment addInventoryItemFragment = new AddInventoryItemFragment();
        MainActivity.replaceFragment(addInventoryItemFragment, "InventoryFragment");
    }

    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }

}
