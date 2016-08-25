package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Messages;
import com.forateq.cloudcheetah.models.TaskProgressReports;

/**
 * Created by PC1 on 8/2/2016.
 */
public class JsonData {
    Messages message;
    TaskProgressReports progress_report;

    public Messages getMessage() {
        return message;
    }

    public void setMessage(Messages message) {
        this.message = message;
    }

    public TaskProgressReports getProgress_report() {
        return progress_report;
    }

    public void setProgress_report(TaskProgressReports progress_report) {
        this.progress_report = progress_report;
    }
}
