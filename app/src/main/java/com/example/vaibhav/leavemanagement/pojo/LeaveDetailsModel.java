package com.example.vaibhav.leavemanagement.pojo;

import java.util.List;

public class LeaveDetailsModel
{

    /**
     * success : true
     * leaveDetails : [{"leaveSanctionedOn":"08/08/2018 22:50 Wed","days":"half day","remarks":"Finish your work before you go"},{"leaveSanctionedOn":"09/08/2018 03:23 Thu","days":"1 day","remarks":"Finish your work before you go"},{"leaveSanctionedOn":"09/08/2018 03:48 Thu","days":"4 and half days","remarks":"Na"}]
     */

    private boolean success;
    /**
     * leaveSanctionedOn : 08/08/2018 22:50 Wed
     * days : half day
     * remarks : Finish your work before you go
     */

    private List<LeaveDetailsBean> leaveDetails;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<LeaveDetailsBean> getLeaveDetails() {
        return leaveDetails;
    }

    public void setLeaveDetails(List<LeaveDetailsBean> leaveDetails) {
        this.leaveDetails = leaveDetails;
    }

    public static class LeaveDetailsBean {
        private String leaveSanctionedOn;
        private String days;
        private String remarks;

        public String getLeaveSanctionedOn() {
            return leaveSanctionedOn;
        }

        public void setLeaveSanctionedOn(String leaveSanctionedOn) {
            this.leaveSanctionedOn = leaveSanctionedOn;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
