package com.example.labourApp.Repository;

import com.example.labourApp.Entity.Labour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabourRepository extends JpaRepository<Labour, Integer> {

    @Query("SELECT l FROM Labour l WHERE SOUNDEX(LOWER(l.labourSkill)) = SOUNDEX(LOWER(CONCAT('%', :category, '%')))")
    Page<Labour> findByLabourSkill(@Param("category") String category , Pageable p);

    boolean existsByLabourMobileNo(String mobileNo);

    @Query("SELECT l FROM Labour l WHERE l.labourMobileNo = :mobileNumber")
    Optional<Labour> findByLabourMobileNo(String mobileNumber);

    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS = 0; TRUNCATE defaultdb.labour_sub_skill; TRUNCATE TABLE defaultdb.labour; SET FOREIGN_KEY_CHECKS = 1;", nativeQuery = true)
    void truncateLabourTable();

    @Query(value = "SELECT l.* FROM defaultdb.labour AS l JOIN defaultdb.labour_sub_skill AS s ON l.labour_id = s.labour_id WHERE SOUNDEX(s.sub_skill_name) = SOUNDEX(:category) OR LOWER(s.sub_skill_name) LIKE LOWER(CONCAT('%', :category, '%'))" , nativeQuery = true)
    Page<Labour> findByLabourSubSkill(String category, Pageable p);
}
