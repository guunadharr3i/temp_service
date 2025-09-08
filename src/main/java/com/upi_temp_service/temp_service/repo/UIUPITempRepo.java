package com.upi_temp_service.temp_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.upi_temp_service.temp_service.Entity.UIUPITmpEntity;



@Repository
public interface UIUPITempRepo extends JpaRepository<UIUPITmpEntity, String> {
 
}