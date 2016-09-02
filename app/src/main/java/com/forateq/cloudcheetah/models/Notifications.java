package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Vallejos Family on 9/1/2016.
 */
@Table(name = "Notifications")
public class Notifications extends Model {

    @Column(name = "notification_type")
    int notification_type;
    @Column(name = "notification_message")
    String notification_message;
    @Column(name = "notification_pointer_id")
    int notification_pointer_id;
    @Column(name = "sender_id")
    int sender_id;

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

    public static List<Notifications> getAllNotifications(){
        return new Select().from(Notifications.class).orderBy("id desc").execute();
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }
}
