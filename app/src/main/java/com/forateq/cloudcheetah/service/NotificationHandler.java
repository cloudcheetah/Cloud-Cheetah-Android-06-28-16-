package com.forateq.cloudcheetah.service;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.forateq.cloudcheetah.fragments.ProjectChatFragment;
import com.forateq.cloudcheetah.fragments.SingleChatFragment;
import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.JsonData;
import com.forateq.cloudcheetah.pojo.JsonDataWrapper;
import com.forateq.cloudcheetah.utils.MyLifeCycleHandler;
import com.google.gson.Gson;
import com.onesignal.OSNotificationPayload;
import com.onesignal.NotificationExtenderService;

import java.math.BigInteger;

public class NotificationHandler extends NotificationExtenderService {
    Gson gson;
    @Override
    protected boolean onNotificationProcessing(final OSNotificationPayload notification) {
        gson = new Gson();
        Log.e("OneSignalExample", "Data: " + notification.additionalData);
        Log.e("OneSignalExample", "Data: " + notification.message);
        final JsonDataWrapper jsonWrapper = gson.fromJson(notification.additionalData.toString(), JsonDataWrapper.class);
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                if(jsonWrapper.getNotification_type() == 1){
                    Messages messages = jsonWrapper.getJson().getMessage();
                    return builder.setColor(new BigInteger("FF00FF00", 16).intValue()).setContentText(messages.getMessage()).setContentTitle(Users.getFullName(messages.getSender_id()));
                }
                else if(jsonWrapper.getNotification_type() == 2){
                    Messages messages = jsonWrapper.getJson().getMessage();
                    return builder.setColor(new BigInteger("FF00FF00", 16).intValue()).setContentText(Users.getFullName(messages.getSender_id()) + " " + "replied to "+ Projects.getProjectById(messages.getProject_id()).getName()).setContentTitle("Project Chat");
                }
                else if(jsonWrapper.getNotification_type() == 3){
                    TaskProgressReports taskProgressReports = jsonWrapper.getJson().getProgress_report();
                    return builder.setColor(new BigInteger("FF00FF00", 16).intValue()).setContentText(Users.getFullName(Tasks.getTaskById(taskProgressReports.getTask_id()).getPerson_responsible_id()) + " " + "submitted a progress report.").setContentTitle("Task Progress Report");
                }
                else{
                    return builder;
                }

            }
        };
        if(MyLifeCycleHandler.isApplicationInForeground() || MyLifeCycleHandler.isApplicationVisible()){
            if(jsonWrapper.getNotification_type() == 1 || jsonWrapper.getNotification_type() == 2){
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.post(new Runnable() {
                    public void run()
                    {
                        Messages messages = jsonWrapper.getJson().getMessage();
                        if(messages.getProject_id() != 0){
                            if(ProjectChatFragment.messagingAdapter != null){
                                ProjectChatFragment.messagingAdapter.addMessage(messages);
                            }
                        }
                        else{
                            if(SingleChatFragment.messagingAdapter != null){
                                SingleChatFragment.messagingAdapter.addMessage(messages);
                            }
                        }
                        messages.setDirection(1);
                        messages.save();
                    }
                });
            }
            else if(jsonWrapper.getNotification_type() == 3){
                displayNotification(overrideSettings);
            }
        }
        else{
            displayNotification(overrideSettings);
        }
        return true;
    }



}
