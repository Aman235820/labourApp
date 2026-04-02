package com.example.labourApp.Repository.sql;


import com.example.labourApp.Entity.sql.EnterpriseLabourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnterpriseLabourRepository extends JpaRepository<EnterpriseLabourEntity , Integer> {
    List<EnterpriseLabourEntity> findEnterpriseLabourEntitiesByEnterpriseId(String enterpriseId);
}
