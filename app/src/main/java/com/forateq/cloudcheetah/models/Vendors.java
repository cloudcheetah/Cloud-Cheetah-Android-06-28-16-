package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC1 on 7/21/2016.
 */
@Table(name = "Vendors")
public class Vendors extends Model {

    @Column(name = "vendor_id")
    int id;
    @Column(name = "name")
    String name;
    @Column(name = "address")
    String address;
    @Column(name = "is_company")
    boolean is_company;
    @Column(name = "description")
    String description;
    @Column(name = "contact_person")
    String contact_person;
    @Column(name = "contact_no")
    String contact_no;
    @Column(name = "email_address")
    String email_address;
    @Column(name = "notes")
    String notes;


    public int getVendorId() {
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

    public boolean is_company() {
        return is_company;
    }

    public void setIs_company(boolean is_company) {
        this.is_company = is_company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static List<String> getAllVendorName(){
        List<Vendors> vendorsList = new Select().from(Vendors.class).execute();
        List<String> vendorNames = new ArrayList<>();
        for(Vendors vendors : vendorsList){
            vendorNames.add(vendors.getName());
        }
        return  vendorNames;
    }

    public static String getVendorName(int vendor_id){
        Vendors vendors = new Select().from(Vendors.class).where("vendor_id = ?", vendor_id).executeSingle();
        return vendors.getName();
    }

    public static int getVendorId(String name){
        Vendors vendors =  new Select().from(Vendors.class).where("name = ?", name).executeSingle();
        return vendors.getVendorId();
    }
}
