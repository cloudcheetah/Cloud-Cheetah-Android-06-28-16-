package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * This class is used to store all the task cash in and cash out in the mobile database
 * Created by Vallejos Family on 6/14/2016.
 *
 */

@Table(name = "TaskCashInCashout")
public class TaskCashInCashOut extends Model {

    @Column(name = "task_cash_in_out_id")
    int task_cash_in_out_id;
    @Column(name = "type")
    String type;
    @Column(name = "date")
    String date;
    @Column(name = "amount")
    double amount;
    @Column(name = "task_offline_id")
    long task_offline_id;
    @Column(name = "task_id")
    int task_id;
    @Column(name = "description")
    String description;
    @Column(name = "purpose")
    String purpose;
    @Column(name = "notes")
    String notes;
    @Column(name = "attachment")
    String attachment;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public int getTask_cash_in_out_id() {
        return task_cash_in_out_id;
    }

    public void setTask_cash_in_out_id(int task_cash_in_out_id) {
        this.task_cash_in_out_id = task_cash_in_out_id;
    }

    /**
     * This method is used to get all the cash in and cash out during offline model
     * @param task_offline_id
     * @return
     */
    public static List<TaskCashInCashOut> getOfflineCashInOut(long task_offline_id){
        return new Select().from(TaskCashInCashOut.class).where("task_offline_id = ?", task_offline_id).execute();
    }
}
