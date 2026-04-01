package com.example.labourApp.Entity.sql;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(
        name = "enterprise_labour",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_enterprise_labour_enterprise_mobile",
                columnNames = {"enterprise_id", "mobile"}
        ),
        indexes = {
                @Index(name = "idx_enterprise_labour_enterprise_id", columnList = "enterprise_id")
        }
)
public class EnterpriseLabourEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enterprise_labour_id")
    private Integer enterpriseLabourId;

    @Column(name = "enterprise_id", nullable = false, length = 64)
    private String enterpriseId;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "mobile", nullable = false, length = 20)
    private String mobile;

    @Column(name = "alternate_mobile", length = 20)
    private String alternateMobile;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "role", length = 64)
    private String role;

    @Column(name = "primary_skill", length = 120)
    private String primarySkill;

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "emergency_contact_mobile", length = 20)
    private String emergencyContactMobile;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(name = "id_document_url", length = 500)
    private String idDocumentUrl;

    @Column(name = "notes", length = 2000)
    private String notes;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "verification_status", length = 32)
    private String verificationStatus;

    @Column(name = "joined_at", length = 64)
    private String joinedAt;

    @Column(name = "admin_comments", length = 1000)
    private String adminComments;

    @Column(name = "registration_time", length = 64)
    private String registrationTime;

    public Integer getEnterpriseLabourId() {
        return enterpriseLabourId;
    }

    public void setEnterpriseLabourId(Integer enterpriseLabourId) {
        this.enterpriseLabourId = enterpriseLabourId;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternateMobile() {
        return alternateMobile;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPrimarySkill() {
        return primarySkill;
    }

    public void setPrimarySkill(String primarySkill) {
        this.primarySkill = primarySkill;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmergencyContactMobile() {
        return emergencyContactMobile;
    }

    public void setEmergencyContactMobile(String emergencyContactMobile) {
        this.emergencyContactMobile = emergencyContactMobile;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getIdDocumentUrl() {
        return idDocumentUrl;
    }

    public void setIdDocumentUrl(String idDocumentUrl) {
        this.idDocumentUrl = idDocumentUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
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
