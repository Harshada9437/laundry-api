package com.laundry.spring.DTO;

public class UserDto {
    private String name;
    private String contactNo;
    private String status;
    private String username;
    private String password;
    private int complexId;
    private int wingId;
    private int flatNo;
    private String email;

    public int getComplexId() {
        return complexId;
    }

    public void setComplexId(int complexId) {
        this.complexId = complexId;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    public int getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(int flatNo) {
        this.flatNo = flatNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", status='" + status + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", complexId=" + complexId +
                ", wingId=" + wingId +
                ", flatNo=" + flatNo +
                ", email='" + email + '\'' +
                '}';
    }
}
