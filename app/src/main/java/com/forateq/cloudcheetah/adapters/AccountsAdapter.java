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
import com.forateq.cloudcheetah.models.Accounts;


import java.util.List;

/**
 * Created by PC1 on 7/25/2016.
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder>{

    private List<Accounts> listAccounts;
    private Context mContext;
    public static final String TAG = "AccountsAdapter";

    /**
     * This is the class contsructor user to create new instance of this adapter
     * @param listAccounts
     * @param context
     */
    public AccountsAdapter(List<Accounts> listAccounts, Context context) {
        this.listAccounts = listAccounts;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_account, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Accounts accounts = listAccounts.get(i);
        viewHolder.accountIdTV.setText(""+accounts.getAccountId());
        viewHolder.accountNameTV.setText(accounts.getAccount_name());
        viewHolder.parentAccountIdTV.setText(""+accounts.getParent_id());
    }

    @Override
    public int getItemCount() {
        return listAccounts == null ? 0 : listAccounts.size();
    }


    /**
     * This method is used to clear all the items in the arraylist of resources
     */
    public void clearItems(){
        listAccounts.clear();
        notifyDataSetChanged();
    }

    /**
     * This method is used to add new item in the arraylist of project resources
     * @param accounts
     */
    public void addItem(Accounts accounts){
        listAccounts.add(accounts);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        MaterialRippleLayout rippleLayout;
        TextView accountNameTV;
        TextView accountIdTV;
        TextView parentAccountIdTV;
        ImageView openAccount;

        public ViewHolder(View itemView) {
            super(itemView);
            accountNameTV = (TextView) itemView.findViewById(R.id.account_name);
            accountIdTV = (TextView) itemView.findViewById(R.id.account_id);
            parentAccountIdTV = (TextView) itemView.findViewById(R.id.parent_id);
            openAccount = (ImageView) itemView.findViewById(R.id.open_info);
            openAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("parent_id", Integer.parseInt(parentAccountIdTV.getText().toString()));
                    bundle.putInt("account_id", Integer.parseInt(accountIdTV.getText().toString()));
                    bundle.putInt("position", getAdapterPosition());
                    bundle.putInt("size", listAccounts.size());
                    AccountViewFragment accountViewFragment = new AccountViewFragment();
                    accountViewFragment.setArguments(bundle);
                    MainActivity.replaceFragment(accountViewFragment, TAG);
                }
            });
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountsFragment accountsFragment = new AccountsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("parent_id", Integer.parseInt(accountIdTV.getText().toString()));
                    accountsFragment.setArguments(bundle);
                    MainActivity.replaceFragment(accountsFragment, TAG);
                }
            });
        }

    }
}
