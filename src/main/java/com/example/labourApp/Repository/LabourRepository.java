package com.example.labourApp.Repository;

import com.example.labourApp.Entity.Labour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabourRepository extends JpaRepository<Labour , Integer> {

}
