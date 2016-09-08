package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.NotificationAdapter;
import com.forateq.cloudcheetah.models.Notifications;

import java.util.List;

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
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 10;
    int firstVisibleItem, visibleItemCount, totalItemCount;


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
        adapter = new NotificationAdapter(Notifications.getFirstTenNotifications(visibleThreshold), getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        listNotifications.setAdapter(adapter);
        listNotifications.setLayoutManager(mLinearLayoutManager);
        listNotifications.setItemAnimator(new DefaultItemAnimator());
        listNotifications.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = listNotifications.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                        List<Notifications> list = Notifications.getOtherNotifications(visibleThreshold, previousTotal);
                        for(Notifications notifications : list){
                            adapter.addItem(notifications);
                        }
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    Log.e("Yaeye!", "end called");
                    loading = true;
                }
            }
        });
    }

    public NotificationsFragment() {
        super();
    }

}

