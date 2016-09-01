package com.forateq.cloudcheetah.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.TaskInProgressViewPagerAdapter;
import com.forateq.cloudcheetah.models.MyTasks;
import com.forateq.cloudcheetah.utils.ParentSlidingTabLayout;

import java.util.Arrays;
import java.util.List;

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
    private CharSequence Titles[] = {"My Tasks", "My Project Tasks"};
    private int numbOfTabs = 2;
    public static final String TAG = "TaskInProgressFragment";
    private SharedPreferences preferences;
    private String[] status = new String[]{
            "Started",
            "Hold",
            "Resume",
            "Cancel",
            "Complete"
    };
    private boolean[] checkedStatus;
    private List<String> statusList;
    private String started = "";
    private String hold = "";
    private String resume = "";
    private String cancel = "";
    private String complete = "";

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
        preferences = getActivity().getSharedPreferences("Filter", Context.MODE_PRIVATE);
        checkedStatus = new boolean[]{
                preferences.getBoolean(status[0], false),
                preferences.getBoolean(status[1], false),
                preferences.getBoolean(status[2], false),
                preferences.getBoolean(status[3], false),
                preferences.getBoolean(status[4], false)

        };
        statusList = Arrays.asList(status);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter by status: ");
        builder.setMultiChoiceItems(status, checkedStatus, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedStatus[which] = isChecked;
                String currentItem = statusList.get(which);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(currentItem,isChecked);
                editor.commit();
            }
        });
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(checkedStatus[0] || checkedStatus[1] || checkedStatus[2] || checkedStatus[3] || checkedStatus[4]){
                            if(checkedStatus[0]){
                                started = ""+1;
                            }
                            else{
                                started = "";
                            }
                            if(checkedStatus[1]){
                                hold = ""+2;
                            }
                            else{
                                hold = "";
                            }
                            if(checkedStatus[2]){
                                resume = ""+3;
                            }
                            else{
                                resume = "";
                            }
                            if(checkedStatus[3]){
                                cancel = ""+4;
                            }
                            else{
                                cancel = "";
                            }
                            if(checkedStatus[4]){
                                complete = ""+5;
                            }
                            else{
                                complete = "";
                            }
                            MyTasksFragment.myTasksAdapter.clearItems();
                            for(MyTasks myTasks : MyTasks.getFilterTasks(started, hold, resume, cancel, complete)){
                                MyTasksFragment.myTasksAdapter.addItem(myTasks);
                            }
                        }
                        else{
                            MyTasksFragment.myTasksAdapter.clearItems();
                            for(MyTasks myTasks : MyTasks.getMyTasks()){
                                MyTasksFragment.myTasksAdapter.addItem(myTasks);
                            }
                        }
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


}
