package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Customers;

/**
 * Created by PC1 on 7/26/2016.
 */
public class AddCustomerWrapper {

    Response response;
    Customers data;


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Customers getData() {
        return data;
    }

    public void setData(Customers data) {
        this.data = data;
    }
}
