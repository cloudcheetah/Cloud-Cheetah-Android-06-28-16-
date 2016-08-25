package com.forateq.cloudcheetah.pojo;

/**
 * Created by PC1 on 8/2/2016.
 */
public class PostNotification {

    Contents contents;
    String [] include_player_ids;
    String data;

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    public String[] getInclude_player_ids() {
        return include_player_ids;
    }

    public void setInclude_player_ids(String[] include_player_ids) {
        this.include_player_ids = include_player_ids;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
