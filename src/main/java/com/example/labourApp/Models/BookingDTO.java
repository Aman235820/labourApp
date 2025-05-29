package com.example.labourApp.Models;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BookingDTO {
    private Integer userId;
    private Integer labourId;
    private String labourMobileNo;
    private String labourSkill;
    private String userMobileNumber;
    private String bookingTime;

    // Getters
    public Integer getUserId() {
        return userId;
    }

    public Integer getLabourId() {
        return labourId;
    }

    public String getLabourMobileNo() {
        return labourMobileNo;
    }

    public String getLabourSkill() {
        return labourSkill;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    // Setters
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setLabourId(Integer labourId) {
        this.labourId = labourId;
    }

    public void setLabourMobileNo(String labourMobileNo) {
        this.labourMobileNo = labourMobileNo;
    }

    public void setLabourSkill(String labourSkill) {
        this.labourSkill = labourSkill;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }
}
