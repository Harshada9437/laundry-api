package com.laundry.spring.bo;

import com.laundry.spring.model.ServiceRequest;

import java.util.List;

public class LaundryRequestBO {
    private int userId;
    private List<ServiceRequest> requests;
    private String pickupDate;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ServiceRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<ServiceRequest> requests) {
        this.requests = requests;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }
}
