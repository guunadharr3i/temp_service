package com.upi_temp_service.temp_service.pojo;

import java.time.LocalDateTime;

public class UIUPITempModel {

    private String id;
    private String rawJson;
    private String createdBy;
    private LocalDateTime dateTime;
    private String status;
    private String actionType;
    private String tableId;
    private String recordId;

    public UIUPITempModel() {}

    public UIUPITempModel(String id, String rawJson, String createdBy, LocalDateTime dateTime,
                         String status, String actionType, String tableId, String recordId) {
        this.id = id;
        this.rawJson = rawJson;
        this.createdBy = createdBy;
        this.dateTime = dateTime;
        this.status = status;
        this.actionType = actionType;
        this.tableId = tableId;
        this.recordId = recordId;
    }

 
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

    @Override
    public String toString() {
        return "UIUPITmpModel{" +
                "id='" + id + '\'' +
                ", rawJson='" + rawJson + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", dateTime=" + dateTime +
                ", status='" + status + '\'' +
                ", actionType='" + actionType + '\'' +
                ", tableId='" + tableId + '\'' +
                ", recordId='" + recordId + '\'' +
                '}';
    }
}
