package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.PurchaseRequests;

import java.util.List;

/**
 * Created by Vallejos Family on 9/9/2016.
 */
public class PurchaseRequestAdapter extends RecyclerView.Adapter<PurchaseRequestAdapter.ViewHolder>{

    private List<PurchaseRequests> listPurchaseRequests;
    private Context mContext;
    public static final String TAG = "PurchaseRequestAdapter";

    /**
     * This is the constructor of the class used to create new instance of this adapter
     * @param listPurchaseRequests
     * @param context
     */
    public PurchaseRequestAdapter(List<PurchaseRequests> listPurchaseRequests, Context context) {
        this.listPurchaseRequests = listPurchaseRequests;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_purchase_request, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PurchaseRequests purchaseRequests = listPurchaseRequests.get(i);
        viewHolder.purchaseRequestCodeTV.setText("PR000"+purchaseRequests.getId());
        viewHolder.purchaseRequestIdTV.setText(""+purchaseRequests.getId());
        viewHolder.timeStampTV.setText(purchaseRequests.getDate());
    }

    /**
     * This method is used to add new item in the arraylists of projects
     * @param purchaseRequests
     */
    public void addItem(PurchaseRequests purchaseRequests){
        listPurchaseRequests.add(purchaseRequests);
        notifyDataSetChanged();
    }

    /**
     * This method is ised to clear all the items in the arraylists of projects
     */
    public void clearItems(){
        listPurchaseRequests.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listPurchaseRequests == null ? 0 : listPurchaseRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public MaterialRippleLayout rippleLayout;
        public TextView purchaseRequestCodeTV;
        public TextView timeStampTV;
        public TextView purchaseRequestIdTV;

        public ViewHolder(View itemView) {
            super(itemView);
            purchaseRequestCodeTV = (TextView) itemView.findViewById(R.id.purchase_request_code);
            timeStampTV = (TextView) itemView.findViewById(R.id.timestamp);
            purchaseRequestIdTV = (TextView) itemView.findViewById(R.id.purchase_request_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }


}
