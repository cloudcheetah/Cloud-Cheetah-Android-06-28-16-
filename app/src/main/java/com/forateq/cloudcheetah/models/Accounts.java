package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC1 on 7/11/2016.
 */
@Table(name = "Accounts")
public class Accounts extends Model {

    @Column(name = "account_id")
    int id;
    @Column(name = "account_number")
    int account_number;
    @Column(name = "account_name")
    String account_name;
    @Column(name = "account_category_id")
    int account_category_id;
    @Column(name = "description")
    String description;
    @Column(name = "parent_id")
    int parent_id;
    @Column(name = "parent_account_name")
    String parent_account_name;


    public int getAccountId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount_number() {
        return account_number;
    }

    public void setAccount_number(int account_number) {
        this.account_number = account_number;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public int getAccount_category_id() {
        return account_category_id;
    }

    public void setAccount_category_id(int account_category_id) {
        this.account_category_id = account_category_id;
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

    public String getParent_account_name() {
        return parent_account_name;
    }

    public void setParent_account_name(String parent_account_name) {
        this.parent_account_name = parent_account_name;
    }

    public static List<Accounts> getAccounts(){
        return new Select().from(Accounts.class).execute();
    }

    public static List<String> getAccountsName(){
        List<Accounts> accountsList = Accounts.getAccounts();
        List<String> accountNames = new ArrayList<>();
        for(Accounts accounts : accountsList){

            accountNames.add(accounts.getAccount_name());

        }
        return  accountNames;
    }

    public static int getAccountId(String account_name){
        Accounts accounts = new Select().from(Accounts.class).where("account_name = ?", account_name).executeSingle();
        return accounts.getAccountId();
    }

    public static String getAccountName(int id){
        Accounts accounts = new Select().from(Accounts.class).where("account_id = ?", id).executeSingle();
        return  accounts.getAccount_name();
    }
}
