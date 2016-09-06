package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.MyHandledTasks;

import java.util.List;

/**
 * Created by Vallejos Family on 9/5/2016.
 */
public class MyHandledTasksResponseWrapper {

    Response response;
    List<MyHandledTasks> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<MyHandledTasks> getData() {
        return data;
    }

    public void setData(List<MyHandledTasks> data) {
        this.data = data;
    }
}
