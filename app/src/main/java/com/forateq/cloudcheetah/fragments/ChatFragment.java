package com.forateq.cloudcheetah.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.ContactAdapter;
import com.forateq.cloudcheetah.adapters.ConversationsAdapter;
import com.forateq.cloudcheetah.models.Conversations;
import com.forateq.cloudcheetah.models.Users;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        init();
    }

}
