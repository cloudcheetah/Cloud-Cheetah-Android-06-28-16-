package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Messages;

import java.util.List;

/**
 * Created by PC1 on 8/2/2016.
 */
public class MessageResponseWrapper {

    Response response;
    Messages data;
    List<NotificationIds> sessions;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Messages getData() {
        return data;
    }

    public void setData(Messages data) {
        this.data = data;
    }

    public List<NotificationIds> getSessions() {
        return sessions;
    }

    public void setSessions(List<NotificationIds> sessions) {
        this.sessions = sessions;
    }
}
