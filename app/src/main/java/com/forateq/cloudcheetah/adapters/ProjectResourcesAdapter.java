package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.fragments.EditResourceFragment;
import com.forateq.cloudcheetah.models.ProjectResources;
import com.forateq.cloudcheetah.models.Resources;

import java.util.List;

/**
 * This adapter is use to display all the resources of a specific project
 * Created by Vallejos Family on 5/25/2016.
 */
public class ProjectResourcesAdapter extends RecyclerView.Adapter<ProjectResourcesAdapter.ViewHolder>{

    private List<ProjectResources> listProjectResources;
    private Context mContext;
    public static final String TAG = "ProjectResourcesAdapter";

    /**
     * This is the class contsructor user to create new instance of this adapter
     * @param listProjectResources
     * @param context
     */
    public ProjectResourcesAdapter(List<ProjectResources> listProjectResources, Context context) {
        this.listProjectResources = listProjectResources;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_resources, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final ProjectResources projectResources = listProjectResources.get(i);
        Resources resources = Resources.getResource(projectResources.getResource_id());
        viewHolder.resourceNameTV.setText(resources.getName());
        viewHolder.resourceQuantityTV.setText(""+projectResources.getQuantity()+" pcs.");
        viewHolder.resourcetIdTV.setText(""+projectResources.getResource_id());
        viewHolder.projectResourceIdTV.setText(""+projectResources.getProject_resource_id());
        viewHolder.projectOfflineIdTV.setText(""+projectResources.getProject_offline_id());
        viewHolder.projectIdTV.setText(""+projectResources.getProject_id());

    }

    @Override
    public int getItemCount() {
        return listProjectResources == null ? 0 : listProjectResources.size();
    }

    /**
     * This method is used to remove a specific item in the arraylist of project resources
     * @param resource_id
     */
    public void removeItem(int resource_id){
        for(ProjectResources projectResources : listProjectResources){
            if(projectResources.getResource_id() == resource_id){
                listProjectResources.remove(projectResources);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * This method is used to clear all the items in the arraylist of project resources
     */
    public void clearItems(){
        listProjectResources.clear();
        notifyDataSetChanged();
    }

    /**
     * This method is used to add new item in the arraylist of project resources
     * @param projectResources
     */
    public void addItem(ProjectResources projectResources){
        listProjectResources.add(projectResources);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView resourceNameTV;
        TextView resourceQuantityTV;
        TextView resourcetIdTV;
        TextView projectResourceIdTV;
        TextView projectIdTV;
        TextView projectOfflineIdTV;
        ImageView editResourceIV;
        ImageView deleteResourceIV;
        MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            resourceNameTV = (TextView) itemView.findViewById(R.id.resource_name);
            resourceQuantityTV = (TextView) itemView.findViewById(R.id.resource_quantity);
            resourcetIdTV = (TextView) itemView.findViewById(R.id.resource_id);
            projectResourceIdTV = (TextView) itemView.findViewById(R.id.project_resource_id);
            projectIdTV = (TextView) itemView.findViewById(R.id.project_id);
            projectOfflineIdTV = (TextView) itemView.findViewById(R.id.project_offline_id);
            editResourceIV = (ImageView) itemView.findViewById(R.id.edit_resource);
            editResourceIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("project_id", projectIdTV.getText().toString());
                    bundle.putString("project_offline_id", projectOfflineIdTV.getText().toString());
                    bundle.putString("project_resource_id", projectResourceIdTV.getText().toString());
                    bundle.putString("resource_id", resourcetIdTV.getText().toString());
                    EditResourceFragment editResourceFragment = new EditResourceFragment();
                    editResourceFragment.setArguments(bundle);
                    MainActivity.replaceFragment(editResourceFragment, TAG);
                }
            });
            deleteResourceIV = (ImageView) itemView.findViewById(R.id.delete_resource);
            deleteResourceIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
}
