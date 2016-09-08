package com.forateq.cloudcheetah.adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.forateq.cloudcheetah.fragments.InvoicesFragment;
import com.forateq.cloudcheetah.fragments.PurchaseOrdersFragment;
import com.forateq.cloudcheetah.fragments.PurchaseReceivablesFragment;
import com.forateq.cloudcheetah.fragments.PurchaseRequestsFragment;
import com.forateq.cloudcheetah.fragments.PurchaseReturnsFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class VendorsAndProcurementViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private Context context;
    public static final String TAG = "TaskInProgressAdapter";


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public VendorsAndProcurementViewPagerAdapter(Context context, FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.context = context;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0){

            PurchaseRequestsFragment purchaseRequestsFragment = new PurchaseRequestsFragment();
            return purchaseRequestsFragment;

        }
        else if(position == 1){

            PurchaseOrdersFragment purchaseOrdersFragment = new PurchaseOrdersFragment();
            return  purchaseOrdersFragment;

        }
        else if(position == 2){

            PurchaseReceivablesFragment purchaseReceivablesFragment = new PurchaseReceivablesFragment();
            return purchaseReceivablesFragment;

        }
        else if(position == 3){

            InvoicesFragment invoicesFragment = new InvoicesFragment();
            return invoicesFragment;

        }
        else{

            PurchaseReturnsFragment purchaseReturnsFragment = new PurchaseReturnsFragment();
            return purchaseReturnsFragment;

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
