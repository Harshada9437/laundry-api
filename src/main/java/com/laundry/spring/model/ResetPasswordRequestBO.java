package com.laundry.spring.model;

/**
 * Created by System-3 on 12/7/2016.
 */
public class ResetPasswordRequestBO
{
    private int id;
    private String newPassword;

    public ResetPasswordRequestBO(int id, String newPassword) {
        this.id = id;
        this.newPassword = newPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
