package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/** This class is used to store all the resources of the tasks
 * Created by Vallejos Family on 5/31/2016.
 */
@Table(name = "TaskResources")
public class TaskResources extends Model {

    @Column(name = "task_resource_id")
    int task_resource_id;
    @Column(name = "resource_id")
    int resource_id;
    @Column(name = "task_offline_id")
    long task_offline_id;
    @Column(name = "task_id")
    int task_id;
    @Column(name = "quantity")
    int quantity;


    public int getTask_resource_id() {
        return task_resource_id;
    }

    public void setTask_resource_id(int task_resource_id) {
        this.task_resource_id = task_resource_id;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public long getTask_offline_id() {
        return task_offline_id;
    }

    public void setTask_offline_id(long task_offline_id) {
        this.task_offline_id = task_offline_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * This method is used to get all the resources of a specific task during offline mode
     * @param task_offline_id
     * @return List of Tasks Resources
     */
    public static List<TaskResources> getTaskResourcesOffline(long task_offline_id){
        return new Select().from(TaskResources.class).where("task_offline_id = ?", task_offline_id).execute();
    }

    public static List<TaskResources> getTaskResourcesOnline(int task_id){
        return new Select().from(TaskResources.class).where("task_id = ?", task_id).execute();
    }

    /**
     * This method is used to get all the resource name of a specific task during offline mode
     * @param task_offline_id
     * @return List of task Resource Name
     */
    public static List<String> getTaskResourcesOfflineProgress(long task_offline_id){
        List<String> resourcesNameList = new ArrayList<>();
        List<TaskResources> taskResourcesList = new Select().from(TaskResources.class).where("task_offline_id = ?", task_offline_id).execute();
        for(TaskResources taskResources : taskResourcesList){
            Resources resources = Resources.getResource(taskResources.getResource_id());
            resourcesNameList.add(resources.getName());
        }
        return resourcesNameList;
    }

    public static List<String> getTaskResourceOnline(int task_id){
        List<String> resourcesNameList = new ArrayList<>();
        List<TaskResources> taskResourcesList = new Select().from(TaskResources.class).where("task_id = ?", task_id).execute();
        for(TaskResources taskResources : taskResourcesList){
            Resources resources = Resources.getResource(taskResources.getResource_id());
            resourcesNameList.add(resources.getName());
        }
        return resourcesNameList;
    }


    public static TaskResources getTaskResourceById(int task_resource_id){
        return new Select().from(TaskResources.class).where("task_resource_id = ?", task_resource_id).executeSingle();
    }
}
