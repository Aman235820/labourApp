package com.example.labourApp.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigInteger;

@Entity
public class Labour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer labourId;

    public String labourName;

    public String labourSkill;

    public BigInteger labourMobileNo;

}
