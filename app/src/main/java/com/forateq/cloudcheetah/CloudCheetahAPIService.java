package com.forateq.cloudcheetah;

import com.forateq.cloudcheetah.pojo.LoginWrapper;
import com.forateq.cloudcheetah.pojo.ProjectListResponseWrapper;
import com.forateq.cloudcheetah.pojo.ProjectResponseWrapper;
import com.forateq.cloudcheetah.pojo.ResourceListResponseWrapper;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.pojo.TaskListResponseWrapper;
import com.forateq.cloudcheetah.pojo.TaskResponseWrapper;
import com.forateq.cloudcheetah.pojo.UsersListResponseWrapper;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
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

    @GET("api_projects")
    Observable<ProjectListResponseWrapper> getAllProjects(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key, @Query("timestamp") String timestamp);

    @FormUrlEncoded
    @POST("/api_projects/{id}")
    Observable<ProjectResponseWrapper> updateProject(@Path("id") int projectId, @Field("project[name]") String projectName, @Field("project[start_date]") String projectStart, @Field("project[end_date]") String projectEnd, @Field("project[budget]") double budget, @Field("project[description]") String projectDescription, @Field("project[objectives]") String projectObjective, @Field("project[latitude]") String latitude, @Field("project[longitude]") String longitude, @Field("project[project_manager_id]") int project_manager_id, @Field("project[project_sponsor_id]") int project_sponsor_id,  @Field("user") String user, @Field("deviceid") String deviceid, @Field("key") String key, @Field("_method") String method);

    @FormUrlEncoded
    @POST("/api_tasks/create")
    Observable<TaskResponseWrapper> createTask(@Field("task[name]") String taskName, @Field("task[start_date]") String startDate, @Field("task[end_date]") String endDate, @Field("task[budget]") String budget, @Field("task[description]") String taskDescription, @Field("task[latitude]") String latitude, @Field("task[longitude]") String longitude, @Field("task[duration]") String duration, @Field("task[person_responsible_id]") int personResponsibleId, @Field("task[project_id]") int project_id, @Field("task[parent_id]") int parent_id, @Field("user") String user, @Field("deviceid") String deviceid, @Field("key") String key);

    @GET("api_tasks")
    Observable<TaskListResponseWrapper> getAllTasks(@Query("project_id") int project_id, @Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key, @Query("timestamp") String timestamp);

    @GET("api/create_from_offline")
    Observable<ResponseWrapper> processOfflineProject(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key, @Query("json") String json);

    @GET("api/update_from_offline")
    Observable<ResponseWrapper> updateOfflineProject(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key, @Query("json") String json);

    @GET("api/assign_project_members")
    Observable<ResponseWrapper> addProjectMember(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key, @Query("json") String json);

    @GET("api/assign_project_resources")
    Observable<ResponseWrapper> addProjectResource(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key, @Query("json") String json);

}
