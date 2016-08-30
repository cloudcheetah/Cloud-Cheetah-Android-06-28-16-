package com.forateq.cloudcheetah.adapters;

/**
 * Created by Vallejos Family on 8/30/2016.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.forateq.cloudcheetah.fragments.MyHandledTaskFragment;
import com.forateq.cloudcheetah.fragments.MyTasksFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class TaskInProgressViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private Context context;
    public static final String TAG = "TaskInProgressAdapter";


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public TaskInProgressViewPagerAdapter(Context context, FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.context = context;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) // if the position is 0 we are returning the First tab
        {
                MyTasksFragment myTasksFragment = new MyTasksFragment();
                return myTasksFragment;

        } else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            MyHandledTaskFragment myHandledTaskFragment = new MyHandledTaskFragment();
            return myHandledTaskFragment;
        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
