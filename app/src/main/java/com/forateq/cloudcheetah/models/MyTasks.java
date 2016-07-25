package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by PC1 on 7/22/2016.
 */
@Table(name = "MyTasks")
public class MyTasks extends Model {

    @Column(name = "task_id")
    int id;
    @Column(name = "project_id")
    int project_id;
    @Column(name = "name")
    String name;
    @Column(name = "start_date")
    String start_date;
    @Column(name = "end_date")
    String end_date;
    @Column(name = "budget")
    double budget;
    @Column(name = "duration")
    int duration;
    @Column(name = "description")
    String description;
    @Column(name = "latitude")
    double latitude;
    @Column(name = "longitude")
    double longitude;
    @Column(name = "parent_id")
    int parent_id;
    @Column(name = "person_responsible_id")
    int person_responsible_id;


    public int getTaskId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getPerson_responsible_id() {
        return person_responsible_id;
    }

    public void setPerson_responsible_id(int person_responsible_id) {
        this.person_responsible_id = person_responsible_id;
    }

    public static List<MyTasks> getMyTasks(){
        return new Select().from(MyTasks.class).execute();
    }

    public static MyTasks getMyTask(int task_id){
        return new Select().from(MyTasks.class).where("task_id = ?", task_id).executeSingle();
    }
}
