package com.upi_temp_service.temp_service.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UIUPI_TMP")
public class UIUPITmpEntity {

    @Id
    @Column(name = "ID")
    private String id; 

    @Lob
    @Column(name = "RAW_JSON", columnDefinition = "CLOB")
    private String rawJson;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DATE_TIME")
    private LocalDateTime dateTime;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Column(name = "TABLE_ID")
    private String tableId;

    @Column(name = "RECORD_ID")
    private String recordId;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRawJson() {
        return rawJson;
    }

    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}

