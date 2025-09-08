package com.upi_temp_service.temp_service.pojo;

public class ColumnMetaData {

    private String name;
    private String type;
    private String size;       
    private Boolean nonEditable; 

    public ColumnMetaData() {}

    public ColumnMetaData(String name, String type, String size, Boolean nonEditable) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.nonEditable = nonEditable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getNonEditable() {
        return nonEditable;
    }

    public void setNonEditable(Boolean nonEditable) {
        this.nonEditable = nonEditable;
    }

    @Override
    public String toString() {
        return "ColumnMetaData{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                ", nonEditable=" + nonEditable +
                '}';
    }
}
