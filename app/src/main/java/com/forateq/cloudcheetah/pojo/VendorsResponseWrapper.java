package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Vendors;

import java.util.List;

/**
 * Created by PC1 on 7/21/2016.
 */
public class VendorsResponseWrapper {

    Response response;
    List<Vendors> data;
    String timestamp;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<Vendors> getData() {
        return data;
    }

    public void setData(List<Vendors> data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
