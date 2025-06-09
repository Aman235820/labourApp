package com.example.labourApp.Repository;

import com.example.labourApp.Entity.Labour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabourRepository extends JpaRepository<Labour, Integer> {

    @Query("SELECT l FROM Labour l WHERE SOUNDEX(LOWER(l.labourSkill)) = SOUNDEX(LOWER(CONCAT('%', :category, '%')))")
    Page<Labour> findByLabourSkill(@Param("category") String category , Pageable p);

    boolean existsByLabourMobileNo(String mobileNo);

    @Query("SELECT l FROM Labour l WHERE l.labourMobileNo = :mobileNumber")
    Optional<Labour> findByLabourMobileNo(String mobileNumber);

}
