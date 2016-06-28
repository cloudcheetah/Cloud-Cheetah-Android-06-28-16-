package com.forateq.cloudcheetah.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.models.TaskCashInCashOut;

import java.util.List;

/**
 * This adapter is use to display all the cashin and cashout of a particular task
 * Created by Vallejos Family on 6/14/2016.
 */
public class CashInOutAdapter extends RecyclerView.Adapter<CashInOutAdapter.ViewHolder>{

    private List<TaskCashInCashOut> cashInCashOuts;
    private Context mContext;
    public static final String TAG = "MyTaskAdapter";

    /**
     * This is the class constructor used to create a new instance of this adapter
     * @param cashInCashOuts
     * @param context
     */
    public CashInOutAdapter(List<TaskCashInCashOut> cashInCashOuts, Context context) {
        this.cashInCashOuts = cashInCashOuts;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_cash_in_out, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TaskCashInCashOut taskCashInCashOut = cashInCashOuts.get(i);
        viewHolder.cashInCashOutDateTV.setText(taskCashInCashOut.getDate());
        viewHolder.typeTV.setText(taskCashInCashOut.getType());
        viewHolder.amountTV.setText(""+taskCashInCashOut.getAmount());
        viewHolder.cashInOutOfflineId.setText(""+taskCashInCashOut.getId());
        viewHolder.cashInOutId.setText(""+taskCashInCashOut.getTask_cash_in_out_id());
    }

    /**
     * This method us used to add new item in the cashin and cashout arraylist
     * @param taskCashInCashOut
     */
    public void addItem(TaskCashInCashOut taskCashInCashOut){
        cashInCashOuts.add(taskCashInCashOut);
        notifyDataSetChanged();
    }

    /**
     * This method is used to clear all the items in the cashin cashout arraylist
     */
    public void clearItems(){
        cashInCashOuts.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cashInCashOuts == null ? 0 : cashInCashOuts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView cashInCashOutDateTV;
        public TextView typeTV;
        public TextView amountTV;
        public TextView cashInOutOfflineId;
        public TextView cashInOutId;
        public MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            cashInCashOutDateTV = (TextView) itemView.findViewById(R.id.cash_in_out_date);
            typeTV = (TextView) itemView.findViewById(R.id.type);
            amountTV = (TextView) itemView.findViewById(R.id.amount);
            cashInOutOfflineId = (TextView) itemView.findViewById(R.id.cash_in_out_offline_id);
            cashInOutId = (TextView) itemView.findViewById(R.id.cash_in_out_id);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }


}
