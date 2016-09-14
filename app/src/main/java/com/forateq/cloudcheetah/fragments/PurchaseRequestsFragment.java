package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.PurchaseRequestAdapter;
import com.forateq.cloudcheetah.models.PurchaseRequests;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vallejos Family on 9/8/2016.
 */
public class PurchaseRequestsFragment extends Fragment{

    @Bind(R.id.search)
    EditText searchEditText;
    @Bind(R.id.list_purchase_requests)
    RecyclerView listPurchaseRequest;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    private LinearLayoutManager mLinearLayoutManager;
    public static final String TAG = "PurchaseRequestsFragment";
    public static PurchaseRequestAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.purchase_requests_fragment, container, false);
        return v;
    }

    public void init(){
        adapter = new PurchaseRequestAdapter(PurchaseRequests.getPurchaseRequests(), getActivity());
        mLinearLayoutManager = new LinearLayoutManager(ApplicationContext.get());
        listPurchaseRequest.setAdapter(adapter);
        listPurchaseRequest.setLayoutManager(mLinearLayoutManager);
        listPurchaseRequest.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    @OnClick(R.id.fab)
    void addPurchaseRequest(){
        AddPurchaseRequestFragment addPurchaseRequestFragment = new AddPurchaseRequestFragment();
        MainActivity.replaceFragment(addPurchaseRequestFragment, TAG);
    }
}
