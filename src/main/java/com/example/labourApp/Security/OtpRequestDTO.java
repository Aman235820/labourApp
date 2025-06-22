package com.example.labourApp.Security;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OtpRequestDTO {

    @NotEmpty
    private String mobile;

    private String role; // e.g., USER or ADMIN


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }



}


