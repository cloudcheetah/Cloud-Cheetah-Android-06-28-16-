package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Employees;

/**
 * Created by PC1 on 8/5/2016.
 */
public class EmployeeResponseWrapper {

    Response response;
    Employees data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Employees getData() {
        return data;
    }

    public void setData(Employees data) {
        this.data = data;
    }
}
