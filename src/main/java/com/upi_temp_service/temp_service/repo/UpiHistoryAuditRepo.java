package com.upi_temp_service.temp_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.upi_temp_service.temp_service.Entity.UIUPIHistoryEntity;

@Repository
public interface UpiHistoryAuditRepo extends JpaRepository<UIUPIHistoryEntity, String> {

}