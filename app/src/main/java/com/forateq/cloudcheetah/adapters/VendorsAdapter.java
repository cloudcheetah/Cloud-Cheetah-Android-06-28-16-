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
import com.forateq.cloudcheetah.fragments.VendorViewFragment;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.Vendors;

import java.util.List;

/**
 * Created by PC1 on 7/26/2016.
 */
public class VendorsAdapter extends RecyclerView.Adapter<VendorsAdapter.ViewHolder>{

    private List<Vendors> listVendors;
    private Context mContext;
    public static final String TAG = "AccountsAdapter";

    /**
     * This is the class contsructor user to create new instance of this adapter
     * @param listVendors
     * @param context
     */
    public VendorsAdapter(List<Vendors> listVendors, Context context) {
        this.listVendors = listVendors;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_vendor, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Vendors vendors = listVendors.get(i);
        viewHolder.vendorIdTV.setText(""+vendors.getVendorId());
        viewHolder.vendorNameTV.setText(vendors.getName());
    }

    @Override
    public int getItemCount() {
        return listVendors == null ? 0 : listVendors.size();
    }


    /**
     * This method is used to clear all the items in the arraylist of resources
     */
    public void clearItems(){
        listVendors.clear();
        notifyDataSetChanged();
    }

    /**
     * This method is used to add new item in the arraylist of project resources
     * @param vendors
     */
    public void addItem(Vendors vendors){
        listVendors.add(vendors);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        MaterialRippleLayout rippleLayout;
        TextView vendorNameTV;
        TextView vendorIdTV;

        public ViewHolder(View itemView) {
            super(itemView);
            vendorNameTV = (TextView) itemView.findViewById(R.id.vendor_name);
            vendorIdTV = (TextView) itemView.findViewById(R.id.vendor_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", getAdapterPosition());
                    bundle.putInt("size", listVendors.size());
                    bundle.putInt("vendor_id", Integer.parseInt(vendorIdTV.getText().toString()));
                    VendorViewFragment vendorViewFragment = new VendorViewFragment();
                    vendorViewFragment.setArguments(bundle);
                    MainActivity.replaceFragment(vendorViewFragment, TAG);
                }
            });
        }

    }
}
