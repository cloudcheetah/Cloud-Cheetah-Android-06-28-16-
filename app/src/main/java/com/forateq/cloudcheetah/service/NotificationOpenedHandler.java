package com.forateq.cloudcheetah.service;

import android.content.Intent;

import com.forateq.cloudcheetah.CloudCheetahApp;
import com.forateq.cloudcheetah.MainActivity;
import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.pojo.JsonDataWrapper;
import com.forateq.cloudcheetah.pojo.SubTasks;
import com.forateq.cloudcheetah.utils.ApplicationContext;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

import org.json.JSONObject;



/**
 * Created by PC1 on 8/16/2016.
 */
public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {


    @Override
    public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
        Gson gson = new Gson();
        CloudCheetahApp.cloudCheetahApp.getNetworkComponent().inject(this);
        final JsonDataWrapper json = gson.fromJson(additionalData.toString(), JsonDataWrapper.class);
        if(json.getNotification_type() == 1){
            Messages messages = json.getJson().getMessage();
            Intent intent = new Intent(ApplicationContext.get(), MainActivity.class);
            CloudCheetahApp.notificationType = json.getNotification_type();
            CloudCheetahApp.currentReceiverId = messages.getReceiver_id();
            CloudCheetahApp.currentSenderId = messages.getSender_id();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationContext.get().startActivity(intent);
        }
        else if(json.getNotification_type() == 2){
            Messages messages = json.getJson().getMessage();
            CloudCheetahApp.notificationType = json.getNotification_type();
            CloudCheetahApp.projectChatId = messages.getProject_id();
            Intent intent = new Intent(ApplicationContext.get(), MainActivity.class);
            CloudCheetahApp.projectChatId = messages.getProject_id();
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationContext.get().startActivity(intent);
        }
        else if(json.getNotification_type() == 3){
            CloudCheetahApp.notificationType = json.getNotification_type();
            CloudCheetahApp.taskProgressReports = json.getJson().getProgress_report();
            Intent intent = new Intent(ApplicationContext.get(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationContext.get().startActivity(intent);
        }
        else if(json.getNotification_type() == 4){
            SubTasks subTasks = json.getJson().getTask();
            CloudCheetahApp.notificationType = json.getNotification_type();
            CloudCheetahApp.taskId = subTasks.getId();
            Intent intent = new Intent(ApplicationContext.get(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationContext.get().startActivity(intent);
        }


    }

    // This fires when a notification is opened by tapping on it.


        // The following can be used to open an Activity of your choice.

        // Intent intent = new Intent(getApplication(), YourActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);

        // Follow the instructions in the link below to prevent the launcher Activity from starting.
        // https://documentation.onesignal.com/docs/android-notification-customizations#changing-the-open-action-of-a-notification
}
