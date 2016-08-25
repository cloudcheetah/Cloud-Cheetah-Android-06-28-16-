package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by PC1 on 8/18/2016.
 */
@Table(name = "ToDo")
public class ToDo extends Model {

    @Column(name = "to_do_id")
    int id;
    @Column(name = "date")
    String date;
    @Column(name = "time")
    String time;
    @Column(name = "todo_note")
    String note;
    @Column(name ="user_id")
    int user_id;


    public int getToDoId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public static List<ToDo> getToDoByDate(String date){
        return new Select().from(ToDo.class).where("date = ?", date).execute();
    }

    public static ToDo getToDo(String date, String time){
        return new Select().from(ToDo.class).where("date = ? AND time = ?", date, time).executeSingle();
    }
}
