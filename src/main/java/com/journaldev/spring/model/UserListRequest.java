package com.journaldev.spring.model;

public class UserListRequest {

    private String name;
    private String status;
    private int no_of_cloth;
    private String name_of_cloth;
    private String created_date;
    private int req_id;
    private int user_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNo_of_cloth() {
        return no_of_cloth;
    }

    public void setNo_of_cloth(int no_of_cloth) {
        this.no_of_cloth = no_of_cloth;
    }

    public String getName_of_cloth() {
        return name_of_cloth;
    }

    public void setName_of_cloth(String name_of_cloth) {
        this.name_of_cloth = name_of_cloth;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public int getReq_id() {
        return req_id;
    }

    public void setReq_id(int req_id) {
        this.req_id = req_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
