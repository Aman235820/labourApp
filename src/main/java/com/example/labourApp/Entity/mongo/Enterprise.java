package com.example.labourApp.Entity.mongo;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Document("enterprise")
public class Enterprise implements Serializable {

    @Id
    private String id;

    private String ownername;

    @NotEmpty
    @Indexed(unique = true)
    private String ownerContactInfo;

    @NotEmpty
    private String companyName;

    private String gstNumber;

    private List<String> otherContactNumbers;

    private String registrationCertificateLink;

    private Integer workforceSize;

    @NotEmpty
    @Indexed
    private Map<String, Object> servicesOffered;

    private Map<String, Object> otherDocumentLinks;

    private String rating;

    private String ratingCount;

    private String location;

    private String verificationStatus;

    private String verifiedAt;

    private String adminComments;

    private String registrationTime;


    // Default constructor
    public Enterprise() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getOwnerContactInfo() {
        return ownerContactInfo;
    }

    public void setOwnerContactInfo(String ownerContactInfo) {
        this.ownerContactInfo = ownerContactInfo;
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

    public List<String> getOtherContactNumbers() {
        return otherContactNumbers;
    }

    public void setOtherContactNumbers(List<String> otherContactNumbers) {
        this.otherContactNumbers = otherContactNumbers;
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

    public Map<String, Object> getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(Map<String, Object> servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public Map<String, Object> getOtherDocumentLinks() {
        return otherDocumentLinks;
    }

    public void setOtherDocumentLinks(Map<String, Object> otherDocumentLinks) {
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

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

}
