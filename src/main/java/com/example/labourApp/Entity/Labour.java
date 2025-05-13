package com.example.labourApp.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

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

    @Override
    public String toString() {
        return "Labour{" +
                "labourId=" + labourId +
                ", labourName='" + labourName + '\'' +
                ", labourSkill='" + labourSkill + '\'' +
                ", labourMobileNo=" + labourMobileNo +
                '}';
    }
}
