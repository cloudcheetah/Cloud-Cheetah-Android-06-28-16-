package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by PC1 on 7/29/2016.
 */
@Table(name = "Messages")
public class Messages extends Model{
    @Column(name = "message_id")
    int id;
    @Column(name = "project_id")
    int project_id;
    @Column(name = "message")
    String message;
    @Column(name = "receiver_id")
    int receiver_id;
    @Column(name = "sender_id")
    int sender_id;
    @Column(name = "timestamp")
    String created_at;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public static List<Messages> getAllMessages(){
        return new Select().from(Messages.class).orderBy("id").execute();
    }

    public static List<Messages> getConversation(int sender_id, int receiver_id){
        return new Select().from(Messages.class).where("(sender_id = ? AND receiver_id = ?) OR (receiver_id = ? AND sender_id = ?)", sender_id, receiver_id, sender_id, receiver_id).orderBy("message_id").execute();
    }

    public static void deleteConversation(int sender_id, int receiver_id){
        new Delete().from(Messages.class).where("(sender_id = ? AND receiver_id = ?) OR (receiver_id = ? AND sender_id = ?)", sender_id, receiver_id, sender_id, receiver_id).execute();
    }

    public static List<Messages> getProjectMessages(int project_id){
        return new Select().from(Messages.class).where("project_id = ?", project_id).execute();
    }

    public static void deleteProjectMessages(int project_id){
        new Delete().from(Messages.class).where("project_id = ?", project_id).execute();
    }
}
