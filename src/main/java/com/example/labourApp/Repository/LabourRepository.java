package com.example.labourApp.Repository;

import com.example.labourApp.Entity.Labour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabourRepository extends JpaRepository<Labour, Integer> {

    @Query("SELECT l FROM Labour l WHERE LOWER(l.labourSkill) LIKE LOWER(CONCAT('%', :category, '%'))")
    Page<Labour> findByLabourSkill(@Param("category") String category , Pageable p);
}
