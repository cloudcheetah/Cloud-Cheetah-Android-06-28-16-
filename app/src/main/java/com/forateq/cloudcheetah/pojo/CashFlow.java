package com.forateq.cloudcheetah.pojo;

/**
 * Created by PC1 on 7/15/2016.
 */
public class CashFlow {

    String transaction_date;
    String location;
    String description;
    long task_offline_id;
    double amount;
    int item_id;
    int invoice_no;
    int payer_id;
    int qty;
    int receipt_no;
    int task_id;
    int account_id;
    int type_id;

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

    public long getTask_offline_id() {
        return task_offline_id;
    }

    public void setTask_offline_id(long task_offline_id) {
        this.task_offline_id = task_offline_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(int invoice_no) {
        this.invoice_no = invoice_no;
    }

    public int getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(int payer_id) {
        this.payer_id = payer_id;
    }

    public int getReceipt_no() {
        return receipt_no;
    }

    public void setReceipt_no(int receipt_no) {
        this.receipt_no = receipt_no;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
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
}
