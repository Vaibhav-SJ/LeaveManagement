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


}
