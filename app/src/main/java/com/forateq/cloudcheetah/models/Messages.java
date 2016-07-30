package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by PC1 on 7/29/2016.
 */
@Table(name = "Messages")
public class Messages extends Model{
    @Column(name = "message_id")
    int id;
    @Column(name = "message")
    String message;
    @Column(name = "receiver_id")
    int receiver_id;
    @Column(name = "sender_id")
    int sender_id;
    @Column(name = "timestamp")
    String timestamp;
    @Column(name = "direction")
    int direction;

    public int getMessageId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }
}
