package com.forateq.cloudcheetah.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC1 on 7/21/2016.
 */
@Table(name = "Units")
public class Units extends Model {

    @Column(name = "unit_id")
    int id;
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "notes")
    String notes;


    public int getUnitId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static List<String> getAllUnits(){
        List<Units> units =  new Select().from(Units.class).execute();
        List<String> unitNames = new ArrayList<>();
        for(Units unit : units){
            unitNames.add(unit.getName());
        }
        return unitNames;
    }

    public static int getUnitId(String name){
        Units units = new Select().from(Units.class).where("name = ?", name).executeSingle();
        return units.getUnitId();
    }

    public static String getUnitName(int id){
        Units units = new Select().from(Units.class).where("unit_id = ?", id).executeSingle();
        return units.getName();
    }
}
