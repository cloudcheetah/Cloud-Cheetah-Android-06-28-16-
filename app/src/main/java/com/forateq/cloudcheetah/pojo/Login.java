package com.forateq.cloudcheetah.pojo;

/**
 * Created by Vallejos Family on 5/12/2016.
 */
public class Login {

    private String id;
    private boolean login_success;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLogin_success() {
        return login_success;
    }

    public void setLogin_success(boolean login_success) {
        this.login_success = login_success;
    }
}
