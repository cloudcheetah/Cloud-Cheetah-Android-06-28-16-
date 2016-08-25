package com.forateq.cloudcheetah;

import com.forateq.cloudcheetah.fragments.AddInventoryItemFragment;
import com.forateq.cloudcheetah.pojo.AccountListResponseWrapper;
import com.forateq.cloudcheetah.pojo.AddAccountWrapper;
import com.forateq.cloudcheetah.pojo.AddCustomerWrapper;
import com.forateq.cloudcheetah.pojo.AddInventoryItemResponseWrapper;
import com.forateq.cloudcheetah.pojo.AddVendorResponseWrapper;
import com.forateq.cloudcheetah.pojo.ConversationResponseWrapper;
import com.forateq.cloudcheetah.pojo.CustomerListResponseWrapper;
import com.forateq.cloudcheetah.pojo.EmployeeListResponseWrapper;
import com.forateq.cloudcheetah.pojo.EmployeeResponseWrapper;
import com.forateq.cloudcheetah.pojo.LoginWrapper;
import com.forateq.cloudcheetah.pojo.MessageListResponseWrapper;
import com.forateq.cloudcheetah.pojo.MessageResponseWrapper;
import com.forateq.cloudcheetah.pojo.MyTasksResponseWrapper;
import com.forateq.cloudcheetah.pojo.ProjectListResponseWrapper;
import com.forateq.cloudcheetah.pojo.ProjectResponseWrapper;
import com.forateq.cloudcheetah.pojo.ResourceListResponseWrapper;
import com.forateq.cloudcheetah.pojo.Response;
import com.forateq.cloudcheetah.pojo.ResponseWrapper;
import com.forateq.cloudcheetah.pojo.SingleTaskResponseWrapper;
import com.forateq.cloudcheetah.pojo.SubmitProgressReportResponseWrapper;
import com.forateq.cloudcheetah.pojo.TaskListResponseWrapper;
import com.forateq.cloudcheetah.pojo.TaskResponseWrapper;
import com.forateq.cloudcheetah.pojo.UnitsResponseWrapper;
import com.forateq.cloudcheetah.pojo.UsersListResponseWrapper;
import com.forateq.cloudcheetah.pojo.VendorsResponseWrapper;
import com.squareup.okhttp.RequestBody;


