package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Vallejos Family on 9/14/2016.
 */
@Table(name = "RequestItems")
public class RequestItems extends Model {

    @Column(name = "request_item_id")
    int id;
    @Column(name = "master_id")
    int master_id;
    @Column(name = "item_id")
    int item_id;
    @Column(name = "qty")
    int qty;
    @Column(name = "cost_price")
    double cost_price;
    @Column(name = "net_amount")
    double net_amount;
    @Column(name = "remarks")
    String remarks;
    @Column(name = "item_name")
    String item_name;

    public int getRequestItemsId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaster_id() {
        return master_id;
    }

    public void setMaster_id(int master_id) {
        this.master_id = master_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getCost_price() {
        return cost_price;
    }

    public void setCost_price(double cost_price) {
        this.cost_price = cost_price;
    }

    public double getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(double net_amount) {
        this.net_amount = net_amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public static int getCount(){
        List<RequestItems> requestItems =  new Select().from(RequestItems.class).execute();
        return requestItems.size();
    }
}
