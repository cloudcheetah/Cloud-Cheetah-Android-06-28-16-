package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Conversations;

import java.util.List;

/**
 * Created by PC1 on 8/3/2016.
 */
public class ConversationResponseWrapper {

    Response response;
    List<Conversations> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<Conversations> getData() {
        return data;
    }

    public void setData(List<Conversations> data) {
        this.data = data;
    }
}
