package com.example.labourApp.Entity.sql;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Labour")
public class Labour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer labourId;

    private String labourName;

    private String labourSkill;

    @OneToMany(mappedBy = "labour", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LabourSubSkill> labourSubSkills = new ArrayList<>();

    @Column(unique = true, nullable = false)
    private String labourMobileNo;

    private String rating;
    private String ratingCount;

    private String registrationTime;

    private String profileImage;

    @OneToMany(mappedBy = "labour", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();


    // Newly added fields
    private String aadhaarUrl;
    private String labourLocation;

    private String verificationStatus; // e.g., PENDING, VERIFIED, REJECTED
    private String verifiedAt;
    private String adminComments;


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

    // Getter and Setter for reviews
    public List<Review> getReviews() {
        return reviews;
    }

    public void addReviews(Review review) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(review);
    }

    public void setReviews(List<Review> list) {
        this.reviews = list;
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


    // New Fields Getters and Setters

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
