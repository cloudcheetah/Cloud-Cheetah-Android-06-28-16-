package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/** This class is used to store all the users from the web service to the mobile database
 * Created by Vallejos Family on 5/20/2016.
 */
@Table(name = "Users")
public class Users extends Model {

    @Column(name = "user_id")
    private int user_id;
    @Column(name = "user_name")
    private String user_name;
    @Column(name = "employee_id")
    private int employee_id;
    @Column(name = "is_admin")
    private boolean is_admin;
    @Column(name = "active")
    private boolean active;
    @Column(name = "full_name")
    private String full_name;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public boolean is_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * This method is used to get all the users
     * @return List of Users
     */
    public static List<Users> getUsers(){
        return new Select().from(Users.class).execute();
    }

    /**
     * This method is used to get a specific user using the user id
     * @param user_id
     * @return User
     */
    public static Users getUser(int user_id){
        return new Select().from(Users.class).where("user_id = ?", user_id).executeSingle();
    }

    /**
     * This method is used to get all the usernames of all users
     * @return List of Usernames
     */
    public static List<String> getUsersNames(){
        List<String> usersName = new ArrayList<>();
        List<Users> listUsers = new Select().from(Users.class).execute();
        for(Users users : listUsers){
            usersName.add(users.getFull_name());
        }
        return usersName;
    }

    /**
     * This method is used to get the user id of a specific user using the users full name
     * @param full_name
     * @return
     */
    public static int getUserId(String full_name){
        Users users =  new Select().from(Users.class).where("full_name = ?", full_name).executeSingle();
        return users.getUser_id();
    }

    /**
     * This method is used to get a specific user using its user id
     * @param user_id
     * @return User
     */
    public static int getUserByUserId(String user_id){
        Users users = new Select().from(Users.class).where("user_name = ?", user_id).executeSingle();
        return users.getUser_id();
    }
}
