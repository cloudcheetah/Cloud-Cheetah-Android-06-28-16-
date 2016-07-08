package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 6/30/2016.
 */
public class TaskBatchProcess {

    String name;
    long task_offline_id;
    long task_parent_offline_id;
    String start_date;
    String end_date;
    double budget;
    int duration;
    String description;
    String objectives;
    int person_responsible_id;
    int id;
    String parent_task_name;
    List<TaskBatchResources> task_resources;

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

    public int getPerson_responsible_id() {
        return person_responsible_id;
    }

    public void setPerson_responsible_id(int person_responsible_id) {
        this.person_responsible_id = person_responsible_id;
    }

    public List<TaskBatchResources> getTask_resources() {
        return task_resources;
    }

    public void setTask_resources(List<TaskBatchResources> task_resources) {
        this.task_resources = task_resources;
    }

    public long getTask_offline_id() {
        return task_offline_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTask_offline_id(long task_offline_id) {
        this.task_offline_id = task_offline_id;
    }

    public long getTask_parent_offline_id() {
        return task_parent_offline_id;
    }

    public void setTask_parent_offline_id(long task_parent_offline_id) {
        this.task_parent_offline_id = task_parent_offline_id;
    }

    public String getParent_task_name() {
        return parent_task_name;
    }

    public void setParent_task_name(String parent_task_name) {
        this.parent_task_name = parent_task_name;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }
}
