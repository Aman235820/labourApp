package com.example.labourApp.Models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class EnterpriseLabourDTO implements Serializable {

    private Integer enterpriseLabourId;

    @NotBlank
    @Size(max = 64)
    private String enterpriseId;

    @NotBlank
    @Size(max = 200)
    private String fullName;

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String mobile;

    @Pattern(regexp = "^$|^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String alternateMobile;

    @Size(max = 255)
    private String email;

    @Size(max = 64)
    private String role;

    @Size(max = 120)
    private String primarySkill;

    @Size(max = 500)
    private String location;

    @Pattern(regexp = "^$|^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String emergencyContactMobile;

    @Size(max = 500)
    private String profileImageUrl;

    @Size(max = 500)
    private String idDocumentUrl;

    @Size(max = 2000)
    private String notes;

    @Size(max = 32)
    private String status;

    @Size(max = 32)
    private String verificationStatus;

    @Size(max = 64)
    private String joinedAt;

    @Size(max = 1000)
    private String adminComments;

    @Size(max = 64)
    private String registrationTime;

    public void setEnterpriseLabourId(Integer enterpriseLabourId) {
        this.enterpriseLabourId = enterpriseLabourId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPrimarySkill(String primarySkill) {
        this.primarySkill = primarySkill;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmergencyContactMobile(String emergencyContactMobile) {
        this.emergencyContactMobile = emergencyContactMobile;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setIdDocumentUrl(String idDocumentUrl) {
        this.idDocumentUrl = idDocumentUrl;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }

    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }
}
