package com.forateq.cloudcheetah.pojo;

/**
 * Created by Vallejos Family on 5/25/2016.
 */
public class ResourceData {

    int id;
    String name;
    String description;
    int parent_id;
    int account_id;
    int type_id;
    int unit_id;
    double unit_cost;
    double sales_price;
    int reorder_point;
    int vendor_id;
    String notes;
    int on_hand_qty;
    int reserved_qty;
    int in_transit_qty;
    String image;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }

    public double getUnit_cost() {
        return unit_cost;
    }

    public void setUnit_cost(double unit_cost) {
        this.unit_cost = unit_cost;
    }

    public double getSales_price() {
        return sales_price;
    }

    public void setSales_price(double sales_price) {
        this.sales_price = sales_price;
    }

    public int getReorder_point() {
        return reorder_point;
    }

    public void setReorder_point(int reorder_point) {
        this.reorder_point = reorder_point;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getOn_hand_qty() {
        return on_hand_qty;
    }

    public void setOn_hand_qty(int on_hand_qty) {
        this.on_hand_qty = on_hand_qty;
    }

    public int getReserved_qty() {
        return reserved_qty;
    }

    public void setReserved_qty(int reserved_qty) {
        this.reserved_qty = reserved_qty;
    }

    public int getIn_transit_qty() {
        return in_transit_qty;
    }

    public void setIn_transit_qty(int in_transit_qty) {
        this.in_transit_qty = in_transit_qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
