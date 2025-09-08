package com.upi_temp_service.temp_service.repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UpiTmpCountRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UpiTmpCountRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getCounts(String userId, List<String> tableIds, Boolean filterByCurrentUser) {
        String sql = "SELECT TABLE_ID, COUNT(*) AS name_count " +
                "FROM UIUPI_TMP " +
                "WHERE TABLE_ID IN (:tableIds) " +
                ((filterByCurrentUser != null && filterByCurrentUser)
                        ? "AND CREATED_BY = :userId "
                        : "AND CREATED_BY <> :userId ")
                +
                "GROUP BY TABLE_ID " +
                "ORDER BY TABLE_ID";

        Map<String, Object> params = new HashMap<>();
        params.put("tableIds", tableIds);
        params.put("userId", userId);

        return jdbcTemplate.queryForList(sql, params);
    }
}
