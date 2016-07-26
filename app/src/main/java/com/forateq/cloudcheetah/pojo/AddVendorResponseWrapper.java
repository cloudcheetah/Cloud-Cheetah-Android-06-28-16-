package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.Vendors;

/**
 * Created by PC1 on 7/26/2016.
 */
public class AddVendorResponseWrapper {

    Response response;
    Vendors data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Vendors getData() {
        return data;
    }

    public void setData(Vendors data) {
        this.data = data;
    }
}
