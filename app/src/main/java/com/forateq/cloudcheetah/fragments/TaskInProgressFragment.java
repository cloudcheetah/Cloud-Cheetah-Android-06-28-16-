package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.TaskInProgressViewPagerAdapter;
import com.forateq.cloudcheetah.utils.ParentSlidingTabLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vallejos Family on 8/30/2016.
 */
public class TaskInProgressFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.ripple_filter)
    MaterialRippleLayout filterRipple;
    @Bind(R.id.filter)
    ImageView filterImageView;
    @Bind(R.id.pager)
    ViewPager pager;
    private TaskInProgressViewPagerAdapter adapter;
    @Bind(R.id.tabs)
    ParentSlidingTabLayout tabs;
    private CharSequence Titles[] = {"My Tasks", "My Handled Tasks"};
    private int numbOfTabs = 2;
    public static final String TAG = "TaskInProgressFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_in_progress_fragment,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
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
        adapter =  new TaskInProgressViewPagerAdapter(getActivity(), getChildFragmentManager(), Titles, numbOfTabs);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
    }

    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.ripple_filter)
    void filter(){

    }

}
