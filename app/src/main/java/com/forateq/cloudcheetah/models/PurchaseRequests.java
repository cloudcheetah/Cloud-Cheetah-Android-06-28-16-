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
    @Column(name = "trans_date")
    String trans_date;
    @Column(name = "trans_no")
    String trans_no;
    @Column(name = "ref_no")
    String ref_no;
    @Column(name = "vendor_id")
    int vendor_id;
    @Column(name = "total_amount")
    double total_amount;
    @Column(name = "remarks")
    String remarks;
    @Column(name = "is_closed")
    boolean is_closed;
    @Column(name = "vendor_name")
    String vendor_name;
    @Column(name = "details")
    List<RequestItems> details;


    public int getPurchaseRequestId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrans_date() {
        return trans_date;
    }

    public void setTrans_date(String trans_date) {
        this.trans_date = trans_date;
    }

    public String getTrans_no() {
        return trans_no;
    }

    public void setTrans_no(String trans_no) {
        this.trans_no = trans_no;
    }

    public String getRef_no() {
        return ref_no;
    }

    public void setRef_no(String ref_no) {
        this.ref_no = ref_no;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean is_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public List<RequestItems> getDetails() {
        return details;
    }

    public void setDetails(List<RequestItems> details) {
        this.details = details;
    }

    public static List<PurchaseRequests> getPurchaseRequests(){
        return new Select().from(PurchaseRequests.class).execute();
    }

    public static PurchaseRequests getPurchaseRequestById(int id){
        return new Select().from(PurchaseRequests.class).where("purchase_request_id = ?", id).executeSingle();
    }
}
