package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Customers;

import java.util.List;

/**
 * Created by PC1 on 7/11/2016.
 */
public class CustomerListResponseWrapper {

    Response response;
    List<Customers> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<Customers> getData() {
        return data;
    }

    public void setData(List<Customers> data) {
        this.data = data;
    }
}
