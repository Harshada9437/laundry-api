package com.laundry.spring.DTO;

import java.util.List;

public class RequestDto {
    private int id;
    private int userId;
    private String category;
    private int categoryId;
    private String customerName;
    private String clotheName;
    private int clotheId;
    private int clotheCount;
    private String createDate;
    private String updateDate;
    private String pickupDate;
    private String remark;
    private String status;
    private int statusId;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getClotheId() {
        return clotheId;
    }

    public void setClotheId(int clotheId) {
        this.clotheId = clotheId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
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
