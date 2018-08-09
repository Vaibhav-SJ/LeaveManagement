package com.example.vaibhav.leavemanagement;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestJson
{
    public static JSONObject getEmployeeDetails()
    {
        return new JSONObject();
    }

    public static JSONObject addLeaveDetails(String eid,float days,String remarks)
    {
        JSONObject object = new JSONObject();

        try {
            object.put("eid",eid);
            object.put("days",days);
            object.put("remarks",remarks);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject getLeaveDetails(String eid)
    {
        JSONObject object = new JSONObject();

        try {
            object.put("eid",eid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject addEmployeeDetails(String name,String phone,String age,
                                                String gender,String img_url,String designation
            ,String email)
    {
        JSONObject object = new JSONObject();

        try {
            object.put("name",name);
            object.put("phone",phone);
            object.put("age",age);
            object.put("gender",gender);
            object.put("img_url",img_url);
            object.put("designation",designation);
            object.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }


}
