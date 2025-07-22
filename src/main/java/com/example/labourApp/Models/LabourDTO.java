package com.example.labourApp.Models;

import com.example.labourApp.Entity.sql.LabourSubSkill;
import com.example.labourApp.Entity.sql.Review;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class LabourDTO implements Serializable {

    private Integer labourId;

    @NotEmpty
    private String labourName;

    @NotEmpty
    private String labourSkill;

    private List<LabourSubSkill> labourSubSkills;

    private String registrationTime;

    @NotEmpty
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String labourMobileNo;

    private String rating;

    private String ratingCount;

    private List<Review> reviews;

    private String profileImage;

    // Getter and Setter for labourId
    public Integer getLabourId() {
        return labourId;
    }

    public void setLabourId(Integer labourId) {
        this.labourId = labourId;
    }

    // Getter and Setter for labourName
    public String getLabourName() {
        return labourName;
    }

    public void setLabourName(String labourName) {
        this.labourName = labourName;
    }

    // Getter and Setter for rating
    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    // Getter and Setter for ratingCount
    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    // Getter and Setter for labourSkill
    public String getLabourSkill() {
        return labourSkill;
    }

    public void setLabourSkill(String labourSkill) {
        this.labourSkill = labourSkill;
    }

    // Getter and Setter for labourMobileNo
    public String getLabourMobileNo() {
        return labourMobileNo;
    }

    public void setLabourMobileNo(String labourMobileNo) {
        this.labourMobileNo = labourMobileNo;
    }

    // Getter and Setter for reviews
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<LabourSubSkill> getLabourSubSkills() {
        return labourSubSkills;
    }

    public void setLabourSubSkills(List<LabourSubSkill> labourSubSkills) {
        this.labourSubSkills = labourSubSkills;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }



}
