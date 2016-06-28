package com.forateq.cloudcheetah.pojo;

/**
 * Created by Vallejos Family on 5/20/2016.
 */
public class Response {

    int id;
    int status_code;
    String response_text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getResponse_text() {
        return response_text;
    }

    public void setResponse_text(String response_text) {
        this.response_text = response_text;
    }
}
