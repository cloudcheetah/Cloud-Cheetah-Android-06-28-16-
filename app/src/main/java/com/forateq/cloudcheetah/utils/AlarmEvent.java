package com.forateq.cloudcheetah.utils;

/**
 * Created by PC1 on 8/19/2016.
 */
public class AlarmEvent{

    private boolean isAlarm;

    public AlarmEvent(boolean isAlarm) {
        this.isAlarm = isAlarm;
    }

    public boolean isAlarm() {
        return this.isAlarm;
    }
}
