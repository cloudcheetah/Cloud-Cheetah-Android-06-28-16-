package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.ChatFragment;
import com.forateq.cloudcheetah.fragments.ContactsFragment;
import com.forateq.cloudcheetah.fragments.ERPFragment;
import com.forateq.cloudcheetah.fragments.HomeFragment;
import com.forateq.cloudcheetah.fragments.ProfileFragment;
import com.forateq.cloudcheetah.utils.ApplicationContext;


/**
 * This adapter is used to hold all the fragments of the main activity
 * Created by Vallejos Family on 3/13/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    HomeFragment homeFragment;
    ContactsFragment contactsFragment;
    ChatFragment chatFragment;
    ERPFragment erpFragment;
    ProfileFragment profileFragment;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, ContactsFragment contactsFragment,
                            ChatFragment chatFragment, ERPFragment erpFragment, ProfileFragment profileFragment, HomeFragment homeFragment) {
        super(fm);
        this.contactsFragment = contactsFragment;
        this.chatFragment = chatFragment;
        this.homeFragment = homeFragment;
        this.chatFragment = chatFragment;
        this.erpFragment = erpFragment;
        this.profileFragment = profileFragment;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            Log.e("Contact", "Created");
            return homeFragment;
        }
        else if(position == 1) // if the position is 0 we are returning the First tab
        {
            Log.e("Chat", "Created");
            SharedPreferences sharedPreferences = ApplicationContext.get().getSharedPreferences(AccountGeneral.ACCOUNT_NAME, Context.MODE_PRIVATE);
            Log.e("Session Key", sharedPreferences.getString(AccountGeneral.SESSION_KEY, ""));
            Log.e("Notif Id", sharedPreferences.getString(AccountGeneral.NOTIFICATION_ID, ""));
            Log.e("Device Id", AccountGeneral.DEVICE_ID);
            return contactsFragment;
        }
        else if(position == 2) // if the position is 0 we are returning the First tab
        {
            Log.e("DMS", "Created");
            return chatFragment;
        }
        else if(position == 3)             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            Log.e("Setting", "Created");
            return erpFragment;
        }
        else{
            return profileFragment;
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
