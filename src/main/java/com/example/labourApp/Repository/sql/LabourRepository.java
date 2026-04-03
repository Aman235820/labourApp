package com.example.labourApp.Repository.sql;

import com.example.labourApp.Entity.sql.Labour;
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
    Page<Labour> findByLabourSkill(@Param("category") String category, Pageable p);

    @Query("SELECT l FROM Labour l WHERE LOWER(l.labourSkill) = LOWER(:category)")
    Page<Labour> findByLabourSkillExact(@Param("category") String category, Pageable p);

    boolean existsByLabourMobileNo(String mobileNo);

    @Query("SELECT l FROM Labour l WHERE l.labourMobileNo = :mobileNumber")
    Optional<Labour> findByLabourMobileNo(String mobileNumber);

    @Modifying
    @Query(value = "SET FOREIGN_KEY_CHECKS = 0; TRUNCATE defaultdb.labour_sub_skill; TRUNCATE TABLE defaultdb.labour; SET FOREIGN_KEY_CHECKS = 1;", nativeQuery = true)
    void truncateLabourTable();

    /** JPQL (no hardcoded schema) so H2 dev and MySQL prod both work; sort comes from {@code Pageable}. */
    @Query(
            value = "SELECT DISTINCT l FROM Labour l JOIN l.labourSubSkills s WHERE "
                    + "SOUNDEX(s.subSkillName) = SOUNDEX(:category) OR "
                    + "LOWER(s.subSkillName) LIKE LOWER(CONCAT('%', :category, '%'))",
            countQuery = "SELECT COUNT(DISTINCT l.labourId) FROM Labour l JOIN l.labourSubSkills s WHERE "
                    + "SOUNDEX(s.subSkillName) = SOUNDEX(:category) OR "
                    + "LOWER(s.subSkillName) LIKE LOWER(CONCAT('%', :category, '%'))"
    )
    Page<Labour> findByLabourSubSkill(@Param("category") String category, Pageable pageable);

    @Query(
            value = "SELECT DISTINCT l FROM Labour l JOIN l.labourSubSkills s WHERE LOWER(s.subSkillName) = LOWER(:category)",
            countQuery = "SELECT COUNT(DISTINCT l.labourId) FROM Labour l JOIN l.labourSubSkills s WHERE LOWER(s.subSkillName) = LOWER(:category)"
    )
    Page<Labour> findByLabourSubSkillExact(@Param("category") String category, Pageable pageable);

//            | Part         | Why                                                             |
//            | ------------ | --------------------------------------------------------------- |
//            | `countQuery` | Ensures Spring Data doesn't try to auto-generate an invalid one |
//            | `DISTINCT`   | Avoids double-counting labours with multiple sub-skills         |
//            | `@Param`     | Required for `:category` usage                                  |

}
