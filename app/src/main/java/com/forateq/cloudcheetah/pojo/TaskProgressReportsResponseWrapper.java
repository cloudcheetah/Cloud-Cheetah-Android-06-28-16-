package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.TaskProgressReports;

import java.util.List;

/**
 * Created by Vallejos Family on 8/31/2016.
 */
public class TaskProgressReportsResponseWrapper {

    Response response;
    List<TaskProgressReports> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<TaskProgressReports> getData() {
        return data;
    }

    public void setData(List<TaskProgressReports> data) {
        this.data = data;
    }
}
