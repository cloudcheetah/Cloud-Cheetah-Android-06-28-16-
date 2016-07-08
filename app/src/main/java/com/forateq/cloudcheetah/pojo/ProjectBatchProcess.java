package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 6/30/2016.
 */
public class ProjectBatchProcess {

    int id; //default 0
    String name;
    String start_date;
    String end_date;
    double budget;
    String description;
    String objectives;
    int project_manager_id;
    int project_sponsor_id;
    List<TaskBatchProcess> tasks;
    List<ProjectBatchResources> project_resources;
    List<ProjectBatchMembers> project_members;
    List<TaskBatchResources> task_resources;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public int getProject_manager_id() {
        return project_manager_id;
    }

    public void setProject_manager_id(int project_manager_id) {
        this.project_manager_id = project_manager_id;
    }

    public int getProject_sponsor_id() {
        return project_sponsor_id;
    }

    public void setProject_sponsor_id(int project_sponsor_id) {
        this.project_sponsor_id = project_sponsor_id;
    }

    public List<TaskBatchProcess> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskBatchProcess> tasks) {
        this.tasks = tasks;
    }

    public List<ProjectBatchResources> getProject_resources() {
        return project_resources;
    }

    public void setProject_resources(List<ProjectBatchResources> project_resources) {
        this.project_resources = project_resources;
    }

    public List<ProjectBatchMembers> getProject_members() {
        return project_members;
    }

    public void setProject_members(List<ProjectBatchMembers> project_members) {
        this.project_members = project_members;
    }

    public List<TaskBatchResources> getTask_resources() {
        return task_resources;
    }

    public void setTask_resources(List<TaskBatchResources> task_resources) {
        this.task_resources = task_resources;
    }
}
