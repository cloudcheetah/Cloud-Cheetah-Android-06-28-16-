package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.util.List;

/**
 * Created by PC1 on 8/4/2016.
 */
@Table(name = "Employees")
public class Employees extends Model {

    @Column(name = "employee_id")
    int id;
    @Column(name = "first_name")
    String first_name;
    @Column(name = "middle_name")
    String middle_name;
    @Column(name = "last_name")
    String last_name;
    @Column(name = "gender_id")
    int gender_id;
    @Column(name = "address")
    String address;
    @Column(name = "date_of_birth")
    String date_of_birth;
    @Column(name = "contact_no")
    String contact_no;
    @Column(name = "email_address")
    String email_address;
    @Column(name = "title")
    String title;
    @Column(name = "employment_type_id")
    int employment_type_id;
    @Column(name = "zip_code")
    String zip_code;
    @Column(name = "tin_no")
    String tin_no;
    @Column(name = "sss_no")
    String sss_no;
    @Column(name = "drivers_license_no")
    String drivers_license_no;
    @Column(name = "civil_status_id")
    int civil_status_id;
    @Column(name = "check_name")
    String check_name;
    @Column(name = "notes")
    String notes;
    @Column(name = "image")
    String image;


    public int getEmployeeId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getGender_id() {
        return gender_id;
    }

    public void setGender_id(int gender_id) {
        this.gender_id = gender_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEmployment_type_id() {
        return employment_type_id;
    }

    public void setEmployment_type_id(int employment_type_id) {
        this.employment_type_id = employment_type_id;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getTin_no() {
        return tin_no;
    }

    public void setTin_no(String tin_no) {
        this.tin_no = tin_no;
    }

    public String getSss_no() {
        return sss_no;
    }

    public void setSss_no(String sss_no) {
        this.sss_no = sss_no;
    }

    public String getDrivers_license_no() {
        return drivers_license_no;
    }

    public void setDrivers_license_no(String drivers_license_no) {
        this.drivers_license_no = drivers_license_no;
    }

    public int getCivil_status_id() {
        return civil_status_id;
    }

    public void setCivil_status_id(int civil_status_id) {
        this.civil_status_id = civil_status_id;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static List<Employees> getAllEmployees(){
        return new Select().from(Employees.class).execute();
    }

    public static List<Employees> searchEmployees(String name){
        String [] selectionArgs = new String[] {"%" + name + "%", "%" + name + "%", "%" + name + "%"};
        List<Employees> searchItems =
                SQLiteUtils.rawQuery(Employees.class,
                        "SELECT * FROM Employees WHERE first_name  LIKE ? OR last_name LIKE ? OR email_address LIKE ?",
                        selectionArgs);
        return searchItems;
    }

    public static Employees getEmployee(int employee_id){
        return new Select().from(Employees.class).where("employee_id = ?", employee_id).executeSingle();
    }
}
