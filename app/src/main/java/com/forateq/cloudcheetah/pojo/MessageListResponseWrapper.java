package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Messages;

import java.util.List;

/**
 * Created by PC1 on 8/3/2016.
 */
public class MessageListResponseWrapper {

    Response response;
    List<Messages> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<Messages> getData() {
        return data;
    }

    public void setData(List<Messages> data) {
        this.data = data;
    }
}
