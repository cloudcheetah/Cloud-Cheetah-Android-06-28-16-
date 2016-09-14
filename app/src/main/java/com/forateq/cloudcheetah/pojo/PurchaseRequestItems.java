package com.forateq.cloudcheetah.pojo;

/**
 * Created by Vallejos Family on 9/14/2016.
 */
public class PurchaseRequestItems {

    int resource_id;
    String resource_name;
    int resource_quantity;
    double resource_price;
    double resource_total_price;

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public int getResource_quantity() {
        return resource_quantity;
    }

    public void setResource_quantity(int resource_quantity) {
        this.resource_quantity = resource_quantity;
    }

    public double getResource_price() {
        return resource_price;
    }

    public void setResource_price(double resource_price) {
        this.resource_price = resource_price;
    }

    public double getResource_total_price() {
        return resource_total_price;
    }

    public void setResource_total_price(double resource_total_price) {
        this.resource_total_price = resource_total_price;
    }
}
