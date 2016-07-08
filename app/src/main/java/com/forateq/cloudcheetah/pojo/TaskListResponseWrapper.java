package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 7/1/2016.
 */
public class TaskListResponseWrapper {

    Response response;
    List<TaskData> data;
    String timestamp;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<TaskData> getData() {
        return data;
    }

    public void setData(List<TaskData> data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
