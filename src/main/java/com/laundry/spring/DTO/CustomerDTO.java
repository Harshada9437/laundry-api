package com.laundry.spring.DTO;

public class CustomerDTO {

    private int id;
    private String name;
    private String contact_no;
    private String email;
    private String Status;
    private String created_date;
    private String comp_name;
    private String flat_no;
    private String type;
    private String wing;
    private String username;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWing() {
        return wing;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getComp_name() {
        return comp_name;
    }

    public void setComp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;

        }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contact_no='" + contact_no + '\'' +
                ", email='" + email + '\'' +
                ", Status='" + Status + '\'' +
                ", created_date='" + created_date + '\'' +
                ", comp_name='" + comp_name + '\'' +
                ", flat_no='" + flat_no + '\'' +
                ", type='" + type + '\'' +
                ", wing='" + wing + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
