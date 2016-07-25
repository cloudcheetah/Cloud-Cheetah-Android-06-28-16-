package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.util.List;

/** This class is used to store all the resources of a specific project in the mobile database
 * Created by Vallejos Family on 5/25/2016.
 */
@Table(name = "ProjectResources")
public class ProjectResources extends Model {

    @Column(name = "project_id")
    int project_id;
    @Column(name = "project_offline_id")
    long project_offline_id;
    @Column(name = "resource_id")
    int resource_id;
    @Column(name = "quantity")
    int quantity;
    @Column(name = "resource_name")
    String resource_name;
    @Column(name = "project_resource_id")
    int project_resource_id;

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getProject_offline_id() {
        return project_offline_id;
    }

    public void setProject_offline_id(long project_offline_id) {
        this.project_offline_id = project_offline_id;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public int getProject_resource_id() {
        return project_resource_id;
    }

    public void setProject_resource_id(int project_resource_id) {
        this.project_resource_id = project_resource_id;
    }

    /**
     * This method is used to get all the resources of a specific project
     * @param project_id
     * @return List of Project Resources
     */
    public static List<ProjectResources> getResources(int project_id){
        return new Select().from(ProjectResources.class).where("project_id = ?", project_id).execute();
    }

    /**
     * This method is used to get all the resources of a specific project during offline mode
     * @param project_offline_id
     * @return List of Project Resources
     */

    public static List<ProjectResources> getResourcesOffline(long project_offline_id){
        return new Select().from(ProjectResources.class).where("project_offline_id = ?", project_offline_id).execute();
    }

    public static List<ProjectResources> getSearchProjectResourcesOffline(String name, long project_offline_id){
        String [] selectionArgs = new String[] {"%" + name + "%", ""+project_offline_id};
        List<ProjectResources> searchItems =
                SQLiteUtils.rawQuery(ProjectResources.class,
                        "SELECT * FROM ProjectResources WHERE resource_name  LIKE ? AND project_offline_id = ?",
                        selectionArgs);
        return searchItems;
    }

    public static List<ProjectResources> getSearchProjectResourcesOnline(String name, int project_id){
        String [] selectionArgs = new String[] {"%" + name + "%", ""+project_id};
        List<ProjectResources> searchItems =
                SQLiteUtils.rawQuery(ProjectResources.class,
                        "SELECT * FROM ProjectResources WHERE resource_name  LIKE ? AND project_id = ?",
                        selectionArgs);
        return searchItems;
    }

    public static ProjectResources getProjectResource(int project_resource_id){
        return new Select().from(ProjectResources.class).where("project_resource_id = ?", project_resource_id).executeSingle();
    }


}
