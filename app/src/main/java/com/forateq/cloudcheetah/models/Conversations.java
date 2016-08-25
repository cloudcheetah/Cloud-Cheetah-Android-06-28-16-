package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by PC1 on 8/3/2016.
 */
@Table(name = "Conversations")
public class Conversations extends Model {

    @Column(name = "group_id")
    int group_id;
    @Column(name = "user_id")
    int user_id;
    @Column(name = "last_message")
    String last_message;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public static List<Conversations> getAllConversations(){
        return new Select().from(Conversations.class).execute();
    }
}
