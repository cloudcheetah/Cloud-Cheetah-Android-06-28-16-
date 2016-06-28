package com.forateq.cloudcheetah.pojo;

import java.util.List;

/**
 * Created by Vallejos Family on 5/18/2016.
 */
public class ProjectMembers {

    int project_id;
    List<String> project_members;

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public List<String> getProject_members() {
        return project_members;
    }

    public void setProject_members(List<String> project_members) {
        this.project_members = project_members;
    }
}
