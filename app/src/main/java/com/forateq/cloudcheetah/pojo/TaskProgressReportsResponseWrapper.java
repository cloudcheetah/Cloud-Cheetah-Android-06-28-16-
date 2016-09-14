package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 8/31/2016.
 */
public class TaskProgressReportsResponseWrapper {

    Response response;
    List<TaskProgressResponse> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<TaskProgressResponse> getData() {
        return data;
    }

    public void setData(List<TaskProgressResponse> data) {
        this.data = data;
    }
}
