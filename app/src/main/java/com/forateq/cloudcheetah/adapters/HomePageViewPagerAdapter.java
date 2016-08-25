package com.forateq.cloudcheetah.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.forateq.cloudcheetah.fragments.CalendarFragment;
import com.forateq.cloudcheetah.fragments.NotificationsFragment;

/**
 * Created by PC1 on 8/18/2016.
 */
public class HomePageViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    NotificationsFragment notificationsFragment;
    CalendarFragment calendarFragment;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public HomePageViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, NotificationsFragment notificationsFragment, CalendarFragment calendarFragment) {
        super(fm);
        this.calendarFragment = calendarFragment;
        this.notificationsFragment = notificationsFragment;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            Log.e("Contact", "Created");
            return notificationsFragment;
        }
        else{
            return calendarFragment;
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
