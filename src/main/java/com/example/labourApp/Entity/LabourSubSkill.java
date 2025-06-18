package com.example.labourApp.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class LabourSubSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subSkillId;

    private String subSkillName;

    @ManyToOne
    @JoinColumn(name = "labour_id") //foreign key
    @JsonIgnore
    private Labour labour;

    // Getter and Setter for subSkillId
    public Integer getSubSkillId() {
        return subSkillId;
    }

    public void setSubSkillId(Integer subSkillId) {
        this.subSkillId = subSkillId;
    }

    // Getter and Setter for labourSubSkills
    public String getSubSkillName() {
        return subSkillName;
    }

    public void setSubSkillName(String subSkillName) {
        this.subSkillName = subSkillName;
    }

    // Getter and Setter for labour
    public Labour getLabour() {
        return labour;
    }

    public void setLabour(Labour labour) {
        this.labour = labour;
    }

}
