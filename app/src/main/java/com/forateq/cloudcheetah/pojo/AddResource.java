package com.forateq.cloudcheetah.pojo;

/**
 * Created by Vallejos Family on 6/10/2016.
 */
public class AddResource{
    String resourceName;
    int resourceQuantity;
    int id;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getResourceQuantity() {
        return resourceQuantity;
    }

    public void setResourceQuantity(int resourceQuantity) {
        this.resourceQuantity = resourceQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
