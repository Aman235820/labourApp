package com.example.labourApp.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer userId;
    private Double rating;
    private String review;

    private LocalDate reviewTime;

    @ManyToOne
    @JoinColumn(name = "labour_id")
    @JsonIgnore
    private Labour labour;


    // Getter and Setter for Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    // Getter and Setter for userId
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    // Getter and Setter for rating
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    // Getter and Setter for review
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Labour getLabour() {
        return labour;
    }

    public void setLabour(Labour labour) {
        this.labour = labour;
    }

    public LocalDate getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(LocalDate time) {
        this.reviewTime = time;
    }


} 