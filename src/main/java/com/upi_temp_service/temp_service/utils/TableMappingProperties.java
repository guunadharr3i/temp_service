package com.upi_temp_service.temp_service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class TableMappingProperties {

    @Value("${table-mapping.UPITEMP}")
    private String upiTemp;

    public String getTableName(String code) {
        Map<String, String> map = new HashMap<>();

        map.put("UPITEMP", upiTemp);

        return map.get(code);
    }
}
