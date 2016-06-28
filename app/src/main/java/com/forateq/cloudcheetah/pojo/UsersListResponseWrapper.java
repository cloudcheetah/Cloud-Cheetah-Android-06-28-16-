package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 5/20/2016.
 */
public class UsersListResponseWrapper {
    Response response;
    List<UserData> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<UserData> getData() {
        return data;
    }

    public void setData(List<UserData> data) {
        this.data = data;
    }
}
