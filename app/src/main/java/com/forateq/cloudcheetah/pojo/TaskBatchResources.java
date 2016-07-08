package com.forateq.cloudcheetah.pojo;

/**
 * Created by Vallejos Family on 6/30/2016.
 */
public class TaskBatchResources {

    long task_offline_id;
    int resource_id;
    int quantity;
    int id;

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTask_offline_id() {
        return task_offline_id;
    }

    public void setTask_offline_id(long task_offline_id) {
        this.task_offline_id = task_offline_id;
    }
}
