package com.forateq.cloudcheetah.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.R;
import com.forateq.cloudcheetah.adapters.MessagingAdapter;
import com.forateq.cloudcheetah.adapters.VendorsAdapter;
import com.forateq.cloudcheetah.authenticate.AccountGeneral;
import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.models.Vendors;
import com.forateq.cloudcheetah.pojo.MessageResponseWrapper;
import com.forateq.cloudcheetah.pojo.NotificationIds;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by PC1 on 8/1/2016.
 */
public class SingleChatFragment extends Fragment {

    @Bind(R.id.ripple_back)
    MaterialRippleLayout backRipple;
    @Bind(R.id.list_chats)
    ListView listChats;
    @Bind(R.id.receiver_name_label)
    TextView receiverNameTV;
    @Bind(R.id.message)
    EditText messageET;
    @Bind(R.id.send)
    ImageView sendBT;
    Gson gson;
    public static final String TAG = "SingleChatFragment";
    public static MessagingAdapter messagingAdapter;
    private String receiverName;
    private int sender_id;
    private int receiver_id;
    @Inject
    CloudCheetahAPIService cloudCheetahAPIService;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.single_chat_fragment, container, false);
        receiverName = getArguments().getString("receiver_name");
        sender_id = getArguments().getInt("sender_id");
        receiver_id = getArguments().getInt("receiver_id");
        Log.e("Receiver: ", receiverName);
        return v;
    }

    public void init(){
        Log.e("Mobile Size: ", ""+Messages.getConversation(sender_id, receiver_id).size());
        messagingAdapter = new MessagingAdapter(getActivity(), Messages.getConversation(sender_id, receiver_id));
        listChats.setAdapter(messagingAdapter);
        receiverNameTV.setText(receiverName);
        gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .serializeNulls()
                .create();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((CloudCheetahApp) getActivity().getApplication()).getNetworkComponent().inject(this);
        init();
    }


    @OnClick(R.id.ripple_back)
    void back(){
        MainActivity.popFragment();
    }

    @OnClick(R.id.send)
    void sendMessage(){
        if(isNetworkAvailable()){
            Toast.makeText(getActivity(), "Sending...", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.get());
            String userName = sharedPreferences.getString(AccountGeneral.ACCOUNT_USERNAME, "");
            String sessionKey = sharedPreferences.getString(AccountGeneral.SESSION_KEY, "");
            String deviceid = Settings.Secure.getString(ApplicationContext.get().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Observable<MessageResponseWrapper> observable = cloudCheetahAPIService.sendMessage(userName, deviceid, sessionKey, Users.getUserId(receiverName), messageET.getText().toString());
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<MessageResponseWrapper>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("SingleChat", e.getMessage(), e);
                        }

                        @Override
                        public void onNext(MessageResponseWrapper messageResponseWrapper) {
                            Messages message = messageResponseWrapper.getData();
                            message.save();
                            String notification_ids = "";
                            for(int i=0; i<messageResponseWrapper.getSessions().size(); i++){
                                if(i == 0){
                                    notification_ids = notification_ids + "\"" + messageResponseWrapper.getSessions().get(i).getNotification_id() + "\"";
                                }
                                else{
                                    notification_ids = notification_ids + "," + "\"" + messageResponseWrapper.getSessions().get(i).getNotification_id() + "\"";
                                }
                            }
                            String data = gson.toJson(message);
                            messagingAdapter.addMessage(message);
//                            try {
//                                Log.e("Json", "{'contents': {'en':'"+ message.getMessage() +"'}, 'include_player_ids': [" + notification_ids + "], 'data': {'json':'"+ data+"', 'notification_type':'"+ 1 +"'}}");
//                                OneSignal.postNotification(new JSONObject("{'contents': {'en':'"+ message.getMessage() +"'}, 'include_player_ids': [" + notification_ids + "], 'data': {'json':'"+ data+"', 'notification_type':'"+ 1 +"'}}"), new OneSignal.PostNotificationResponseHandler() {
//                                    @Override
//                                    public void onSuccess(JSONObject response) {
//                                        Log.i("OneSignalExample", "postNotification Success: " + response.toString());
//                                    }
//
//                                    @Override
//                                    public void onFailure(JSONObject response) {
//                                        Log.e("OneSignalExample", "postNotification Failure: " + response.toString());
//                                    }
//                                });
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "Please connect to a network to send message.", Toast.LENGTH_SHORT).show();
        }
        messageET.setText("");
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

}
