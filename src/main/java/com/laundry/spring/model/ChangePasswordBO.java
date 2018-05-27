package com.laundry.spring.model;

/**
 * Created by System-2 on 2/10/2017.
 */
public class ChangePasswordBO {
    private int id;
    private String oldPassword;
    private String newPassword;

    public ChangePasswordBO(int userId, String oldPassword, String newPassword) {
        this.id = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public int getId() {
        return id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
