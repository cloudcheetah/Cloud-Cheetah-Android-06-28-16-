package com.forateq.cloudcheetah.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.fragments.SingleChatFragment;
import com.forateq.cloudcheetah.models.Employees;
import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.MessageListResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This adapter is used to list all the contacts of the current user of the app
 *
 * Created by Vallejos Family on 5/20/2016.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{

    private List<Users> usersList;
    private Context mContext;
    public static final String TAG = "ContactAdapter";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;


    public ContactAdapter(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.mContext = context;
        ((CloudCheetahApp) ApplicationContext.get()).getNetworkComponent().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_contact, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Users users = usersList.get(i);
        viewHolder.contactName.setText(users.getFull_name());
        viewHolder.userName.setText(users.getUser_name());
        viewHolder.userId.setText(""+users.getUser_id());
        Employees employees = Employees.getEmployee(users.getEmployee_id());
        if(null != employees){
            Picasso.with(ApplicationContext.get()).load("http://"+employees.getImage()).placeholder( R.drawable.progress_animation ).resize(50, 50)
                    .centerCrop().into(viewHolder.profilePicIV);
        }
    }

    /**
     * This method is used to add item in the arraylist of users
     * @param users
     */
    public void addItem(Users users){
        usersList.add(users);
        notifyDataSetChanged();
    }

    /**
     * This method is used to clear all the items in the arraylist of users
     */
    public void clearItems(){
        usersList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return usersList == null ? 0 : usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView contactName;
        public TextView userId;
        public TextView userName;
        public ImageView deleteIV;
        public ImageView profilePicIV;

        public MaterialRippleLayout rippleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            userId = (TextView) itemView.findViewById(R.id.user_id);
            userName = (TextView) itemView.findViewById(R.id.contact_username);
            deleteIV = (ImageView) itemView.findViewById(R.id.remove_member);
            deleteIV.setVisibility(View.GONE);
            profilePicIV = (ImageView) itemView.findViewById(R.id.profile_pic);
            rippleLayout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            rippleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                    String currentUser = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                    String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                    String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    final int sender_id = Users.getUserIdByUserName(currentUser);
                    final int receiver_id = Users.getUserIdByUserName(userName.getText().toString());
                    if(isNetworkAvailable()){
                        final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage("Getting messages...");
                        mProgressDialog.show();
                        Observable<MessageListResponseWrapper> observable = cloudCheetahAPIService.getMessages(currentUser, deviceid, sessionKey, sender_id, receiver_id);
                        observable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<MessageListResponseWrapper>() {
                                    @Override
                                    public void onCompleted() {
                                        if(mProgressDialog.isShowing()){
                                            mProgressDialog.dismiss();
                                        }
                                        Bundle bundle = new Bundle();
                                        bundle.putString("receiver_name", contactName.getText().toString());
                                        bundle.putInt("sender_id", sender_id);
                                        bundle.putInt("receiver_id", receiver_id);
                                        Log.e("Receiver: ", contactName.getText().toString());
                                        SingleChatFragment singleChatFragment = new SingleChatFragment();
                                        singleChatFragment.setArguments(bundle);
                                        MainActivity.replaceFragment(singleChatFragment, TAG);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if(mProgressDialog.isShowing()){
                                            mProgressDialog.dismiss();
                                        }
                                        Log.e(TAG, e.getMessage(), e);
                                    }

                                    @Override
                                    public void onNext(MessageListResponseWrapper messageListResponseWrapper) {
                                        Messages.deleteConversation(sender_id, receiver_id);
                                        Log.e("API Size: ", ""+messageListResponseWrapper.getData().size());
                                        Log.e("Before Size: ", ""+Messages.getAllMessages().size());
                                        for(Messages messages : messageListResponseWrapper.getData()){
                                            Log.e("Receiver_Id: ", ""+messages.getReceiver_id());
                                            Log.e("Sender_Id: ", ""+messages.getSender_id());
                                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                                            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                                            if(messages.getSender_id() == Users.getUserIdByUserName(userName)){
                                                messages.setDirection(0);
                                            }
                                            else{
                                                messages.setDirection(1);
                                            }
                                            messages.save();
                                        }
                                        Log.e("After Size: ", ""+Messages.getAllMessages().size());
                                    }
                                });
                    }
                    else{
                        Bundle bundle = new Bundle();
                        bundle.putString("receiver_name", contactName.getText().toString());
                        bundle.putInt("sender_id", sender_id);
                        bundle.putInt("receiver_id", receiver_id);
                        Log.e("Receiver: ", contactName.getText().toString());
                        SingleChatFragment singleChatFragment = new SingleChatFragment();
                        singleChatFragment.setArguments(bundle);
                        MainActivity.replaceFragment(singleChatFragment, TAG);
                    }
                }
            });
        }

    }

    /**
     * Checks if there is a network available before login
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ApplicationContext.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
