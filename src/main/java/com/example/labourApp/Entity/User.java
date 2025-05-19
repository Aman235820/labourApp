package com.example.labourApp.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String fullName;
    private String email;
    private String password;
    private String mobileNumber;

    // Getter and Setter for fullName
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for mobileNumber
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
} 