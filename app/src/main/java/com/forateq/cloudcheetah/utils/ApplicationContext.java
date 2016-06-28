package com.forateq.cloudcheetah.utils;

import android.content.Context;

/** This class is used to provide application context to all components of class of the application
 * Created by Vallejos Family on 5/13/2016.
 */
public class ApplicationContext {

    private Context appContext;

    private ApplicationContext(){}

    public void init(Context context){
        if(appContext == null){
            appContext = context;
        }
    }

    private Context getContext(){
        return appContext;
    }

    public static Context get(){
        return getInstance().getContext();
    }

    private static ApplicationContext instance;

    public static ApplicationContext getInstance(){
        return instance == null ?
                (instance = new ApplicationContext()):
                instance;
    }
}
