package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.ViewPagerAdapter;
import com.forateq.cloudcheetah.utils.CustomViewPager;
import com.forateq.cloudcheetah.utils.SlidingTabLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/** This fragment is the container of all the major fragments of the app
 * Created by Vallejos Family on 5/11/2016.
 */
public class MainFragment extends Fragment {

    @Bind(R.id.viewPager)
    CustomViewPager pager;
    private ViewPagerAdapter adapter;
    @Bind(R.id.tabs)
    SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Home", "Contacts", "Chat", "ERP", "Profile"};
    private int Numboftabs = 5;
    private HomeFragment homeFragment;
    private ContactsFragment contactsFragment;
    private ChatFragment chatFragment;
    private ERPFragment erpFragment;
    private ProfileFragment profileFragment;
    public static final String TAG = "MainFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    public void init(){
        homeFragment = new HomeFragment();
        contactsFragment = new ContactsFragment();
        chatFragment = new ChatFragment();
        erpFragment = new ERPFragment();
        profileFragment = new ProfileFragment();
        adapter =  new ViewPagerAdapter(getChildFragmentManager(), Titles, Numboftabs, contactsFragment, chatFragment, erpFragment, profileFragment, homeFragment);
        pager.setAdapter(adapter);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorLightPrimary);
            }
        });
        tabs.setViewPager(pager);

    }

    public MainFragment() {
        super();
    }

}
