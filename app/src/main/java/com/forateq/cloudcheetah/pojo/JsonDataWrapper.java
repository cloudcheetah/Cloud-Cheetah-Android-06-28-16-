package com.forateq.cloudcheetah.pojo;

/**
 * Created by PC1 on 8/16/2016.
 */
public class JsonDataWrapper {

    JsonData json;

    int notification_type;

    public JsonData getJson() {
        return json;
    }

    public void setJson(JsonData json) {
        this.json = json;
    }

    public int getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(int notification_type) {
        this.notification_type = notification_type;
    }
}
