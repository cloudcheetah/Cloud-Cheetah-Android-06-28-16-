package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Accounts;

/**
 * Created by PC1 on 7/25/2016.
 */
public class AddAccountWrapper {

    Response response;
    Accounts data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Accounts getData() {
        return data;
    }

    public void setData(Accounts data) {
        this.data = data;
    }
}
