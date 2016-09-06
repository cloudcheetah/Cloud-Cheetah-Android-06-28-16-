package com.forateq.cloudcheetah.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.activeandroid.query.Delete;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.ConversationsAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Conversations;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.ConversationResponseWrapper;
import com.forateq.cloudcheetah.utils.ApplicationContext;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This fragment is used all the chat of the current user of the app
 * Created by Vallejos Family on 5/11/2016.
 */
public class ChatFragment extends Fragment {

    @Bind(R.id.list_conversations)
    RecyclerView listChats;
    @Bind(R.id.search)
    EditText searchEditText;
    private LinearLayoutManager mLinearLayoutManager;
    ConversationsAdapter conversationsAdapter;
    public static final String TAG = "ChatFragment";
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_fragment, container, false);
        return v;
    }

    public void init(){
        Log.e("Size", ""+ Users.getUsers().size());
        conversationsAdapter = new ConversationsAdapter(Conversations.getAllConversations(), getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        listChats.setAdapter(conversationsAdapter);
        listChats.setLayoutManager(mLinearLayoutManager);
        listChats.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) ApplicationContext.get()).getNetworkComponent().inject(this);
        getAllConversations();
    }

    public void getAllConversations(){
        MainFragment.progressBarLayout.setVisibility(View.VISIBLE);
        MainFragment.pager.setVisibility(View.GONE);
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
                        MainFragment.progressBarLayout.setVisibility(View.GONE);
                        MainFragment.pager.setVisibility(View.VISIBLE);
                        init();
                    }

                    @Override
                    public void onError(Throwable e) {
                        MainFragment.progressBarLayout.setVisibility(View.GONE);
                        MainFragment.pager.setVisibility(View.VISIBLE);
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

}
