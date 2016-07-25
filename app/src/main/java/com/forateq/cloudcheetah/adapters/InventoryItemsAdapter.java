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
import com.forateq.cloudcheetah.fragments.UpdateDeleteInventoryItemFragment;
import com.forateq.cloudcheetah.models.ProjectResources;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * This adapter is use to display all the resources of a specific project
 * Created by Vallejos Family on 5/25/2016.
 */
public class InventoryItemsAdapter extends RecyclerView.Adapter<InventoryItemsAdapter.ViewHolder>{

    private List<Resources> listResources;
    private Context mContext;
    public static final String TAG = "ProjectResourcesAdapter";

    /**
     * This is the class contsructor user to create new instance of this adapter
     * @param listResources
     * @param context
     */
    public InventoryItemsAdapter(List<Resources> listResources, Context context) {
        this.listResources = listResources;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_inventory_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Resources resources = listResources.get(i);
        viewHolder.resourceIdTV.setText(""+resources.getResource_id());
        viewHolder.resourceNameTV.setText(resources.getName());
        viewHolder.resourceOnHandQtyTV.setText("On Hand: "+resources.getOn_hand_qty());
        viewHolder.resourceReservedQtyTV.setText("Reserved: "+resources.getReserved_qty());
        viewHolder.resourceInTransitQtyTV.setText("In Transit: "+resources.getIn_transit_qty());
        Picasso.with(ApplicationContext.get()).load("http://"+resources.getImage()).placeholder( R.drawable.progress_animation ).resize(50, 50)
                .centerCrop().into(viewHolder.resourceIV);
    }

    @Override
    public int getItemCount() {
        return listResources == null ? 0 : listResources.size();
    }


    /**
     * This method is used to clear all the items in the arraylist of resources
     */
    public void clearItems(){
        listResources.clear();
        notifyDataSetChanged();
    }

    /**
     * This method is used to add new item in the arraylist of project resources
     * @param resources
     */
    public void addItem(Resources resources){
        listResources.add(resources);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView resourceNameTV;
        TextView resourceOnHandQtyTV;
        TextView resourceReservedQtyTV;
        TextView resourceInTransitQtyTV;
        TextView resourceIdTV;
        ImageView resourceIV;
        MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            resourceNameTV = (TextView) itemView.findViewById(R.id.resource_name);
            resourceOnHandQtyTV = (TextView) itemView.findViewById(R.id.resource_on_hand_quantity);
            resourceReservedQtyTV = (TextView) itemView.findViewById(R.id.resource_reserved_quantity);
            resourceInTransitQtyTV = (TextView) itemView.findViewById(R.id.resource_in_transit_quantity);
            resourceIdTV = (TextView) itemView.findViewById(R.id.resource_id);
            resourceIV = (ImageView) itemView.findViewById(R.id.resource_image);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("resource_id", resourceIdTV.getText().toString());
                    bundle.putString("position", ""+getAdapterPosition());
                    bundle.putString("size", ""+listResources.size());
                    UpdateDeleteInventoryItemFragment updateDeleteInventoryItemFragment = new UpdateDeleteInventoryItemFragment();
                    updateDeleteInventoryItemFragment.setArguments(bundle);
                    MainActivity.replaceFragment(updateDeleteInventoryItemFragment, "InventoryAdapter");
                }
            });
        }

    }
}

