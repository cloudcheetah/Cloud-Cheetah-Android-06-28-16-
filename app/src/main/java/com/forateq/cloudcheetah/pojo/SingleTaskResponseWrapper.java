package com.forateq.cloudcheetah.pojo;

/**
 * Created by PC1 on 7/20/2016.
 */
public class SingleTaskResponseWrapper {

    Response response;
    TaskData data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public TaskData getData() {
        return data;
    }

    public void setData(TaskData data) {
        this.data = data;
    }
}
