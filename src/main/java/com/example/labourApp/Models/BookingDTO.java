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
    private String userName;
    private String labourName;
    private Integer bookingStatusCode;

    private String preferredDate;
    private String preferredTime;
    private String workDescription;
    private String urgencyLevel;


    public Integer getBookingStatusCode() {
        return bookingStatusCode;
    }

    public void setBookingStatusCode(Integer bookingStatusCode) {
        this.bookingStatusCode = bookingStatusCode;
    }


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

    public String getUserName() {
        return userName;
    }

    public String getLabourName() {
        return labourName;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLabourName(String labourName) {
        this.labourName = labourName;
    }


    public String getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(String preferredDate) {
        this.preferredDate = preferredDate;
    }

    public String getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(String preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }


}
