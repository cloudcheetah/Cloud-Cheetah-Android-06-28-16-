package com.forateq.cloudcheetah.utils;

/**
 * Created by Vallejos Family on 9/1/2016.
 */
public class NotificationEvent {

    int notification_type;
    String json;

    public NotificationEvent(int notification_type, String json) {
        this.notification_type = notification_type;
        this.json = json;
    }

    public int getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(int notification_type) {
        this.notification_type = notification_type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
