package com.upi_temp_service.temp_service.pojo;

import java.util.List;

public class DataTablePayloadModel {
    private String tableCode;
    private int limit;
    private int offset;
    private SortModel sort;
    private List<FilterModel> filters;
    private String tempTableName;
    private String userId;
    private Boolean filterByCurrentUser;

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public SortModel getSort() {
        return sort;
    }

    public void setSort(SortModel sort) {
        this.sort = sort;
    }

    public List<FilterModel> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterModel> filters) {
        this.filters = filters;
    }

    public String getTempTableName() {
        return tempTableName;
    }

    public void setTempTableName(String tempTableName) {
        this.tempTableName = tempTableName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
     public Boolean getFilterByCurrentUser() {
        return filterByCurrentUser;
    }

    public void setFilterByCurrentUser(Boolean filterByCurrentUser) {
        this.filterByCurrentUser = filterByCurrentUser;
    }
}
