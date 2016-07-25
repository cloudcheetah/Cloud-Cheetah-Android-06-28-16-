package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.MyTasks;

import java.util.List;

/**
 * Created by PC1 on 7/22/2016.
 */
public class MyTasksResponseWrapper {

    Response response;
    List<MyTasks> data;
    String timestamp;

    public List<MyTasks> getData() {
        return data;
    }

    public void setData(List<MyTasks> data) {
        this.data = data;
    }

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
}
