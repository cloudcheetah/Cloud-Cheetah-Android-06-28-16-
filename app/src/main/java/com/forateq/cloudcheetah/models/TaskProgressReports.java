package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/** This class is used to store all the progress reports of tasks in the mobile database
 * Created by Vallejos Family on 6/10/2016.
 */
@Table(name = "TaskProgressReports")
public class TaskProgressReports extends Model {

    @Column(name="task_progress_id")
    int task_progress_id;
    @Column(name="task_id")
    int task_id;
    @Column(name="task_offline_id")
    long task_offline_id;
    @Column(name = "task_name")
    String task_name;
    @Column(name="task_status")
    String task_status;
    @Column(name="report_date")
    String report_date;
    @Column(name="hours_work")
    int hours_work;
    @Column(name="percent_completion")
    int percent_completion;
    @Column(name="resources_used")
    String resources_used;
    @Column(name="task_action")
    int task_action;
    @Column(name="notes")
    String notes;
    @Column(name="concern_issues")
    String concerns_issues;
    @Column(name="change_request")
    String change_request;
    @Column(name = "is_submitted")
    boolean is_submitted;
    @Column(name = "attachment_1")
    String attachment_1;
    @Column(name = "attachment_2")
    String attachment_2;
    @Column(name = "attachment_3")
    String attachment_3;

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public long getTask_offline_id() {
        return task_offline_id;
    }

    public void setTask_offline_id(long task_offline_id) {
        this.task_offline_id = task_offline_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    public int getHours_work() {
        return hours_work;
    }

    public void setHours_work(int hours_work) {
        this.hours_work = hours_work;
    }

    public int getPercent_completion() {
        return percent_completion;
    }

    public void setPercent_completion(int percent_completion) {
        this.percent_completion = percent_completion;
    }

    public String getResources_used() {
        return resources_used;
    }

    public void setResources_used(String resources_used) {
        this.resources_used = resources_used;
    }

    public int getTask_action() {
        return task_action;
    }

    public void setTask_action(int task_action) {
        this.task_action = task_action;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getConcerns_issues() {
        return concerns_issues;
    }

    public void setConcerns_issues(String concerns_issues) {
        this.concerns_issues = concerns_issues;
    }

    public String getChange_request() {
        return change_request;
    }

    public void setChange_request(String change_request) {
        this.change_request = change_request;
    }

    public int getTask_progress_id() {
        return task_progress_id;
    }

    public void setTask_progress_id(int task_progress_id) {
        this.task_progress_id = task_progress_id;
    }

    public boolean is_submitted() {
        return is_submitted;
    }

    public void setIs_submitted(boolean is_submitted) {
        this.is_submitted = is_submitted;
    }

    public String getAttachment_1() {
        return attachment_1;
    }

    public void setAttachment_1(String attachment_1) {
        this.attachment_1 = attachment_1;
    }

    public String getAttachment_2() {
        return attachment_2;
    }

    public void setAttachment_2(String attachment_2) {
        this.attachment_2 = attachment_2;
    }

    public String getAttachment_3() {
        return attachment_3;
    }

    public void setAttachment_3(String attachment_3) {
        this.attachment_3 = attachment_3;
    }

    /**
     * This method is used to get all the progress reports of a specific task during offline mode
     * @param task_offline_id
     * @return List of Progress Reports
     */
    public static List<TaskProgressReports> getProgressOfflineReports(long task_offline_id){
        return new Select().from(TaskProgressReports.class).where("task_offline_id = ?", task_offline_id).execute();
    }

    public static List<TaskProgressReports> getProgressReports(int task_id){
        return new Select().from(TaskProgressReports.class).where("task_id = ?", task_id).execute();
    }

    /**
     * this method is used to get the details of a specific progress report during offline mode
     * @param task_progress_offline_id
     * @return Progress Report
     */
    public static TaskProgressReports getProgressOfflineReportById(long task_progress_offline_id){
        return new Select().from(TaskProgressReports.class).where("id = ?", task_progress_offline_id).executeSingle();
    }

    public static TaskProgressReports getProgressReportById(int task_progress_id){
        return new Select().from(TaskProgressReports.class).where("task_progress_id = ?", task_progress_id).executeSingle();
    }


}
