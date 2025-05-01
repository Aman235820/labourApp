package com.example.labourApp.Models;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@RequiredArgsConstructor
public class LabourDTO {

        public Integer labourId;

        public String labourName;

        public String labourSkill;

        public BigInteger labourMobileNo;


}
