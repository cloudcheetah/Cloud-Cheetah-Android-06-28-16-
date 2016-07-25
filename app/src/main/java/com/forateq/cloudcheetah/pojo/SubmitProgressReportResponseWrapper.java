package com.forateq.cloudcheetah.pojo;

/**
 * Created by PC1 on 7/15/2016.
 */
public class SubmitProgressReportResponseWrapper {

    Response response;

    ProgressReportResponse data;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public ProgressReportResponse getData() {
        return data;
    }

    public void setData(ProgressReportResponse data) {
        this.data = data;
    }
}
