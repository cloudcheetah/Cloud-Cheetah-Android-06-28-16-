package com.forateq.cloudcheetah.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Employees;
import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PC1 on 8/10/2016.
 */
public class ProjectMessagingAdapter extends BaseAdapter {

    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;

    private List<Messages> listMessage;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private int res;

    public ProjectMessagingAdapter(Activity activity, List<Messages> listMessage) {
        this.activity = activity;
        layoutInflater = activity.getLayoutInflater();
        this.listMessage = listMessage;
    }

    public void addMessage(Messages messages) {

        listMessage.add(messages);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listMessage.size();
    }

    @Override
    public Object getItem(int i) {
        return listMessage.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int i) {
        return listMessage.get(i).getDirection();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Messages messages = listMessage.get(i);
        int direction = getItemViewType(i);
        Users users = Users.getUser(messages.getSender_id());
        String sender = users.getFull_name();
        String timeStamp = messages.getCreated_at();
        String message = messages.getMessage();
        if (convertView == null) {
            Log.e("Direction Inside", "" + direction);

            if (direction == DIRECTION_INCOMING) {
                res = R.layout.cardview_incoming_message;
            } else if (direction == DIRECTION_OUTGOING) {
                res = R.layout.cardview_outgoing_message;
            }
            convertView = layoutInflater.inflate(res, viewGroup, false);
        }
        ImageView userPhotoIV = (ImageView) convertView.findViewById(R.id.profile_pic);
        Employees employees = Employees.getEmployee(users.getEmployee_id());
        Picasso.with(ApplicationContext.get()).load("http://"+employees.getImage()).placeholder( R.drawable.progress_animation ).resize(50, 50)
                .centerCrop().into(userPhotoIV);
        TextView senderTV = (TextView) convertView.findViewById(R.id.sender);
        TextView messageTV = (TextView) convertView.findViewById(R.id.message);
        TextView timestampTV = (TextView) convertView.findViewById(R.id.timestamp);
        TextView messageIdTV = (TextView) convertView.findViewById(R.id.chat_reply_id);
        senderTV.setText(sender);
        messageTV.setText(message);
        timestampTV.setText(timeStamp);
        messageIdTV.setText(""+messages.getMessageId());
        return convertView;
    }
}
