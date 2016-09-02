package com.forateq.cloudcheetah.pojo;

/**
 * Created by Vallejos Family on 9/2/2016.
 */
public class ProjectsNotificationWrapper {

    double budget;
    String project_manager;
    int id;
    int project_manager_id;
    String end_date;
    String description;
    String name;
    String project_sponsor;
    int project_sponsor_id;
    String objectives;
    String start_date;

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getProject_manager() {
        return project_manager;
    }

    public void setProject_manager(String project_manager) {
        this.project_manager = project_manager;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProject_manager_id() {
        return project_manager_id;
    }

    public void setProject_manager_id(int project_manager_id) {
        this.project_manager_id = project_manager_id;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProject_sponsor() {
        return project_sponsor;
    }

    public void setProject_sponsor(String project_sponsor) {
        this.project_sponsor = project_sponsor;
    }

    public int getProject_sponsor_id() {
        return project_sponsor_id;
    }

    public void setProject_sponsor_id(int project_sponsor_id) {
        this.project_sponsor_id = project_sponsor_id;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
