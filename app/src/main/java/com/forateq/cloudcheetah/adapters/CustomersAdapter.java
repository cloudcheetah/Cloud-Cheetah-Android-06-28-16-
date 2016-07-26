package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.fragments.AccountViewFragment;
import com.forateq.cloudcheetah.fragments.AccountsFragment;
import com.forateq.cloudcheetah.fragments.CustomerViewFragment;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Customers;

import java.util.List;

/**
 * Created by PC1 on 7/26/2016.
 */
public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder>{

    private List<Customers> listCustomers;
    private Context mContext;
    public static final String TAG = "CustomersAdapter";

    /**
     * This is the class contsructor user to create new instance of this adapter
     * @param listCustomers
     * @param context
     */
    public CustomersAdapter(List<Customers> listCustomers, Context context) {
        this.listCustomers = listCustomers;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_customer, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Customers customers = listCustomers.get(i);
        viewHolder.customerNameTV.setText(customers.getName());
        viewHolder.customerIdTV.setText(""+customers.getCustomerId());
    }

    @Override
    public int getItemCount() {
        return listCustomers == null ? 0 : listCustomers.size();
    }


    /**
     * This method is used to clear all the items in the arraylist of resources
     */
    public void clearItems(){
        listCustomers.clear();
        notifyDataSetChanged();
    }

    /**
     * This method is used to add new item in the arraylist of project resources
     * @param customers
     */
    public void addItem(Customers customers){
        listCustomers.add(customers);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        MaterialRippleLayout rippleLayout;
        TextView customerNameTV;
        TextView customerIdTV;

        public ViewHolder(View itemView) {
            super(itemView);
            customerNameTV = (TextView) itemView.findViewById(R.id.customer_name);
            customerIdTV = (TextView) itemView.findViewById(R.id.customer_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", getAdapterPosition());
                    bundle.putInt("size", listCustomers.size());
                    bundle.putInt("customer_id", Integer.parseInt(customerIdTV.getText().toString()));
                    CustomerViewFragment customerViewFragment = new CustomerViewFragment();
                    customerViewFragment.setArguments(bundle);
                    MainActivity.replaceFragment(customerViewFragment, TAG);
                }
            });
        }

    }
}
