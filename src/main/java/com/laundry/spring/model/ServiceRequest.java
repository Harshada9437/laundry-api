package com.laundry.spring.model;

import java.util.List;

public class ServiceRequest {
    private int service;
    private String serviceName;
    private List<ClotheRequest> clothes;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getService() {
        return service;
    }

    public void setService(int service) {
        this.service = service;
    }

    public List<ClotheRequest> getClothes() {
        return clothes;
    }

    public void setClothes(List<ClotheRequest> clothes) {
        this.clothes = clothes;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
