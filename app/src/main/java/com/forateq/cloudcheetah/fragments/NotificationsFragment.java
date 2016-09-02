package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.NotificationAdapter;
import com.forateq.cloudcheetah.models.Notifications;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by PC1 on 8/18/2016.
 */

public class NotificationsFragment extends Fragment {

    @Bind(R.id.list_notifications)
    RecyclerView listNotifications;
    private LinearLayoutManager mLinearLayoutManager;
    private NotificationAdapter adapter;
    public static final String TAG = "NotificationsFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notifications_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    public void init(){
        adapter = new NotificationAdapter(Notifications.getAllNotifications(), getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        listNotifications.setAdapter(adapter);
        listNotifications.setLayoutManager(mLinearLayoutManager);
        listNotifications.setItemAnimator(new DefaultItemAnimator());
    }

    public NotificationsFragment() {
        super();
    }

}

