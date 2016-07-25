package com.forateq.cloudcheetah.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.ProjectMembers;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.ProjectJsonMembers;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** This adapter is used to display all the members of a project
 * Created by Vallejos Family on 5/25/2016.
 */
public class ProjectMembersAdapter extends RecyclerView.Adapter<ProjectMembersAdapter.ViewHolder>{

    private List<ProjectMembers> listProjectMembers;
    private Context mContext;
    public static final String TAG = "ProjectMembersAdapter";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;

    /**
     * This is the constructor of the class used to create new instance of this class
     * @param listProjectMembers
     * @param context
     */
    public ProjectMembersAdapter(List<ProjectMembers> listProjectMembers, Context context) {
        this.listProjectMembers = listProjectMembers;
        this.mContext = context;
        ((CloudCheetahApp) ApplicationContext.get()).getNetworkComponent().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_contact, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final ProjectMembers projectMembers = listProjectMembers.get(i);
        Users users = Users.getUser(projectMembers.getUser_id());
        viewHolder.contactNameTV.setText(users.getFull_name());
        viewHolder.contactUsernameTV.setText(users.getUser_name());
        viewHolder.contactUserIdTV.setText(""+projectMembers.getUser_id());
        viewHolder.projectIdTV.setText(""+projectMembers.getProject_id());
        viewHolder.projectOfflineIdTV.setText(""+projectMembers.getProject_offline_id());
        viewHolder.projectMemberIdTV.setText(""+projectMembers.getId());

    }

    @Override
    public int getItemCount() {
        return listProjectMembers == null ? 0 : listProjectMembers.size();
    }

    public void removeItem(int user_id){
        for(ProjectMembers projectMembers : listProjectMembers){
            if(projectMembers.getUser_id() == user_id){
                listProjectMembers.remove(projectMembers);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * This methid is used to clear all the items in the arraylist of project members
     */
    public void clearItems(){
        listProjectMembers.clear();
        notifyDataSetChanged();
    }

    /**
     * This method is used to add a projectmember in the arraylist of projectmembers
     * @param projectMembers
     */
    public void addItem(ProjectMembers projectMembers){
        listProjectMembers.add(projectMembers);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView contactNameTV;
        TextView contactUsernameTV;
        TextView contactUserIdTV;
        TextView projectIdTV;
        TextView projectOfflineIdTV;
        TextView projectMemberIdTV;
        ImageView removeMemberIV;
        MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            contactNameTV = (TextView) itemView.findViewById(R.id.contact_name);
            contactUsernameTV = (TextView) itemView.findViewById(R.id.contact_username);
            contactUserIdTV = (TextView) itemView.findViewById(R.id.user_id);
            projectIdTV = (TextView) itemView.findViewById(R.id.project_id);
            projectOfflineIdTV = (TextView) itemView.findViewById(R.id.project_offline_id);
            projectMemberIdTV = (TextView) itemView.findViewById(R.id.project_member_id);
            removeMemberIV = (ImageView) itemView.findViewById(R.id.remove_member);
            removeMemberIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Projects.getProjectStatus(Long.parseLong(projectOfflineIdTV.getText().toString())).equals(AccountGeneral.STATUS_SYNC)){
                        int user_id = Integer.parseInt(contactUserIdTV.getText().toString());
                        int project_id = Integer.parseInt(projectIdTV.getText().toString());
                        new Delete().from(ProjectMembers.class).where("project_id = ? AND user_id = ?", project_id, user_id).execute();
                        listProjectMembers.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),listProjectMembers.size());
                    }
                    else{
                        if(isNetworkAvailable()){
                            int user_id = Integer.parseInt(contactUserIdTV.getText().toString());
                            int project_id = Integer.parseInt(projectIdTV.getText().toString());
                            new Delete().from(ProjectMembers.class).where("project_id = ? AND user_id = ?", project_id, user_id).execute();
                            listProjectMembers.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(),listProjectMembers.size());
                            List<ProjectMembers> projectMembersList = ProjectMembers.getProjectMembers(Integer.parseInt(projectIdTV.getText().toString()));
                            ProjectJsonMembers projectJsonMembers = new ProjectJsonMembers();
                            projectJsonMembers.setProject_id(Integer.parseInt(projectIdTV.getText().toString()));
                            List<String> memberIds = new ArrayList<>();
                            for(ProjectMembers projectMembers : projectMembersList){
                                memberIds.add(""+projectMembers.getUser_id());
                            }
                            projectJsonMembers.setProject_members(memberIds);
                            Gson gson = new Gson();
                            String json = gson.toJson(projectJsonMembers);
                            Log.e("Json", json);
                            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                            Observable<ResponseWrapper> observable = cloudCheetahAPIService.addProjectMember(userName, Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                    Settings.Secure.ANDROID_ID), sessionKey, json);
                            observable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .unsubscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<ResponseWrapper>() {
                                        @Override
                                        public void onCompleted() {
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e("AddProjectMember", e.getMessage(), e);
                                        }

                                        @Override
                                        public void onNext(ResponseWrapper responseWrapper) {
                                            if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                                Toast.makeText(ApplicationContext.get(), "Project member successfully deleted.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else{
                            int user_id = Integer.parseInt(contactUserIdTV.getText().toString());
                            int project_id = Integer.parseInt(projectIdTV.getText().toString());
                            new Delete().from(ProjectMembers.class).where("project_id = ? AND user_id = ?", project_id, user_id).execute();
                            listProjectMembers.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(),listProjectMembers.size());
                            Projects projects = Projects.getProjectsOfflineMode(Long.parseLong(projectOfflineIdTV.getText().toString()));
                            projects.setStatus(AccountGeneral.STATUS_UNSYNC);
                            projects.save();
                            Toast.makeText(ApplicationContext.get(), "You currently don't have a network connection all changes is saved in the device. Kindly sync the project manually once the network is connected.", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                    }
                    Log.e("Clicked", "Clicked");
                }
            });
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Clicked", "Clicked");
                }
            });
        }

    }

    /**
     * Checks if there is a network available before login
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ApplicationContext.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
