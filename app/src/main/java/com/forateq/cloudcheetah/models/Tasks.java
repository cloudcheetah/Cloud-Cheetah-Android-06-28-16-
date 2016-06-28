package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.util.List;

/** This class is used to store all the tasks in the mobile database
 * Created by Vallejos Family on 5/19/2016.
 */
@Table(name = "Tasks")
public class Tasks extends Model {

    @Column(name = "task_id")
    int task_id;
    @Column(name = "project_id")
    int project_id;
    @Column(name = "project_offline_id")
    long project_offline_id;
    @Column(name = "task_name")
    String name;
    @Column(name = "task_start_date")
    String start_date;
    @Column(name = "task_end_date")
    String end_date;
    @Column(name = "task_budget")
    double budget;
    @Column(name = "task_duration")
    int duration;
    @Column(name = "task_description")
    String description;
    @Column(name = "task_latitude")
    double latitude;
    @Column(name = "task_longitude")
    double longitide;
    @Column(name = "task_parent_id")
    int parent_id;
    @Column(name = "parent_offline_id")
    long parent_offline_id;
    @Column(name = "task_person_responsible_id")
    int person_responsible_id;

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
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

    public double getLongitide() {
        return longitide;
    }

    public void setLongitide(double longitide) {
        this.longitide = longitide;
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

    public long getProject_offline_id() {
        return project_offline_id;
    }

    public void setProject_offline_id(long project_offline_id) {
        this.project_offline_id = project_offline_id;
    }

    public long getParent_offline_id() {
        return parent_offline_id;
    }

    public void setParent_offline_id(long parent_offline_id) {
        this.parent_offline_id = parent_offline_id;
    }

    /**
     * This method is used to get all the tasks of a specific project
     * @param project_id
     * @param parent_id
     * @return
     */
    public static List<Tasks> getTasks(int project_id, int parent_id){
        return new Select().from(Tasks.class).where("project_id = ? AND task_parent_id = ?", project_id, parent_id).execute();
    }

    /**
     * This method is used to get all the tasks of a specific project during offlne mode
     * @param project_offline_id
     * @param parent_offline_id
     * @return List of Tasks
     */
    public static List<Tasks> getTasksOffline(long project_offline_id, long parent_offline_id){
        return new Select().from(Tasks.class).where("project_offline_id = ? AND parent_offline_id = ?", project_offline_id, parent_offline_id).execute();
    }

    /**
     * This method is used to search all the tasks of a specific project
     * @param search
     * @param project_id
     * @param parent_id
     * @return List of tasks similar to search string
     */
    public static List<Tasks> getSearchTasks(String search, int project_id, int parent_id){
        String [] selectionArgs = new String[] {"%" + search + "%", ""+project_id, ""+parent_id };
        List<Tasks> searchTasks = SQLiteUtils.rawQuery(Projects.class,
                "SELECT * FROM Projects WHERE project_name  LIKE ? AND project_id = ? AND parent_id = ?",
                selectionArgs);
        return searchTasks;
    }

    /**
     * This method is used to get a specific task using task id
     * @param task_id
     * @return Task
     */
    public static Tasks getTaskById(int task_id){
        return new Select().from(Tasks.class).where("task_id = ?", task_id).executeSingle();
    }

    /**
     * This method is used to get a specific task using task offline id during offline mode
     * @param task_offline_id
     * @return Task
     */
    public static Tasks getTaskByOfflineId(long task_offline_id){
        return new Select().from(Tasks.class).where("id = ?", task_offline_id).executeSingle();
    }

    /**
     * This method is used to get all the tasks of a specific person responsible
     * @param person_responsible_id
     * @return List of Tasks
     */
    public static List<Tasks> getTasksByPersonResponsibleId(int person_responsible_id){
        return new Select().from(Tasks.class).where("task_person_responsible_id = ?", person_responsible_id).execute();
    }

}
