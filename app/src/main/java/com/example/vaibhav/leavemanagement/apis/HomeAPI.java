package com.example.vaibhav.leavemanagement.apis;

import com.example.vaibhav.leavemanagement.pojo.GetEmployeeDetailsModule;
import com.example.vaibhav.leavemanagement.pojo.LeaveDetailsModel;
import com.example.vaibhav.leavemanagement.pojo.SuccessModel;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.mime.TypedInput;

public interface HomeAPI
{
    @POST("/index.php/admin/getAllEmployeeDetails")
    void getEmployeeDetails(@Body TypedInput input, Callback<GetEmployeeDetailsModule> getEmployeeDetailsModuleCallback);

    @POST("/index.php/admin/addLeaveDetails")
    void addLeaveDetails(@Body TypedInput input, Callback<SuccessModel> successModelCallback);

    @POST("/index.php/admin/getLeaveDetailsByEID")
    void getLeaveDetails(@Body TypedInput input, Callback<LeaveDetailsModel> leaveDetailsModelCallback);
}
