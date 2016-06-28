package com.forateq.cloudcheetah.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.forateq.cloudcheetah.CloudCheetahAPIService;
import com.forateq.cloudcheetah.CloudCheetahApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by Vallejos Family on 5/12/2016.
 */
@Module
public class NetworkModule {

    String mBaseUrl;

    public NetworkModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    CloudCheetahAPIService provideCloudCheetahAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        CloudCheetahAPIService cloudCheetahAPIService = retrofit.create(CloudCheetahAPIService.class);
        return cloudCheetahAPIService;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(CloudCheetahApp application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

}
