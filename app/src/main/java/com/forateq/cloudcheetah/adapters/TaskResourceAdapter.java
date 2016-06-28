package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.TaskResources;

import java.util.List;

/**
 * This class is used to display all the resources of the selected task
 * Created by Vallejos Family on 5/31/2016.
 */
public class TaskResourceAdapter extends RecyclerView.Adapter<TaskResourceAdapter.ViewHolder>{

    private List<TaskResources> listTaskResources;
    private Context mContext;
    public static final String TAG = "ProjectResourcesAdapter";

    /**
     * This is the constructor of the class used to create new instance of this adapter
     * @param listTaskResources
     * @param context
     */
    public TaskResourceAdapter(List<TaskResources> listTaskResources, Context context) {
        this.listTaskResources = listTaskResources;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_task_resources, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TaskResources taskResources = listTaskResources.get(i);
        Resources resources = Resources.getResource(taskResources.getResource_id());
        viewHolder.resourceNameTV.setText(resources.getName());
        viewHolder.resourceQuantityTV.setText(""+taskResources.getQuantity()+" pcs.");
        viewHolder.resourcetIdTV.setText(""+taskResources.getResource_id());

    }

    @Override
    public int getItemCount() {
        return listTaskResources == null ? 0 : listTaskResources.size();
    }

    public void removeItem(int resource_id){
        for(TaskResources taskResources : listTaskResources){
            if(taskResources.getResource_id() == resource_id){
                listTaskResources.remove(taskResources);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * This method is used to clear all the items in the arraylist of task resources
     */
    public void clearItems(){
        listTaskResources.clear();
        notifyDataSetChanged();
    }

    /**
     * This method is used to add new item in the arraylist of task resources
     * @param taskResources
     */
    public void addItem(TaskResources taskResources){
        listTaskResources.add(taskResources);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView resourceNameTV;
        TextView resourceQuantityTV;
        TextView resourcetIdTV;
        MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            resourceNameTV = (TextView) itemView.findViewById(R.id.resource_name);
            resourceQuantityTV = (TextView) itemView.findViewById(R.id.resource_quantity);
            resourcetIdTV = (TextView) itemView.findViewById(R.id.resource_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }
}
