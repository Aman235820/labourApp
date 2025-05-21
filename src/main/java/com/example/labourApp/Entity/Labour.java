package com.example.labourApp.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
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

    private String labourMobileNo;

    private String rating;
    private String ratingCount;

    private List<String> reviews;

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
    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(reviews);
    }

    @Override
    public String toString() {
        return "Labour{" +
                "labourId=" + labourId +
                ", labourName='" + labourName + '\'' +
                ", labourSkill='" + labourSkill + '\'' +
                ", labourMobileNo='" + labourMobileNo + '\'' +
                ", rating='" + rating + '\'' +
                ", ratingCount='" + ratingCount + '\'' +
                ", reviews=" + (reviews != null ? String.join(", ", reviews) : "null") +
                '}';
    }
}
