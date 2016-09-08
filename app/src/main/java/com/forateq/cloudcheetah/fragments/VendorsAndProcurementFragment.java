package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.VendorsAndProcurementViewPagerAdapter;
import com.forateq.cloudcheetah.utils.ParentSlidingTabLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vallejos Family on 9/8/2016.
 */
public class VendorsAndProcurementFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tabs)
    ParentSlidingTabLayout tabs;
    private CharSequence Titles[] = {"Requests", "Orders", "Receivables", "Invoices", "Returns"};
    private int numbOfTabs = 5;
    public static final String TAG = "VendorsAndProcurementFragment";
    private VendorsAndProcurementViewPagerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vendors_and_procurement_fragment, container, false);
        return v;
    }

    public void init(){
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new ParentSlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorLightPrimary);
            }
        });
        adapter =  new VendorsAndProcurementViewPagerAdapter(getActivity(), getChildFragmentManager(), Titles, numbOfTabs);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }

}
