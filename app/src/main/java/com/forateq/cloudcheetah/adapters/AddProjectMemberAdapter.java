package com.forateq.cloudcheetah.adapters;

/**
 * Created by Vallejos Family on 5/26/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.ProjectMembers;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This adapter is use to list all the possible members that can be added in a project
 *
 * Created by Vallejos Family on 5/20/2016.
 */
public class AddProjectMemberAdapter extends RecyclerView.Adapter<AddProjectMemberAdapter.ViewHolder>{

    private List<Users> usersList;
    private Context mContext;
    public static final String TAG = "ContactAdapter";
    public static Map<String, Integer> membersMap;
    private Map<String, Integer> memberIdMap;
    int project_id;
    long project_offline_id;

    /**
     * This is the class constructor use to create a new instance of this adapter
     * @param usersList
     * @param project_id
     * @param project_offline_id
     * @param context
     */
    public AddProjectMemberAdapter(List<Users> usersList, int project_id, long project_offline_id, Context context) {
        this.usersList = usersList;
        this.mContext = context;
        this.project_id = project_id;
        this.project_offline_id = project_offline_id;
        if(!Projects.getProjectStatus(project_offline_id).equals(AccountGeneral.STATUS_SYNC)){
            memberIdMap = ProjectMembers.getProjectMemberOfflineIdList(project_offline_id);
        }
        else{
            memberIdMap = ProjectMembers.getProjectMemberIdList(project_id);
        }
        membersMap = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_member, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Users users = usersList.get(i);
        viewHolder.contactName.setText(users.getFull_name());
        viewHolder.userName.setText(users.getUser_name());
        viewHolder.userId.setText(""+users.getUser_id());
        if(memberIdMap.containsKey(""+users.getUser_id())){
            viewHolder.checkBoxInClude.setVisibility(View.GONE);
        }
    }

    /**
     * This method is used to add items in the array list of users
     * @param users
     */
    public void addItem(Users users){
        usersList.add(users);
        notifyDataSetChanged();
    }

    /**
     * This method is used to clear all the contents of the arraylist of users
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
        public CheckBox checkBoxInClude;
        public MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            userId = (TextView) itemView.findViewById(R.id.user_id);
            userName = (TextView) itemView.findViewById(R.id.contact_username);
            checkBoxInClude = (CheckBox) itemView.findViewById(R.id.include_checkbox);
            checkBoxInClude.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        Log.e(contactName.getText().toString(), "Checked");
                        membersMap.put(userId.getText().toString(), Integer.parseInt(userId.getText().toString()));
                    }
                    else{
                        membersMap.remove(userId.getText().toString());
                    }
                }
            });
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }


}

