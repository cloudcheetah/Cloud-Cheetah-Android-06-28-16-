package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Notifications;

import java.util.List;

/**
 * Created by Vallejos Family on 9/2/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private List<Notifications> listNotifications;
    private Context mContext;
    public static final String TAG = "NotificationAdapter";

    /**
     * This is the constructor of the class used to create new instance of this adapter
     * @param listNotifications
     * @param context
     */
    public NotificationAdapter(List<Notifications> listNotifications, Context context) {
        this.listNotifications = listNotifications;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_notification, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Notifications notifications = listNotifications.get(i);
        viewHolder.notificationMessageTV.setText(notifications.getNotification_message());
        viewHolder.notificationTypeIdTV.setText(""+notifications.getNotification_type());
        viewHolder.notificationPointerIdTV.setText(""+notifications.getNotification_pointer_id());
        viewHolder.notificationTimestamp.setText(notifications.getTimestamp());
        if(notifications.is_read()){
            viewHolder.rippleLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorLightPrimary));
        }
    }

    /**
     * This method is used to add new item in the arraylists of projects
     * @param notifications
     */
    public void addItem(Notifications notifications){
        listNotifications.add(notifications);
        notifyDataSetChanged();
    }

    /**
     * This method is ised to clear all the items in the arraylists of projects
     */
    public void clearItems(){
        listNotifications.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listNotifications == null ? 0 : listNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialRippleLayout rippleLayout;
        public TextView notificationMessageTV;
        public TextView notificationPointerIdTV;
        public TextView notificationTypeIdTV;
        public TextView notificationTimestamp;
        public ImageView senderPicIV;

        public ViewHolder(View itemView) {
            super(itemView);
            notificationMessageTV = (TextView) itemView.findViewById(R.id.notification_message);
            notificationPointerIdTV = (TextView) itemView.findViewById(R.id.notification_pointer_id);
            notificationTypeIdTV = (TextView) itemView.findViewById(R.id.notification_type_id);
            senderPicIV = (ImageView) itemView.findViewById(R.id.sender_profile_pic);
            notificationTimestamp = (TextView) itemView.findViewById(R.id.timestamp);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }


}
