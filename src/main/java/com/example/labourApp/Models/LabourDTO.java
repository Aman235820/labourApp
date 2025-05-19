package com.example.labourApp.Models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class LabourDTO {

    private Integer labourId;

    @NotEmpty
    private String labourName;

    @NotEmpty
    private String labourSkill;

    @NotEmpty
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String labourMobileNo;

    private String rating;

    private String ratingCount;

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
}
