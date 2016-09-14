package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Vallejos Family on 9/9/2016.
 */
@Table(name = "PurchaseRequests")
public class PurchaseRequests extends Model {

    @Column(name = "purchase_request_id")
    int id;
    @Column(name = "purchase_request_date")
    String date;
    @Column(name = "shipping_address")
    String shipping_address;
    @Column(name = "purchase_request_items")
    String purchase_request_items;
    @Column(name = "purchase_request_status")
    boolean purchase_request_status;

    public int getPurchaseRequestId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getPurchase_request_items() {
        return purchase_request_items;
    }

    public void setPurchase_request_items(String purchase_request_items) {
        this.purchase_request_items = purchase_request_items;
    }

    public boolean isPurchase_request_status() {
        return purchase_request_status;
    }

    public void setPurchase_request_status(boolean purchase_request_status) {
        this.purchase_request_status = purchase_request_status;
    }

    public static List<PurchaseRequests> getPurchaseRequest(){
        return new Select().from(PurchaseRequests.class).execute();
    }
}
