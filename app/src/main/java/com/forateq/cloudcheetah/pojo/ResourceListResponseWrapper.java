package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 5/25/2016.
 */
public class ResourceListResponseWrapper {

    Response response;

    List<ResourceData> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<ResourceData> getData() {
        return data;
    }

    public void setData(List<ResourceData> data) {
        this.data = data;
    }
}
