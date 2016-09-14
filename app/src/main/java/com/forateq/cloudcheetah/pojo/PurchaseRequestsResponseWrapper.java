package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.PurchaseRequests;

/**
 * Created by Vallejos Family on 9/14/2016.
 */
public class PurchaseRequestsResponseWrapper {
    Response response;
    PurchaseRequests data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public PurchaseRequests getData() {
        return data;
    }

    public void setData(PurchaseRequests data) {
        this.data = data;
    }
}
