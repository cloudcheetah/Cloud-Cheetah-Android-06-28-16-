package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Users;

import java.util.List;

/**
 * This adapter is used to list all the contacts of the current user of the app
 *
 * Created by Vallejos Family on 5/20/2016.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    private List<Users> usersList;
    private Context mContext;
    public static final String TAG = "ContactAdapter";
    public ContactAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_contact, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Users users = usersList.get(i);
        viewHolder.contactName.setText(users.getFull_name());
        viewHolder.userName.setText(users.getUser_name());
        viewHolder.userId.setText(""+users.getUser_id());

    }

    /**
     * This method is used to add item in the arraylist of users
     * @param users
     */
    public void addItem(Users users){
        usersList.add(users);
        notifyDataSetChanged();
    }

    /**
     * This method is used to clear all the items in the arraylist of users
     */
    public void clearItems(){
        usersList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return usersList == null ? 0 : usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView contactName;
        public TextView userId;
        public TextView userName;
        public MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            userId = (TextView) itemView.findViewById(R.id.user_id);
            userName = (TextView) itemView.findViewById(R.id.contact_username);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }


}
