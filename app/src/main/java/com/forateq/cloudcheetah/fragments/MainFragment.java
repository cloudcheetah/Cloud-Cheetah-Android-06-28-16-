package com.forateq.cloudcheetah.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.ViewPagerAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Conversations;
import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.ConversationResponseWrapper;
import com.forateq.cloudcheetah.pojo.MessageListResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.utils.CustomViewPager;
import com.forateq.cloudcheetah.utils.SlidingTabLayout;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** This fragment is the container of all the major fragments of the app
 * Created by Vallejos Family on 5/11/2016.
 */
public class MainFragment extends Fragment {

    @Bind(R.id.viewPager)
    CustomViewPager pager;
    private ViewPagerAdapter adapter;
    @Bind(R.id.tabs)
    SlidingTabLayout tabs;
    @Bind(R.id.linlaHeaderProgress)
    LinearLayout progressBarLayout;
    private CharSequence Titles[] = {"Home", "Contacts", "Chat", "ERP", "Profile"};
    private int Numboftabs = 5;
    public static final String TAG = "MainFragment";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        return v;
    }

    public void init(){
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorLightPrimary);
            }
        });
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:{
                        Log.e("Home ", "Fragment");
                        break;
                    }
                    case 1:{
                        Log.e("Contacts ", "Fragment");
                        break;
                    }
                    case 2:{
                        Log.e("Chat ", "Fragment");
                        if(isNetworkAvailable()){
                            getAllConversations();
                        }
                        break;
                    }
                    case 3:{
                        Log.e("ERP ", "Fragment");
                        break;
                    }
                    case 4:{
                        Log.e("Profile ", "Fragment");
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter =  new ViewPagerAdapter(getChildFragmentManager(), Titles, Numboftabs, CloudCheetahApp.contactsFragment, CloudCheetahApp.chatFragment, CloudCheetahApp.erpFragment, CloudCheetahApp.profileFragment, CloudCheetahApp.homeFragment);
        pager.setAdapter(adapter);
        if(CloudCheetahApp.notificationType != 0){
            if(CloudCheetahApp.notificationType == 1){
                getSingleChatMessages();
            }
            else if(CloudCheetahApp.notificationType == 2){
                getProjectChatMessages();
            }
            else if(CloudCheetahApp.notificationType == 3){
                getProgressReport();
            }
        }
        tabs.setViewPager(pager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) ApplicationContext.get()).getNetworkComponent().inject(this);
        init();
    }


    /**
     * Checks if there is a network available before login
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getAllConversations(){
        progressBarLayout.setVisibility(View.VISIBLE);
        pager.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
        String currentUser = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
        String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
        String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Observable<ConversationResponseWrapper> observable = cloudCheetahAPIService.getConversations(currentUser, deviceid, sessionKey);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ConversationResponseWrapper>() {
                    @Override
                    public void onCompleted() {
                        progressBarLayout.setVisibility(View.GONE);
                        pager.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBarLayout.setVisibility(View.GONE);
                        pager.setVisibility(View.VISIBLE);
                        Log.e(TAG, e.getMessage(), e);
                    }

                    @Override
                    public void onNext(ConversationResponseWrapper conversationResponseWrapper) {
                        Log.e("Conversation Size: ", ""+conversationResponseWrapper.getData().size());
                        new Delete().from(Conversations.class).execute();
                        for(Conversations conversations : conversationResponseWrapper.getData()){
                            Log.e("Conversation Id", ""+conversations.getUser_id());
                            conversations.save();
                        }
                    }
                });
    }

    public void getSingleChatMessages(){
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Getting messages...");
        mProgressDialog.show();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String currentUser = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            final int sender_id = CloudCheetahApp.currentSenderId;
            final int receiver_id = CloudCheetahApp.currentReceiverId;
            if(isNetworkAvailable()){
                Toast.makeText(ApplicationContext.get(), "Getting messages...", Toast.LENGTH_SHORT).show();
                Observable<MessageListResponseWrapper> observable = cloudCheetahAPIService.getMessages(currentUser, deviceid, sessionKey, sender_id, receiver_id);
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<MessageListResponseWrapper>() {
                            @Override
                            public void onCompleted() {
                                Bundle bundle = new Bundle();
                                bundle.putString("receiver_name", Users.getUser(receiver_id).getFull_name());
                                bundle.putInt("sender_id", sender_id);
                                bundle.putInt("receiver_id", receiver_id);
                                SingleChatFragment singleChatFragment = new SingleChatFragment();
                                singleChatFragment.setArguments(bundle);
                                MainActivity.replaceFragment(singleChatFragment, "Handler");
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                                clearNotifications();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("Handler", e.getMessage(), e);
                                if(mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
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
                bundle.putString("receiver_name", Users.getUser(CloudCheetahApp.currentReceiverId).getUser_name());
                bundle.putInt("sender_id", sender_id);
                bundle.putInt("receiver_id", receiver_id);
                SingleChatFragment singleChatFragment = new SingleChatFragment();
                singleChatFragment.setArguments(bundle);
                MainActivity.replaceFragment(singleChatFragment, "Handler");
            }
    }

    public void getProjectChatMessages(){
        Log.e("Processing", "in progress");
        if(isNetworkAvailable()){
            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage("Processing...");
            mProgressDialog.show();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Observable<MessageListResponseWrapper> observable = cloudCheetahAPIService.getProjectMessages(userName, deviceid, sessionKey, CloudCheetahApp.projectChatId);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<MessageListResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            Projects project = Projects.getProjectById(CloudCheetahApp.projectChatId);
                            Bundle bundle = new Bundle();
                            bundle.putInt("project_id", CloudCheetahApp.projectChatId);
                            bundle.putString("project_name", project.getName());
                            ProjectChatFragment projectChatFragment = new ProjectChatFragment();
                            projectChatFragment.setArguments(bundle);
                            MainActivity.replaceFragment(projectChatFragment, TAG);
                            clearNotifications();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            Log.e("GetProjectMessage", e.getMessage(), e);
                        }

                        @Override
                        public void onNext(MessageListResponseWrapper messageListResponseWrapper) {
                            Messages.deleteProjectMessages(CloudCheetahApp.projectChatId);
                            for(Messages messages : messageListResponseWrapper.getData()){
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
                        }
                    });
        }
        else{
            Projects project = Projects.getProjectById(CloudCheetahApp.projectChatId);
            Bundle bundle = new Bundle();
            bundle.putInt("project_id", CloudCheetahApp.projectChatId);
            bundle.putString("project_name", project.getName());
            ProjectChatFragment projectChatFragment = new ProjectChatFragment();
            projectChatFragment.setArguments(bundle);
            MainActivity.replaceFragment(projectChatFragment, TAG);
        }
    }

    public void getProgressReport(){
        //to be created
        clearNotifications();
    }

    public void clearNotifications(){
        CloudCheetahApp.currentSenderId = 0;
        CloudCheetahApp.currentReceiverId = 0;
        CloudCheetahApp.notificationType = 0;
        CloudCheetahApp.projectChatId = 0;
        CloudCheetahApp.taskProgressReports = null;
    }

}
