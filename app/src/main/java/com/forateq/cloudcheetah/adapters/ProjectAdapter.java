package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.fragments.ProjectsComponentsContainerFragment;
import com.forateq.cloudcheetah.models.Projects;

import java.util.List;

/**
 * This adapter is used to display all the projects of the current user of the app
 *
 * Created by Vallejos Family on 5/16/2016.
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder>{

    private List<Projects> listProjects;
    private Context mContext;
    public static final String TAG = "ProjectAdapter";

    /**
     * This is the constructor of the class used to create new instance of this adapter
     * @param listProjects
     * @param context
     */
    public ProjectAdapter(List<Projects> listProjects, Context context) {
        this.listProjects = listProjects;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_project, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Projects projects = listProjects.get(i);
        viewHolder.projectName.setText(projects.getName());
        viewHolder.projectStatus.setText(projects.getStatus());
        viewHolder.projectId.setText(""+projects.getProject_id());
        viewHolder.projectOfflineId.setText(""+projects.getId());
    }

    /**
     * This method is used to add new item in the arraylists of projects
     * @param projects
     */
    public void addItem(Projects projects){
        listProjects.add(projects);
        notifyDataSetChanged();
    }

    /**
     * This method is used to remove a specific item in the arraylists of projects
     * @param project_id
     */
    public void removeItem(int project_id){
        for(Projects projects:listProjects){
            if(projects.getProject_id() == project_id){
                listProjects.remove(projects);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * This method is ised to clear all the items in the arraylists of projects
     */
    public void clearItems(){
        listProjects.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listProjects == null ? 0 : listProjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView projectName;
        public TextView projectStatus;
        public TextView projectId;
        public TextView projectOfflineId;
        public MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            projectName = (TextView) itemView.findViewById(R.id.project_name);
            projectStatus = (TextView) itemView.findViewById(R.id.status);
            projectId = (TextView) itemView.findViewById(R.id.project_id);
            projectOfflineId = (TextView) itemView.findViewById(R.id.project_offline_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("project_id", projectId.getText().toString());
                    bundle.putString("project_offline_id", projectOfflineId.getText().toString());
                    ProjectsComponentsContainerFragment projectsComponentsContainerFragment = new ProjectsComponentsContainerFragment();
                    projectsComponentsContainerFragment.setArguments(bundle);
                    MainActivity.replaceFragment(projectsComponentsContainerFragment, TAG);
                }
            });
        }

    }


}
