package com.example.vaibhav.leavemanagement.pojo;

import java.util.List;

public class GetEmployeeDetailsModule
{

    /**
     * success : true
     * employeeDetails : [{"id":"1","name":"John","phone":"9964396588","age":"45","gender":"Male","img_url":"https://s3.ap-southeast-1.amazonaws.com/images.deccanchronicle.com/dc-Cover-k9kadk7lho9mrs9cpcbsdefk66-20171211150932.Medi.jpeg","designation":"CEO","email":"john@gmail.com"},{"id":"2","name":"SRK","phone":"9964398888","age":"40","gender":"Male","img_url":"https://static.toiimg.com/photo/63264644.cms","designation":"Co-Founder","email":"srk@gmail.com"},{"id":"3","name":"Yuvi","phone":"8884050684","age":"35","gender":"Male","img_url":"http://im.rediff.com/cricket/2017/jun/14yuvraj1.jpg","designation":"Developer","email":"uv@gmail.com"}]
     */

    private boolean success;
    /**
     * id : 1
     * name : John
     * phone : 9964396588
     * age : 45
     * gender : Male
     * img_url : https://s3.ap-southeast-1.amazonaws.com/images.deccanchronicle.com/dc-Cover-k9kadk7lho9mrs9cpcbsdefk66-20171211150932.Medi.jpeg
     * designation : CEO
     * email : john@gmail.com
     */

    private List<EmployeeDetailsBean> employeeDetails;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<EmployeeDetailsBean> getEmployeeDetails() {
        return employeeDetails;
    }

    public void setEmployeeDetails(List<EmployeeDetailsBean> employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    public static class EmployeeDetailsBean {
        private String id;
        private String name;
        private String phone;
        private String age;
        private String gender;
        private String img_url;
        private String designation;
        private String email;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
