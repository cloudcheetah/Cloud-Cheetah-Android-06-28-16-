package com.forateq.cloudcheetah.modules;

import android.content.Context;

import com.forateq.cloudcheetah.CloudCheetahApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vallejos Family on 5/12/2016.
 */
@Module
public class AppModule {

    CloudCheetahApp cloudCheetahApp;

    public AppModule(CloudCheetahApp cloudCheetahApp){
        this.cloudCheetahApp = cloudCheetahApp;
    }

    @Provides
    @Singleton
    Context providesApplication(){
        return cloudCheetahApp;
    }


}
