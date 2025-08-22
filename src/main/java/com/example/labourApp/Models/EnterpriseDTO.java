package com.example.labourApp.Models;


import com.example.labourApp.Entity.sql.Review;

import java.util.List;

public class EnterpriseDTO {

    private Long id;
    private String ownername;
    private String companyName;
    private String gstNumber;
    private String registrationCertificateLink;
    private Integer workforceSize;
    private String servicesOffered;
    private String otherDocumentLinks;
    private String rating;
    private String ratingCount;
    private List<Review> reviews;
    private String profileImage;
    private String location;
    private String verificationStatus;
    private String verifiedAt;
    private String adminComments;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getRegistrationCertificateLink() {
        return registrationCertificateLink;
    }

    public void setRegistrationCertificateLink(String registrationCertificateLink) {
        this.registrationCertificateLink = registrationCertificateLink;
    }

    public Integer getWorkforceSize() {
        return workforceSize;
    }

    public void setWorkforceSize(Integer workforceSize) {
        this.workforceSize = workforceSize;
    }

    public String getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(String servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public String getOtherDocumentLinks() {
        return otherDocumentLinks;
    }

    public void setOtherDocumentLinks(String otherDocumentLinks) {
        this.otherDocumentLinks = otherDocumentLinks;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
