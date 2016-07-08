package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 6/29/2016.
 */
public class Projects {

    int id;
    String name;
    String start_date;
    String end_date;
    double budget;
    String description;
    double latitude;
    double longitude;
    String objectives;
    int created_by_id;
    String created_at;
    int modified_by_id;
    String modified_at;
    boolean delete_flag;
    int deleted_by_id;
    String deleted_at;
    int project_manager_id;
    int project_sponsor_id;
    String created_by;
    String modified_by;
    String deleted_by;
    String project_manager;
    String project_sponsor;
    List<ProjectResources> resources;
    List<ProjectMembers> members;

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

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public int getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(int created_by_id) {
        this.created_by_id = created_by_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getModified_by_id() {
        return modified_by_id;
    }

    public void setModified_by_id(int modified_by_id) {
        this.modified_by_id = modified_by_id;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public boolean isDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(boolean delete_flag) {
        this.delete_flag = delete_flag;
    }

    public int getDeleted_by_id() {
        return deleted_by_id;
    }

    public void setDeleted_by_id(int deleted_by_id) {
        this.deleted_by_id = deleted_by_id;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
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

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getDeleted_by() {
        return deleted_by;
    }

    public void setDeleted_by(String deleted_by) {
        this.deleted_by = deleted_by;
    }

    public String getProject_manager() {
        return project_manager;
    }

    public void setProject_manager(String project_manager) {
        this.project_manager = project_manager;
    }

    public String getProject_sponsor() {
        return project_sponsor;
    }

    public void setProject_sponsor(String project_sponsor) {
        this.project_sponsor = project_sponsor;
    }

    public List<ProjectResources> getResources() {
        return resources;
    }

    public void setResources(List<ProjectResources> resources) {
        this.resources = resources;
    }

    public List<ProjectMembers> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMembers> members) {
        this.members = members;
    }
}
