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
import com.forateq.cloudcheetah.fragments.ProgressReportViewFragment;
import com.forateq.cloudcheetah.models.TaskProgressReports;

import java.util.List;

/** This adapter is used to display all the task progress report of a selected task
 * Created by Vallejos Family on 6/10/2016.
 */
public class TaskProgressAdapter extends RecyclerView.Adapter<TaskProgressAdapter.ViewHolder>{

    private List<TaskProgressReports> listTaskReports;
    private Context mContext;
    public static final String TAG = "TaskAdapter";

    /**
     * This is the constructor of the class used to create new instance of this adapter
     * @param listTaskReports
     * @param context
     */
    public TaskProgressAdapter(List<TaskProgressReports> listTaskReports, Context context) {
        this.listTaskReports = listTaskReports;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_progress_report, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final TaskProgressReports taskProgressReports = listTaskReports.get(i);
        viewHolder.taskProgressOfflineId.setText(""+taskProgressReports.getId());
        viewHolder.taskProgressId.setText(""+taskProgressReports.getTask_progress_id());
        viewHolder.progressReportDate.setText(taskProgressReports.getReport_date());
    }

    /**
     * This method is used to add new progress report in the arraylist of progress reports
     * @param taskProgressReports
     */
    public void addItem(TaskProgressReports taskProgressReports){
        listTaskReports.add(taskProgressReports);
        notifyDataSetChanged();
    }

    /**
     * This method is used to clear all the items in the arraylist of progress reports
     */
    public void clearItems(){
        listTaskReports.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listTaskReports == null ? 0 : listTaskReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView progressReportDate;
        public TextView taskProgressId;
        public TextView taskProgressOfflineId;
        public MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            progressReportDate = (TextView) itemView.findViewById(R.id.task_date);
            taskProgressId = (TextView) itemView.findViewById(R.id.task_progress_id);
            taskProgressOfflineId = (TextView) itemView.findViewById(R.id.task_progress_offline_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("task_progress_id", taskProgressId.getText().toString());
                    bundle.putString("task_progress_offline_id", taskProgressOfflineId.getText().toString());
                    ProgressReportViewFragment progressReportViewFragment = new ProgressReportViewFragment();
                    progressReportViewFragment.setArguments(bundle);
                    MainActivity.replaceFragment(progressReportViewFragment, TAG);
                }
            });
        }

    }


}
