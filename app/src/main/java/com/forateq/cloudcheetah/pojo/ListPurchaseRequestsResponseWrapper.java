package com.forateq.cloudcheetah.pojo;

import com.forateq.cloudcheetah.models.PurchaseRequests;

import java.util.List;

/**
 * Created by Vallejos Family on 9/16/2016.
 */
public class ListPurchaseRequestsResponseWrapper {

    Response response;
    List<PurchaseRequests> data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public List<PurchaseRequests> getData() {
        return data;
    }

    public void setData(List<PurchaseRequests> data) {
        this.data = data;
    }
}
