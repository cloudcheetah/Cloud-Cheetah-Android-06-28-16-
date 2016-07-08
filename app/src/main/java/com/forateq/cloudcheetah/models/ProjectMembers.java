package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This class is used to save the project members of a specific project in the mobile database
 *
 * Created by Vallejos Family on 5/25/2016.
 */
@Table(name = "ProjectJsonMembers")
public class ProjectMembers extends Model {

    @Column(name = "project_id")
    int project_id;
    @Column(name = "user_id")
    int user_id;
    @Column(name = "project_offline_id")
    long project_offline_id;
    @Column(name = "project_member_name")
    String project_member_name;

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getProject_offline_id() {
        return project_offline_id;
    }

    public void setProject_offline_id(long project_offline_id) {
        this.project_offline_id = project_offline_id;
    }

    public String getProject_member_name() {
        return project_member_name;
    }

    public void setProject_member_name(String project_member_name) {
        this.project_member_name = project_member_name;
    }

    /**
     * This method is used to get all the members of a specific project
     * @param project_id
     * @return List of Project Members
     */
    public static List<ProjectMembers> getProjectMembers(int project_id){
        return new Select().from(ProjectMembers.class).where("project_id = ?", project_id).execute();
    }

    /**
     * This method is used to get all the members of a specific project during offline mode
     * @param project_offline_id
     * @return List of Project Members
     */
    public static List<ProjectMembers> getProjectOfflineMembers(long project_offline_id){
        return new Select().from(ProjectMembers.class).where("project_offline_id = ?", project_offline_id).execute();
    }

    public static List<ProjectMembers> getSearchProjectMembersOffline(String name, long project_offline_id){
        String [] selectionArgs = new String[] {"%" + name + "%", ""+project_offline_id};
        List<ProjectMembers> searchItems =
                SQLiteUtils.rawQuery(ProjectMembers.class,
                        "SELECT * FROM ProjectJsonMembers WHERE project_member_name  LIKE ? AND project_offline_id = ?",
                        selectionArgs);
        return searchItems;
    }

    public static List<ProjectMembers> getSearchProjectMembersOnline(String name, int project_id){
        String [] selectionArgs = new String[] {"%" + name + "%", ""+project_id};
        List<ProjectMembers> searchItems =
                SQLiteUtils.rawQuery(ProjectMembers.class,
                        "SELECT * FROM ProjectJsonMembers WHERE project_member_name  LIKE ? AND project_id = ?",
                        selectionArgs);
        return searchItems;
    }

    /**
     * This method is used to get all the id of project members
     * @param project_id
     * @return
     */
    public static Map<String, Integer> getProjectMemberIdList(int project_id){
        Map<String, Integer> projectMembersList = new HashMap<>();
        List<ProjectMembers> projectMemberses = new Select().from(ProjectMembers.class).where("project_id = ?", project_id).execute();
        for(ProjectMembers projectMembers : projectMemberses){
            projectMembersList.put(""+projectMembers.getUser_id(), projectMembers.getUser_id());
        }
        return  projectMembersList;
    }

    /**
     * This method is used to get all the id of project members in offline mode
     * @param project_offline_id
     * @return
     */
    public static Map<String, Integer> getProjectMemberOfflineIdList(long project_offline_id){
        Map<String, Integer> projectMembersList = new HashMap<>();
        List<ProjectMembers> projectMemberses = new Select().from(ProjectMembers.class).where("project_offline_id = ?", project_offline_id).execute();
        for(ProjectMembers projectMembers : projectMemberses){
            projectMembersList.put(""+projectMembers.getUser_id(), projectMembers.getUser_id());
        }
        return  projectMembersList;
    }
}
