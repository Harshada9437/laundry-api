package com.journaldev.spring.DTO;

import org.springframework.stereotype.Component;

@Component
public class PreRequestDTO {

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



    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (name_of_cloth != null ? name_of_cloth.hashCode() : 0);
        result = 31 * result + (created_date != null ? created_date.hashCode() : 0);
        result = 31 * result + no_of_cloth;
        result = 31 * result + req_id;
        result = 31 * result + user_id;
        return result;
    }

    @Override
    public String toString() {
        return "PreRequestDTO{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", created_date='" + created_date + '\'' +
                ", name_of_cloth='" + name_of_cloth + '\'' +
                ", no_of_cloth='" + no_of_cloth + '\'' +
                ", req_id='" + req_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
