package com.forateq.cloudcheetah.pojo;

/**
 * Created by Vallejos Family on 7/1/2016.
 */
public class TaskResponseWrapper {

    Response response;

    String timestamp;

    TaskData data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public TaskData getData() {
        return data;
    }

    public void setData(TaskData data) {
        this.data = data;
    }
}
