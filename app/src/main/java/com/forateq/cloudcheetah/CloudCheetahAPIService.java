package com.forateq.cloudcheetah;

import com.forateq.cloudcheetah.pojo.LoginWrapper;
import com.forateq.cloudcheetah.pojo.ResourceListResponseWrapper;
import com.forateq.cloudcheetah.pojo.UsersListResponseWrapper;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/** This interface is used as a java component of web service
 * Created by Vallejos Family on 5/12/2016.
 */
public interface CloudCheetahAPIService {

    String SERVER_TOKEN = "!JJJJcheetah8888";


    @GET("/api/apilogin/")
    Observable<LoginWrapper> login(@Query("user") String user, @Query("password") String password, @Query("deviceid") String deviceId, @Query("token") String token, @Query("notif") String notification_id);

    @GET("/api_users/")
    Observable<UsersListResponseWrapper> getAllUsers(@Query("userid") String user, @Query("deviceid") String deviceid, @Query("key") String key);

    @GET("api_resources/")
    Observable<ResourceListResponseWrapper> getAllResources(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key);

}
