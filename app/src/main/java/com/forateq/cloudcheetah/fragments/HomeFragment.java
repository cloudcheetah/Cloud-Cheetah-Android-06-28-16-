package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.HomePageViewPagerAdapter;
import com.forateq.cloudcheetah.adapters.ViewPagerAdapter;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.utils.CustomViewPager;
import com.forateq.cloudcheetah.utils.HomePageSlidingTabLayout;
import com.forateq.cloudcheetah.utils.SlidingTabLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/** This fragment is used to display the home view of the app
 * Created by Vallejos Family on 5/11/2016.
 */
public class HomeFragment extends Fragment {

    @Bind(R.id.viewPager)
    CustomViewPager pager;
    private HomePageViewPagerAdapter adapter;
    @Bind(R.id.tabs)
    HomePageSlidingTabLayout tabs;
    @Bind(R.id.linlaHeaderProgress)
    LinearLayout progressBarLayout;
    private CharSequence Titles[] = {"Notifications", "Calendar"};
    private int Numboftabs = 2;
    public static final String TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
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
        tabs.setCustomTabColorizer(new HomePageSlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorLightPrimary);
            }
        });
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:{
                        Log.e("Notifications ", "Fragment");
                        break;
                    }
                    case 1:{
                        Log.e("Calendar ", "Fragment");
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter =  new HomePageViewPagerAdapter(getChildFragmentManager(), Titles, Numboftabs, CloudCheetahApp.notificationsFragment, CloudCheetahApp.calendarFragment);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
    }

    public HomeFragment() {
        super();
    }

}
