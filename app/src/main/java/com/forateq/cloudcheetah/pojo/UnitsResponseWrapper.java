package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Units;

import java.util.List;

/**
 * Created by PC1 on 7/21/2016.
 */
public class UnitsResponseWrapper {

    Response response;
    List<Units> data;
    String timestamp;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<Units> getData() {
        return data;
    }

    public void setData(List<Units> data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
