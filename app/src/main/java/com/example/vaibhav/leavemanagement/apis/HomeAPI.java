package com.example.vaibhav.leavemanagement.apis;

import com.example.vaibhav.leavemanagement.pojo.GetEmployeeDetailsModule;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.mime.TypedInput;

public interface HomeAPI
{
    @POST("/index.php/admin/getAllEmployeeDetails")
    void getEmployeeDetails(@Body TypedInput input, Callback<GetEmployeeDetailsModule> getEmployeeDetailsModuleCallback);
}