import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
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

    @GET("/api/apilogout/")
    Observable<ResponseWrapper> logout(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key);

    @GET("/api_users/")
    Observable<UsersListResponseWrapper> getAllUsers(@Query("userid") String user, @Query("deviceid") String deviceid, @Query("key") String key);

    @GET("api_resources/")
    Observable<ResourceListResponseWrapper> getAllResources(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key);

    @GET("api_accounts/")
    Observable<AccountListResponseWrapper> getAllAccounts(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key);

    @GET("api_vendors/")
    Observable<VendorsResponseWrapper> getAllVendors(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key);

    @GET("api_customers/")
    Observable<CustomerListResponseWrapper> getAllCustomers(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key);

    @GET("api_units/")
    Observable<UnitsResponseWrapper> getAllUnits(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key);

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

    @POST("api/report_task_updates")
    Observable<SubmitProgressReportResponseWrapper> addProgressReport(@Query("user") String user,
                                                                      @Query("deviceid") String deviceid,
                                                                      @Query("key") String key,
                                                                      @Query("task_id") String task_id,
                                                                      @Query("report_date") String report_date,
                                                                      @Query("percentage_completion") String percentage_completion,
                                                                      @Query("task_status") String task_status,
                                                                      @Query("hours_worked") String hours_worked,
                                                                      @Query("resources_used") String resources_used,
                                                                      @Query("action") String action,
                                                                      @Query("notes") String notes,
                                                                      @Query("concerns") String concerns,
                                                                      @Query("requests") String requests,
                                                                      @Body RequestBody requestBody);



    @POST("api/add_cash_flow")
    Observable<SubmitProgressReportResponseWrapper> addCashFlow(@Query("user") String user,
                                                                @Query("deviceid") String deviceid,
                                                                @Query("key") String key,
                                                                @Query("json") String json,
                                                                @Body RequestBody requestBody);

    @GET("api/assign_task_resources")
    Observable<ResponseWrapper> addTaskResource(@Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key, @Query("json") String json);

    @GET("api_tasks/{id}")
    Observable<SingleTaskResponseWrapper> getSubTasks(@Path("id") int task_id, @Query("user") String user, @Query("deviceid") String deviceid, @Query("key") String key);

    @POST("api_resources/create")
    Observable<AddInventoryItemResponseWrapper> addInventoryItem(@Query("resource[name]") String resourceName,
                                                                 @Query("resource[flag_id]") int flag_id,
                                                                 @Query("resource[description]") String description,
                                                                 @Query("resource[parent_id]") int parent_id,
                                                                 @Query("resource[account_id]") int account_id,
                                                                 @Query("resource[type_id]") int type_id,
                                                                 @Query("resource[unit_id]") int unit_id,
                                                                 @Query("resource[unit_cost]") double unit_cost,
                                                                 @Query("resource[sales_price]") double sales_price,
                                                                 @Query("resource[reorder_point]") int reorder_point,
                                                                 @Query("resource[vendor_id]") int vendor_id,
                                                                 @Query("resource[notes]") String resource_note,
                                                                 @Body RequestBody requestBody,
                                                                 @Query("user") String user,
                                                                 @Query("deviceid") String deviceid,
                                                                 @Query("key") String key);

    @PUT("api_resources/{id}")
    Observable<AddInventoryItemResponseWrapper> updateInventoryItem(@Path("id") int resource_id, @Query("resource[name]") String resourceName,
                                                                 @Query("resource[flag_id]") int flag_id,
                                                                 @Query("resource[description]") String description,
                                                                 @Query("resource[parent_id]") int parent_id,
                                                                 @Query("resource[account_id]") int account_id,
                                                                 @Query("resource[type_id]") int type_id,
                                                                 @Query("resource[unit_id]") int unit_id,
                                                                 @Query("resource[unit_cost]") double unit_cost,
                                                                 @Query("resource[sales_price]") double sales_price,
                                                                 @Query("resource[reorder_point]") int reorder_point,
                                                                 @Query("resource[vendor_id]") int vendor_id,
                                                                 @Query("resource[notes]") String resource_note,
                                                                 @Body RequestBody requestBody,
                                                                 @Query("user") String user,
                                                                 @Query("deviceid") String deviceid,
                                                                 @Query("key") String key,
                                                                 @Query("_method") String method);

    @DELETE("api_resources/{id}")
    Observable<ResponseWrapper> deleteInventoryItem(@Path("id") int resource_id,
                                                    @Query("user") String user,
                                                    @Query("deviceid") String deviceid,
                                                    @Query("key") String key,
                                                    @Query("_method") String method);


    @GET("api/get_my_tasks")
    Observable<MyTasksResponseWrapper> getMyTasks(@Query("user") String user,
                                                  @Query("deviceid") String deviceid,
                                                  @Query("key") String key,
                                                  @Query("timestamp") String timestamp);


    @POST("api_accounts/create")
    Observable<AddAccountWrapper> addAccount(@Query("account[account_name]") String account_name,
                                             @Query("account[account_number]") String account_number,
                                             @Query("account[description]") String account_description,
                                             @Query("account[parent_id]") int parent_id,
                                             @Query("account[account_category_id]") int account_category,
                                             @Query("user") String user,
                                             @Query("deviceid") String deviceid,
                                             @Query("key") String key);

    @PUT("api_accounts/{id}")
    Observable<AddAccountWrapper> updateAccount(@Path("id") int account_id,
                                                @Query("account[account_name]") String account_name,
                                                @Query("account[account_number]") String account_number,
                                                @Query("account[description]") String account_description,
                                                @Query("account[parent_id]") int parent_id,
                                                @Query("account[account_category_id]") int account_category,
                                                @Query("user") String user,
                                                @Query("deviceid") String deviceid,
                                                @Query("key") String key,
                                                @Query("_method") String method);

    @DELETE("api_accounts/{id}")
    Observable<ResponseWrapper> deleteAccount(@Path("id") int account_id,
                                              @Query("user") String user,
                                              @Query("deviceid") String deviceid,
                                              @Query("key") String key,
                                              @Query("_method") String method);

    @POST("api_customers/create")
    Observable<AddCustomerWrapper> addCustomer(@Query("customer[name]") String customer_name,
                                               @Query("customer[address]") String customer_address,
                                               @Query("customer[notes]") String customer_notes,
                                               @Query("user") String user,
                                               @Query("deviceid") String deviceid,
                                               @Query("key") String key);

    @PUT("api_customers/{id}")
    Observable<AddCustomerWrapper> updateCustomer(@Path("id") int customer_id,
                                                  @Query("customer[name]") String customer_name,
                                                  @Query("customer[address]") String customer_address,
                                                  @Query("customer[notes]") String customer_notes,
                                                  @Query("user") String user,
                                                  @Query("deviceid") String deviceid,
                                                  @Query("key") String key,
                                                  @Query("_method") String method);

    @DELETE("api_customers/{id}")
    Observable<ResponseWrapper> deleteCustomer(@Path("id") int account_id,
                                               @Query("user") String user,
                                               @Query("deviceid") String deviceid,
                                               @Query("key") String key,
                                               @Query("_method") String method);

    @POST("api_vendors/create")
    Observable<AddVendorResponseWrapper> addVendor(@Query("vendor[name]") String vendor_name,
                                                   @Query("vendor[address]") String vendor_address,
                                                   @Query("vendor[is_company]") int is_company,
                                                   @Query("vendor[description]") String description,
                                                   @Query("vendor[contact_no]") String contact_number,
                                                   @Query("vendor[contact_person]") String contact_person,
                                                   @Query("vendor[email_address]") String email,
                                                   @Query("vendor[notes]") String notes,
                                                   @Query("user") String user,
                                                   @Query("deviceid") String deviceid,
                                                   @Query("key") String key);

    @PUT("api_vendors/{id}")
    Observable<AddVendorResponseWrapper> updateVendor(@Path("id") int vendor_id,
                                                      @Query("vendor[name]") String vendor_name,
                                                      @Query("vendor[address]") String vendor_address,
                                                      @Query("vendor[is_company]") int is_company,
                                                      @Query("vendor[description]") String description,
                                                      @Query("vendor[contact_no]") String contact_number,
                                                      @Query("vendor[contact_person]") String contact_person,
                                                      @Query("vendor[email_address]") String email,
                                                      @Query("vendor[notes]") String notes,
                                                      @Query("user") String user,
                                                      @Query("deviceid") String deviceid,
                                                      @Query("key") String key,
                                                      @Query("_method") String method);

    @DELETE("api_vendors/{id}")
    Observable<ResponseWrapper> deleteVendor(@Path("id") int vendor_id,
                                             @Query("user") String user,
                                             @Query("deviceid") String deviceid,
                                             @Query("key") String key,
                                             @Query("_method") String method);

    @GET("api/send_chat")
    Observable<MessageResponseWrapper> sendMessage(@Query("user") String user,
                                                   @Query("deviceid") String deviceid,
                                                   @Query("key") String key,
                                                   @Query("receiverid") int receiver_id,
                                                   @Query("message") String message);

    @GET("api/send_project_chat")
    Observable<MessageResponseWrapper> sendProjectMessage(@Query("user") String user,
                                                   @Query("deviceid") String deviceid,
                                                   @Query("key") String key,
                                                   @Query("projectid") int project_id,
                                                   @Query("message") String message);

    @GET("api/get_project_chats")
    Observable<MessageListResponseWrapper> getProjectMessages(@Query("user") String user,
                                                              @Query("deviceid") String deviceid,
                                                              @Query("key") String key,
                                                              @Query("projectid") int project_id);

    @GET("api/get_chats")
    Observable<MessageListResponseWrapper> getMessages(@Query("user") String user,
                                                       @Query("deviceid") String deviceid,
                                                       @Query("key") String key,
                                                       @Query("p1") int sender_id,
                                                       @Query("p2") int receiver_id);

    @GET("api/get_chat_lists")
    Observable<ConversationResponseWrapper> getConversations(@Query("user") String user,
                                                             @Query("deviceid") String deviceid,
                                                             @Query("key") String key);

    @GET("/api_employees")
    Observable<EmployeeListResponseWrapper> getEmployees(@Query("user") String user,
                                                         @Query("deviceid") String deviceid,
                                                         @Query("key") String key);

    @POST("api_employees/create")
    Observable<EmployeeResponseWrapper> createEmployee(@Query("employee[first_name]") String first_name,
                                                       @Query("employee[middle_name]") String middle_name,
                                                       @Query("employee[last_name]") String last_name,
                                                       @Query("employee[gender_id]") int gender_id,
                                                       @Query("employee[date_of_birth]") String date_of_birth,
                                                       @Query("employee[address]") String address,
                                                       @Query("employee[email_address]") String email,
                                                       @Query("employee[contact_no]") String contact_no,
                                                       @Query("employee[status_id]") int status_id,
                                                       @Query("employee[title]") String title,
                                                       @Query("employee[employment_type_id]") int employment_type_id,
                                                       @Query("employee[zip_code]") String zip_code,
                                                       @Query("employee[tin_no]") String tin_no,
                                                       @Query("employee[sss_no]") String sss_no,
                                                       @Query("employee[drivers_license_no]") String drivers_license_no,
                                                       @Query("employee[civil_status_id]") int civil_status_id,
                                                       @Query("employee[notes]") String employee_notes,
                                                       @Body RequestBody requestBody,
                                                       @Query("user") String user,
                                                       @Query("deviceid") String deviceid,
                                                       @Query("key") String key);


    @PUT("api_employees/{id}")
    Observable<EmployeeResponseWrapper> updateEmployee(@Path("id") int employee_id,
                                                       @Query("employee[first_name]") String first_name,
                                                       @Query("employee[middle_name]") String middle_name,
                                                       @Query("employee[last_name]") String last_name,
                                                       @Query("employee[gender_id]") int gender_id,
                                                       @Query("employee[date_of_birth]") String date_of_birth,
                                                       @Query("employee[address]") String address,
                                                       @Query("employee[email_address]") String email,
                                                       @Query("employee[contact_no]") String contact_no,
                                                       @Query("employee[status_id]") int status_id,
                                                       @Query("employee[title]") String title,
                                                       @Query("employee[employment_type_id]") int employment_type_id,
                                                       @Query("employee[zip_code]") String zip_code,
                                                       @Query("employee[tin_no]") String tin_no,
                                                       @Query("employee[sss_no]") String sss_no,
                                                       @Query("employee[drivers_license_no]") String drivers_license_no,
                                                       @Query("employee[civil_status_id]") int civil_status_id,
                                                       @Query("employee[notes]") String employee_notes,
                                                       @Body RequestBody requestBody,
                                                       @Query("user") String user,
                                                       @Query("deviceid") String deviceid,
                                                       @Query("key") String key,
                                                       @Query("_method") String method);

    @DELETE("api_employees/{id}")
    Observable<ResponseWrapper> deleteEmployee(@Path("id") int employee_id,
                                               @Query("user") String user,
                                               @Query("deviceid") String deviceid,
                                               @Query("key") String key,
                                               @Query("_method") String method);




}
