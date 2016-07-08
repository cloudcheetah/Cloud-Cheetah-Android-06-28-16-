package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 6/29/2016.
 */
public class ProjectListResponseWrapper {

    Response response;

    List<Projects> data;

    String timestamp;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<Projects> getData() {
        return data;
    }

    public void setData(List<Projects> data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
