package com.upi_temp_service.temp_service.pojo;


import java.util.List;
import java.util.Map;

public class ResponsePojo {

    private List<ColumnMetaData> metaData;
    private List<Map<String, Object>> data;
    private String token;
    private int totalCount;
    private int pageCount; // ⬅️ New field

    public List<ColumnMetaData> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<ColumnMetaData> metaDataList) {
        this.metaData = metaDataList;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
