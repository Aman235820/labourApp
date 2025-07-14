package com.example.labourApp.Repository.sql;

import com.example.labourApp.Entity.sql.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String username);

    boolean existsByEmail(String email);

    Optional<User> findByMobileNumber(String mobileNumber);

    boolean existsByMobileNumber(String mobile);
}
