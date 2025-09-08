package com.upi_temp_service.temp_service.pojo;


import java.util.List;

public class UpiTempCountRequest {
    private String userId;
    private List<String> tableIds;
    private Boolean filterByCurrentUser;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getTableIds() {
        return tableIds;
    }
    public void setTableIds(List<String> tableIds) {
        this.tableIds = tableIds;
    }

    public Boolean getFilterByCurrentUser() {
        return filterByCurrentUser;
    }
    public void setFilterByCurrentUser(Boolean filterByCurrentUser) {
        this.filterByCurrentUser = filterByCurrentUser;
    }
}
