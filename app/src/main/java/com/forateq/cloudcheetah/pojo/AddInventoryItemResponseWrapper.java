package com.forateq.cloudcheetah.pojo;

/**
 * Created by PC1 on 7/22/2016.
 */
public class AddInventoryItemResponseWrapper {

    Response response;
    InventoryItemResponse data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public InventoryItemResponse getData() {
        return data;
    }

    public void setData(InventoryItemResponse data) {
        this.data = data;
    }
}
