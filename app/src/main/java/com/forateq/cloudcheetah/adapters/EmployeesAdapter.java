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
import com.forateq.cloudcheetah.fragments.UpdateHREmployeeFragment;
import com.forateq.cloudcheetah.models.Employees;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by PC1 on 8/4/2016.
 */
public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter .ViewHolder>{

    private List<Employees> employeesList;
    private Context mContext;
    public static final String TAG = "EmployeesAdapter";


    public EmployeesAdapter (List<Employees> employeesList, Context context) {
        this.employeesList = employeesList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_employees, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Employees employees = employeesList.get(i);
        viewHolder.employeeName.setText(employees.getFirst_name() + " "+ employees.getLast_name());
        viewHolder.employeeEmail.setText(employees.getEmail_address());
        viewHolder.employeeId.setText(""+employees.getEmployeeId());
        Picasso.with(ApplicationContext.get()).load("http://"+employees.getImage()).placeholder( R.drawable.progress_animation ).resize(50, 50)
                .centerCrop().into(viewHolder.employeePic);
    }

    /**
     * This method is used to add item in the arraylist of users
     * @param employees
     */
    public void addItem(Employees employees){
        employeesList.add(employees);
        notifyDataSetChanged();
    }

    /**
     * This method is used to clear all the items in the arraylist of users
     */
    public void clearItems(){
        employeesList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return employeesList == null ? 0 : employeesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView employeePic;
        public TextView employeeName;
        public TextView employeeEmail;
        public TextView employeeId;
        public MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            employeePic = (ImageView) itemView.findViewById(R.id.employee_image);
            employeeName = (TextView) itemView.findViewById(R.id.employee_name);
            employeeEmail = (TextView) itemView.findViewById(R.id.employee_email);
            employeeId = (TextView) itemView.findViewById(R.id.employee_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("employee_id", Integer.parseInt(employeeId.getText().toString()));
                    bundle.putInt("position", getAdapterPosition());
                    bundle.putInt("size", employeesList.size());
                    UpdateHREmployeeFragment updateHREmployeeFragment = new UpdateHREmployeeFragment();
                    updateHREmployeeFragment.setArguments(bundle);
                    MainActivity.replaceFragment(updateHREmployeeFragment, TAG);
                }
            });
        }

    }

}
