package com.forateq.cloudcheetah.service;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.forateq.cloudcheetah.fragments.ProjectChatFragment;
import com.forateq.cloudcheetah.fragments.SingleChatFragment;
import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.models.Projects;
import com.forateq.cloudcheetah.models.TaskProgressReports;
import com.forateq.cloudcheetah.models.Tasks;
import com.forateq.cloudcheetah.models.Users;
import com.forateq.cloudcheetah.pojo.JsonDataWrapper;
import com.forateq.cloudcheetah.pojo.ProjectsNotificationWrapper;
import com.forateq.cloudcheetah.pojo.SubTasks;
import com.forateq.cloudcheetah.utils.MyLifeCycleHandler;
import com.forateq.cloudcheetah.utils.NotificationEvent;
import com.google.gson.Gson;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationPayload;

import org.greenrobot.eventbus.EventBus;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotificationHandler extends NotificationExtenderService {
    Gson gson;
    @Override
    protected boolean onNotificationProcessing(final OSNotificationPayload notification) {
        Calendar c = Calendar.getInstance();
        Date mTime = c.getTime();
        final String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mTime);
        Log.e("Time", timestamp);
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
                    NotificationEvent notificationEvent = new NotificationEvent(jsonWrapper.getNotification_type(), notification.message, Tasks.getTaskById(taskProgressReports.getTask_id()).getPerson_responsible_id(),taskProgressReports.getTask_progress_id(), timestamp);
                    EventBus.getDefault().post(notificationEvent);
                    return builder.setColor(new BigInteger("FF00FF00", 16).intValue()).setContentText(notification.message).setContentTitle("Task Progress Report");
                }
                else if(jsonWrapper.getNotification_type() == 4){
                    SubTasks subTasks = jsonWrapper.getJson().getTask();
                    NotificationEvent notificationEvent = new NotificationEvent(jsonWrapper.getNotification_type(), notification.message, 0, subTasks.getId(), timestamp);
                    EventBus.getDefault().post(notificationEvent);
                    return builder.setColor(new BigInteger("FF00FF00", 16).intValue()).setContentText(notification.message).setContentTitle("Task");
                }
                else if(jsonWrapper.getNotification_type() == 5){
                    ProjectsNotificationWrapper project = jsonWrapper.getJson().getProject();
                    NotificationEvent notificationEvent = new NotificationEvent(jsonWrapper.getNotification_type(), notification.message, 0, project.getId(), timestamp);
                    EventBus.getDefault().post(notificationEvent);
                    return builder.setColor(new BigInteger("FF00FF00", 16).intValue()).setContentText(notification.message).setContentTitle("Project");
                }
                else if(jsonWrapper.getNotification_type() == 6){
                    int project_id = jsonWrapper.getJson().getProject_id();
                    NotificationEvent notificationEvent = new NotificationEvent(jsonWrapper.getNotification_type(), notification.message, 0, project_id, timestamp);
                    EventBus.getDefault().post(notificationEvent);
                    return builder.setColor(new BigInteger("FF00FF00", 16).intValue()).setContentText(notification.message).setContentTitle("Project");
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
            else if(jsonWrapper.getNotification_type() == 4){
                displayNotification(overrideSettings);
            }
            else if(jsonWrapper.getNotification_type() == 5){
                displayNotification(overrideSettings);
            }
            else if(jsonWrapper.getNotification_type() == 6){
                displayNotification(overrideSettings);
            }
        }
        else{
            displayNotification(overrideSettings);
        }
        return true;
    }



}
