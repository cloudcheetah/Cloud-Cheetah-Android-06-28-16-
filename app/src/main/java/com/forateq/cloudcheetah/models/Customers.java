package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by PC1 on 7/11/2016.
 */
@Table(name = "Customers")
public class Customers extends Model{

    @Column(name = "customer_id")
    int id;
    @Column(name = "name")
    String name;
    @Column(name = "address")
    String address;
    @Column(name = "notes")
    String notes;


    public int getCustomerId() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static List<Customers> getCustomers(){
        return new Select().from(Customers.class).execute();
    }
}
