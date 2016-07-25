package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by PC1 on 7/11/2016.
 */
@Table(name = "CashInOut")
public class CashInOut extends Model {

    @Column(name = "cash_in_out_id")
    int id;

    @Column(name = "type_id")
    int type_id;

    @Column(name = "task_id")
    int task_id;

    @Column(name = "task_offline_id")
    long task_offline_id;

    @Column(name = "account_id")
    int account_id;

    @Column(name = "payer_id")
    int payer_id;

    @Column(name = "invoice_no")
    int invoice_no;

    @Column(name = "receipt_no")
    int receipt_no;

    @Column(name = "item_id")
    int item_id;

    @Column(name = "qty")
    int qty;

    @Column(name = "amount")
    double amount;

    @Column(name = "transaction_date")
    String transaction_date;

    @Column(name = "location")
    String location;

    @Column(name = "description")
    String description;

    @Column(name = "attachment_1")
    String attachment_1;

    @Column(name = "attachment_2")
    String attachment_2;

    @Column(name = "attachment_3")
    String attachment_3;

    @Column(name = "is_submitted")
    boolean is_submitted;


    public int getCashInOutId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public long getTask_offline_id() {
        return task_offline_id;
    }

    public void setTask_offline_id(long task_offline_id) {
        this.task_offline_id = task_offline_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(int payer_id) {
        this.payer_id = payer_id;
    }

    public int getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(int invoice_no) {
        this.invoice_no = invoice_no;
    }

    public int getReceipt_no() {
        return receipt_no;
    }

    public void setReceipt_no(int receipt_no) {
        this.receipt_no = receipt_no;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment_1() {
        return attachment_1;
    }

    public void setAttachment_1(String attachment_1) {
        this.attachment_1 = attachment_1;
    }

    public String getAttachment_2() {
        return attachment_2;
    }

    public void setAttachment_2(String attachment_2) {
        this.attachment_2 = attachment_2;
    }

    public String getAttachment_3() {
        return attachment_3;
    }

    public void setAttachment_3(String attachment_3) {
        this.attachment_3 = attachment_3;
    }

    public boolean is_submitted() {
        return is_submitted;
    }

    public void setIs_submitted(boolean is_submitted) {
        this.is_submitted = is_submitted;
    }

    public static CashInOut getCashInOut(long cash_flow_offline_id){
        return new Select().from(CashInOut.class).where("id = ?", cash_flow_offline_id).executeSingle();
    }

    public static List<CashInOut> getCashInOuts(long task_offline_id){
        return new Select().from(CashInOut.class).where("task_offline_id = ?", task_offline_id).execute();
    }

}
