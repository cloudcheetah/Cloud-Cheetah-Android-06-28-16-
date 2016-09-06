package com.forateq.cloudcheetah.utils;

/**
 * Created by Vallejos Family on 9/1/2016.
 */
public class NotificationEvent {

    int notification_type;
    String notification_message;
    int sender_id;
    int notification_pointer_id;
    String timestamp;

    public NotificationEvent(int notification_type, String notification_message, int sender_id, int notification_pointer_id, String timestamp) {
        this.notification_type = notification_type;
        this.notification_message = notification_message;
        this.sender_id = sender_id;
        this.notification_pointer_id = notification_pointer_id;
        this.timestamp = timestamp;
    }

    public int getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(int notification_type) {
        this.notification_type = notification_type;
    }

    public String getNotification_message() {
        return notification_message;
    }

    public void setNotification_message(String notification_message) {
        this.notification_message = notification_message;
    }

    public int getNotification_pointer_id() {
        return notification_pointer_id;
    }

    public void setNotification_pointer_id(int notification_pointer_id) {
        this.notification_pointer_id = notification_pointer_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
