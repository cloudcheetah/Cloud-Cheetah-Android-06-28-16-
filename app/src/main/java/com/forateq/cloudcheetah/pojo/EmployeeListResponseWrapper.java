package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Employees;

import java.util.List;

/**
 * Created by PC1 on 8/4/2016.
 */
public class EmployeeListResponseWrapper {

    Response response;
    List<Employees> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<Employees> getData() {
        return data;
    }

    public void setData(List<Employees> data) {
        this.data = data;
    }
}
