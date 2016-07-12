package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Accounts;

import java.util.List;

/**
 * Created by PC1 on 7/11/2016.
 */
public class AccountListResponseWrapper {

    Response response;
    List<Accounts> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<Accounts> getData() {
        return data;
    }

    public void setData(List<Accounts> data) {
        this.data = data;
    }
}
