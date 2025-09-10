package com.upi_temp_service.temp_service.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upi_temp_service.temp_service.Entity.UIUPIHistoryEntity;
import com.upi_temp_service.temp_service.pojo.ApproveRejectAuditModel;
import com.upi_temp_service.temp_service.repo.UpiHistoryAuditRepo;


@Service
public class ActionAuditService {

    @Autowired
    private UpiHistoryAuditRepo actionAuditRepo;

    public void saveAudit(ApproveRejectAuditModel model) {

        UIUPIHistoryEntity audit = new UIUPIHistoryEntity();

        audit.setId(model.getId());
        audit.setActionType(model.getActionType());
        audit.setCreatedBy(model.getCreatedBy());
        audit.setRawJson(model.getRawJson());
        audit.setDateTime(LocalDateTime.now());
        audit.setTableId(model.getTableId());
        audit.setRecordId(model.getRecordId());
        audit.setStatus(model.getStatus());
        audit.setApprovedBy(model.getApprovedBy());
        audit.setApprovedTime(LocalDateTime.now());
        actionAuditRepo.save(audit);
    }

}