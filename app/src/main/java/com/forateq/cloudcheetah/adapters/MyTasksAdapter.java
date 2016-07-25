package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.TaskProgressReportsFragment;
import com.forateq.cloudcheetah.models.MyTasks;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Tasks;

import java.util.List;

/**
 * This adapter is use to display the tasks of the current user of the app
 *
 * Created by Vallejos Family on 6/10/2016.
 */
public class MyTasksAdapter extends RecyclerView.Adapter<MyTasksAdapter.ViewHolder>{

    private List<MyTasks> listTasks;
    private Context mContext;
    public static final String TAG = "MyTaskAdapter";

    /**
     * This is the constructor of the class used to create a new instance of this adapter
     * @param listTasks
     * @param context
     */
    public MyTasksAdapter(List<MyTasks> listTasks, Context context) {
        this.listTasks = listTasks;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_task, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final MyTasks myTasks = listTasks.get(i);
        viewHolder.taskName.setText(myTasks.getName());
        viewHolder.projectId.setText(""+myTasks.getProject_id());
        viewHolder.parentTaskId.setText(""+myTasks.getParent_id());
        viewHolder.taskId.setText(""+myTasks.getTaskId());
        viewHolder.taskOfflineId.setText(""+myTasks.getId());
    }

    /**
     * This method is used to add new item in the arraylist of tasks
     * @param myTasks
     */
    public void addItem(MyTasks myTasks){
        listTasks.add(myTasks);
        notifyDataSetChanged();
    }



    /**
     * This method is used to clear the items in the arraylist of tasks
     */
    public void clearItems(){
        listTasks.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listTasks == null ? 0 : listTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView taskName;
        public TextView taskId;
        public TextView projectId;
        public TextView parentTaskId;
        public TextView projectOfflineId;
        public TextView taskOfflineId;
        public MaterialRippleLayout rippleLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            taskId = (TextView) itemView.findViewById(R.id.task_id);
            projectId = (TextView) itemView.findViewById(R.id.project_id);
            parentTaskId = (TextView) itemView.findViewById(R.id.parent_task_id);
            projectOfflineId = (TextView) itemView.findViewById(R.id.project_offline_id);
            taskOfflineId = (TextView) itemView.findViewById(R.id.task_offline_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("task_offline_id", taskOfflineId.getText().toString());
                    bundle.putString("task_id", taskId.getText().toString());
                    bundle.putString("task_name", taskName.getText().toString());
                    TaskProgressReportsFragment taskProgressReportsFragment = new TaskProgressReportsFragment();
                    taskProgressReportsFragment.setArguments(bundle);
                    MainActivity.replaceFragment(taskProgressReportsFragment, TAG);
                }
            });
        }

    }


}
