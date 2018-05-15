package com.laundry.spring.DTO;

import java.util.List;

public class RequestDto {
    private int id;
    List<String> category;
    private String customerName;
    private String clotheName;
    private int clotheCount;
    private String createDate;
    private String updateDate;
    private String status;

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getClotheName() {
        return clotheName;
    }

    public void setClotheName(String clotheName) {
        this.clotheName = clotheName;
    }

    public int getClotheCount() {
        return clotheCount;
    }

    public void setClotheCount(int clotheCount) {
        this.clotheCount = clotheCount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RequestDto{" +
                "id=" + id +
                ", category=" + category +
                ", customerName='" + customerName + '\'' +
                ", clotheName='" + clotheName + '\'' +
                ", clotheCount=" + clotheCount +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
