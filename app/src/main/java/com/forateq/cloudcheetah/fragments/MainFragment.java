package com.forateq.cloudcheetah.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.ViewPagerAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Accounts;
import com.forateq.cloudcheetah.models.CashInOut;
import com.forateq.cloudcheetah.models.Conversations;
import com.forateq.cloudcheetah.models.Customers;
import com.forateq.cloudcheetah.models.Employees;
import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.models.MyTasks;
import com.forateq.cloudcheetah.models.Notifications;
import com.forateq.cloudcheetah.models.ProjectMembers;
import com.forateq.cloudcheetah.models.ProjectResources;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.Resources;
import com.forateq.cloudcheetah.models.TaskCashInCashOut;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.models.TaskResources;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.ToDo;
import com.forateq.cloudcheetah.models.Units;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.ConversationResponseWrapper;
import com.forateq.cloudcheetah.pojo.MessageListResponseWrapper;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.pojo.SingleTaskResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.forateq.cloudcheetah.utils.CustomViewPager;
import com.forateq.cloudcheetah.utils.SlidingTabLayout;
import com.onesignal.OneSignal;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/** This fragment is the container of all the major fragments of the app
 * Created by Vallejos Family on 5/11/2016.
 */
public class MainFragment extends Fragment {

    public static CustomViewPager pager;
    private ViewPagerAdapter adapter;
    @Bind(R.id.tabs)
    SlidingTabLayout tabs;
    public static LinearLayout progressBarLayout;
    @Bind(R.id.ripple_logout)
    MaterialRippleLayout rippleLogout;
    private CharSequence Titles[] = {"Home", "Contacts", "Chat", "ERP", "Profile"};
    private int Numboftabs = 5;
    public static final String TAG = "MainFragment";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;
    private AccountManager accountManager;
    private Account[] accounts;
    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.main_fragment, container, false);
        return v;
    }

    public void init(){
        progressBarLayout = (LinearLayout) v.findViewById(R.id.linlaHeaderProgress);
        pager = (CustomViewPager) v.findViewById(R.id.viewPager);
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
            else if(CloudCheetahApp.notificationType == 4){
                getTask();
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

    public void getTask(){
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
            Observable<SingleTaskResponseWrapper> observable = cloudCheetahAPIService.getSubTasks(CloudCheetahApp.taskId, userName, deviceid, sessionKey);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<SingleTaskResponseWrapper>() {
                        @Override
                        public void onCompleted() {
                            if(mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            Bundle bundle = new Bundle();
                            bundle.putInt("task_id", CloudCheetahApp.taskId);
                            TaskNotificationFragment taskNotificationFragment = new TaskNotificationFragment();
                            taskNotificationFragment.setArguments(bundle);
                            MainActivity.replaceFragment(taskNotificationFragment, TAG);
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
                        public void onNext(SingleTaskResponseWrapper singleTaskResponseWrapper) {
                            Tasks tasks = new Tasks();
                            tasks.setName(singleTaskResponseWrapper.getData().getName());
                            tasks.setStart_date(singleTaskResponseWrapper.getData().getStart_date());
                            tasks.setEnd_date(singleTaskResponseWrapper.getData().getEnd_date());
                            tasks.setBudget(singleTaskResponseWrapper.getData().getBudget());
                            tasks.setDescription(singleTaskResponseWrapper.getData().getDescription());
                            tasks.setPerson_responsible_id(singleTaskResponseWrapper.getData().getPerson_responsible_id());
                            tasks.setDuration(singleTaskResponseWrapper.getData().getDuration());
                            tasks.setTask_id(singleTaskResponseWrapper.getData().getId());
                            tasks.setProject_id(singleTaskResponseWrapper.getData().getProject_id());
                            tasks.save();
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "Error! No internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.ripple_logout)
    public void logout(){
        if(isNetworkAvailable()){
            final MaterialDialog.Builder createNoteDialog = new MaterialDialog.Builder(getActivity())
                    .title("Logout")
                    .content("Are you sure you want to logout?")
                    .contentColorRes(R.color.colorText)
                    .titleColorRes(R.color.colorText)
                    .backgroundColorRes(R.color.colorPrimary)
                    .widgetColorRes(R.color.colorText)
                    .positiveText("Ok")
                    .negativeText("Cancel")
                    .positiveColorRes(R.color.colorText)
                    .negativeColorRes(R.color.colorText)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setMessage("Logging out...");
                            mProgressDialog.show();
                            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
                            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
                            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
                            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            Log.e("Credentials", sessionKey + " " + userName + " " + deviceid);
                            Observable<ResponseWrapper> observable = cloudCheetahAPIService.logout(userName, deviceid, sessionKey);
                            observable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .unsubscribeOn(Schedulers.io())
                                    .subscribe(new Subscriber<ResponseWrapper>() {
                                        @Override
                                        public void onCompleted() {
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if(mProgressDialog.isShowing()){
                                                mProgressDialog.dismiss();
                                            }
                                            Log.e("Logout", e.getMessage(), e);
                                        }

                                        @Override
                                        public void onNext(ResponseWrapper responseWrapper) {
                                            if(responseWrapper.getResponse().getStatus_code() == AccountGeneral.SUCCESS_CODE){
                                                truncateTables();
                                                Toast.makeText(getActivity(), "You have logout successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getActivity(), "Error: "+responseWrapper.getResponse().getStatus_code(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                        }
                    });
            final MaterialDialog addNoteDialog = createNoteDialog.build();
            addNoteDialog.show();
        }
    }

    public void clearNotifications(){
        CloudCheetahApp.currentSenderId = 0;
        CloudCheetahApp.currentReceiverId = 0;
        CloudCheetahApp.notificationType = 0;
        CloudCheetahApp.projectChatId = 0;
        CloudCheetahApp.taskProgressReports = null;
    }

    public void truncateTables(){
        new Delete().from(Accounts.class).execute();
        new Delete().from(CashInOut.class).execute();
        new Delete().from(Conversations.class).execute();
        new Delete().from(Customers.class).execute();
        new Delete().from(Employees.class).execute();
        new Delete().from(Messages.class).execute();
        new Delete().from(MyTasks.class).execute();
        new Delete().from(ProjectMembers.class).execute();
        new Delete().from(ProjectResources.class).execute();
        new Delete().from(Projects.class).execute();
        new Delete().from(Resources.class).execute();
        new Delete().from(TaskCashInCashOut.class).execute();
        new Delete().from(TaskProgressReports.class).execute();
        new Delete().from(TaskResources.class).execute();
        new Delete().from(Tasks.class).execute();
        new Delete().from(Units.class).execute();
        new Delete().from(Users.class).execute();
        new Delete().from(Vendors.class).execute();
        new Delete().from(Conversations.class).execute();
        new Delete().from(Notifications.class).execute();
        new Delete().from(ToDo.class).execute();
        accountManager = AccountManager.get(getActivity());
        accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        accountManager.removeAccount(accounts[0], null, null);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AccountGeneral.ACCOUNT_USERNAME, "");
        editor.putString(AccountGeneral.SESSION_KEY, "");
        editor.putString(AccountGeneral.NOTIFICATION_ID, "");
        editor.putString(AccountGeneral.PROJECT_TIMESTAMP, "");
        editor.putString(AccountGeneral.USER_ID, "");
        editor.putBoolean(AccountGeneral.LOGIN_STATUS, false);
        editor.commit();
        OneSignal.setSubscription(false);
        Intent loginIntent = new Intent(getActivity(), MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(loginIntent);
    }

}
