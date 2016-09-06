package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 9/5/2016.
 */
public class NotificationsResponseWrapper {

    Response response;
    List<NotificationsWrapper> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<NotificationsWrapper> getData() {
        return data;
    }

    public void setData(List<NotificationsWrapper> data) {
        this.data = data;
    }
}
