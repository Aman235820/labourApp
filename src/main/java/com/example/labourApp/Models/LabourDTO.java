package com.example.labourApp.Models;

import com.example.labourApp.Entity.sql.LabourSubSkill;
import com.example.labourApp.Entity.sql.Review;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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

    // Newly added fields
    private String aadhaarUrl;
    private String labourLocation;
    private String verificationStatus;
    private String verifiedAt;
    private String adminComments;

    // Getters and Setters

    public Integer getLabourId() {
        return labourId;
    }

    public void setLabourId(Integer labourId) {
        this.labourId = labourId;
    }

    public String getLabourName() {
        return labourName;
    }

    public void setLabourName(String labourName) {
        this.labourName = labourName;
    }

    public String getLabourSkill() {
        return labourSkill;
    }

    public void setLabourSkill(String labourSkill) {
        this.labourSkill = labourSkill;
    }

    public List<LabourSubSkill> getLabourSubSkills() {
        return labourSubSkills;
    }

    public void setLabourSubSkills(List<LabourSubSkill> labourSubSkills) {
        this.labourSubSkills = labourSubSkills;
    }

    public String getLabourMobileNo() {
        return labourMobileNo;
    }

    public void setLabourMobileNo(String labourMobileNo) {
        this.labourMobileNo = labourMobileNo;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        this.ratingCount = ratingCount;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
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

    // New fields' getters and setters

    public String getAadhaarUrl() {
        return aadhaarUrl;
    }

    public void setAadhaarUrl(String aadhaarUrl) {
        this.aadhaarUrl = aadhaarUrl;
    }

    public String getLabourLocation() {
        return labourLocation;
    }

    public void setLabourLocation(String labourLocation) {
        this.labourLocation = labourLocation;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(String verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getAdminComments() {
        return adminComments;
    }

    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }
}
