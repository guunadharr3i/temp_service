package com.upi_temp_service.temp_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.upi_temp_service.temp_service.Entity.UIUPITmpEntity;
import com.upi_temp_service.temp_service.pojo.ColumnMetaData;
import com.upi_temp_service.temp_service.pojo.FilterModel;
import com.upi_temp_service.temp_service.pojo.ResponsePojo;
import com.upi_temp_service.temp_service.pojo.UIUPITempModel;
import com.upi_temp_service.temp_service.repo.UIUPITempRepo;
import com.upi_temp_service.temp_service.utils.TableMappingProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UpiTempService {
    private static final Logger logger = LogManager.getLogger(UpiTempService.class);
    private final TableMappingProperties tableMappingProperties;

    @Autowired
    public UpiTempService(TableMappingProperties tableMappingProperties) {
        this.tableMappingProperties = tableMappingProperties;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UIUPITempRepo uiupiTempRepo;

    public ResponsePojo getFilteredUpiTempData(
            String tableCode, int limit, int offset,
            String sortColumn, String sortDirection,
            List<FilterModel> filters, String tempTableName, String makerId, Boolean showPendingReq) {

        String tableName = tableMappingProperties.getTableName(tableCode);

        logger.info("Fetching filtered data from {}", tableName);

        StringBuilder baseFilterBuilder = new StringBuilder(" WHERE 1=1");

        if (tempTableName != null && !tempTableName.trim().isEmpty()) {
            baseFilterBuilder.append(" AND TABLE_ID = '").append(tempTableName).append("'");

            if (Boolean.TRUE.equals(showPendingReq)) {
                baseFilterBuilder.append(" AND CREATED_BY = '").append(makerId).append("'");
                logger.debug("Including user's own requests for tempTableName: {}", tempTableName);
            } else {
                baseFilterBuilder.append(" AND CREATED_BY != '").append(makerId).append("'");
                logger.debug("Excluding user's own requests for tempTableName: {}", tempTableName);
            }
        }

        List<String> validColumns = new ArrayList<>();
        jdbcTemplate.query("SELECT * FROM " + tableName + " FETCH FIRST 1 ROWS ONLY",
                rs -> {
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        validColumns.add(metaData.getColumnName(i).toUpperCase());
                    }
                    return null;
                });

        if (filters != null) {
            for (FilterModel filter : filters) {
                String col = filter.getColumn();
                String value = filter.getValue();
                String operator = filter.getOperator();

                if (col != null && !col.isEmpty() &&
                        operator != null && !operator.isEmpty() &&
                        value != null && !value.isEmpty()) {

                    String colUpper = col.toUpperCase();
                    boolean isValid = validColumns.stream().anyMatch(c -> c.equals(colUpper));

                    if (isValid) {
                        if ("LIKE".equalsIgnoreCase(operator)) {
                            baseFilterBuilder.append(" AND LOWER(")
                                    .append(colUpper)
                                    .append(") LIKE '%")
                                    .append(value.toLowerCase())
                                    .append("%'");
                        } else if ("=".equalsIgnoreCase(operator)) {
                            baseFilterBuilder.append(" AND ")
                                    .append(colUpper)
                                    .append(" = '")
                                    .append(value)
                                    .append("'");
                        } else {
                            throw new IllegalArgumentException("Unsupported filter operator: " + operator);
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid filter column: " + col);
                    }
                }
            }
        }

        String countQuery = "SELECT COUNT(*) FROM " + tableName + baseFilterBuilder;
        int totalCount = jdbcTemplate.queryForObject(countQuery, Integer.class);

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ").append(tableName).append(baseFilterBuilder);

        if (sortColumn != null && sortDirection != null) {
            String sortColUpper = sortColumn.toUpperCase();
            boolean isValidSort = validColumns.stream().anyMatch(c -> c.equals(sortColUpper));
            if (isValidSort) {
                queryBuilder.append(" ORDER BY ").append(sortColUpper).append(" ").append(sortDirection);
            } else {
                logger.warn("Invalid sort column: {}, using fallback ID DESC", sortColumn);
                queryBuilder.append(" ORDER BY ID DESC");
            }
        } else {
            queryBuilder.append(" ORDER BY ID DESC");
        }

        int rowOffset = offset * limit;
        queryBuilder.append(" OFFSET ").append(rowOffset)
                .append(" ROWS FETCH NEXT ").append(limit).append(" ROWS ONLY");

        String finalQuery = queryBuilder.toString();
        logger.info("Executing SQL query: {}", finalQuery);

        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(finalQuery);

        List<ColumnMetaData> metaDataList = getTableMetadata(tableName, "UIUPI_TMP");

        ResponsePojo response = new ResponsePojo();
        response.setMetaData(metaDataList);
        response.setData(dataList);
        response.setTotalCount(totalCount);
        response.setPageCount((int) Math.ceil((double) totalCount / limit));

        logger.info("Returning {} rows with {} metadata columns, totalCount: {}",
                dataList.size(), metaDataList.size(), totalCount);

        return response;
    }

    public List<ColumnMetaData> getTableMetadata(String tableName, String tableCode) {

        String metaQuery = "SELECT COLUMN_NAME, DATA_TYPE, CHAR_LENGTH, DATA_PRECISION, DATA_SCALE, " +
                "NULLABLE, DATA_DEFAULT " +
                "FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";

        return jdbcTemplate.query(metaQuery, new Object[] { tableName.toUpperCase() }, (rs, rowNum) -> {
            ColumnMetaData meta = new ColumnMetaData();
            String columnName = rs.getString("COLUMN_NAME");
            meta.setName(columnName);

            String sqlType = rs.getString("DATA_TYPE");
            int charLength = rs.getInt("CHAR_LENGTH");
            Integer precision = rs.getObject("DATA_PRECISION") != null ? rs.getInt("DATA_PRECISION") : null;
            Integer scale = rs.getObject("DATA_SCALE") != null ? rs.getInt("DATA_SCALE") : null;

            if ("VARCHAR2".equalsIgnoreCase(sqlType) || "CHAR".equalsIgnoreCase(sqlType)
                    || "NVARCHAR2".equalsIgnoreCase(sqlType)) {
                meta.setSize(String.valueOf(charLength));
            } else if ("NUMBER".equalsIgnoreCase(sqlType)) {
                if (precision != null && precision > 0) {
                    meta.setSize(precision + "," + (scale != null ? scale : 0));
                } else {
                    meta.setSize("");
                }
            } else {
                meta.setSize("");
            }

            meta.setType(convertToJavaType(sqlType, precision, scale));

            meta.setNonEditable(null);
            return meta;
        });
    }

    public String convertToJavaType(String sqlType, Integer precision, Integer scale) {
        if (sqlType == null)
            return "Object";

        switch (sqlType.toUpperCase()) {
            case "NUMBER":
                if (scale != null && scale > 0) {
                    return "Double";
                } else {
                    return "Long";
                }
            case "VARCHAR2":
            case "CHAR":
            case "NVARCHAR2":
            case "CLOB":
                return "String";
            case "DATE":
            case "TIMESTAMP":
                return "java.util.Date";
            case "BLOB":
                return "byte[]";
            case "BOOLEAN":
                return "Boolean";
            default:
                return "Object";
        }
    }

    public boolean checkIfExists(String compositeId) {
        try {
            return uiupiTempRepo.existsById(compositeId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Failed to check existence for ID: " + compositeId + ". Error: " + e.getMessage());
        }
    }

    public String saveEntity(UIUPITempModel model) {
        try {
            UIUPITmpEntity entity = new UIUPITmpEntity();
            entity.setId(model.getId());
            entity.setRawJson(model.getRawJson());
            entity.setCreatedBy(model.getCreatedBy());
            entity.setDateTime(LocalDateTime.now());
            entity.setStatus("PENDING");
            entity.setActionType(model.getActionType());
            entity.setRecordId(model.getRecordId());
            entity.setTableId(model.getTableId());

            uiupiTempRepo.save(entity);

            return "Success";

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Failed to save entity with ID: " + model.getId() + ". Error: " + e.getMessage());
        }
    }

    public Optional<UIUPITmpEntity> getTempData(String compositeId) {
        try {
            Optional<UIUPITmpEntity> entity = uiupiTempRepo.findById(compositeId);
            if (entity.isPresent()) {
                logger.info("Temp data found for ID {}", compositeId);
            } else {
                logger.warn("No temp data found for ID {}", compositeId);
            }
            return entity;
        } catch (Exception ex) {
            logger.error("Error fetching temp data for ID {}: {}", compositeId, ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch temp data for ID: " + compositeId);
        }
    }

    public void deleteById(String compositeId) {
        try {
            if (uiupiTempRepo.existsById(compositeId)) {
                uiupiTempRepo.deleteById(compositeId);
                logger.info("Successfully deleted entity with ID: {}", compositeId);
            } else {
                logger.warn("No entity found to delete with ID: {}", compositeId);
                throw new RuntimeException("Entity not found with ID: " + compositeId);
            }
        } catch (Exception ex) {
            logger.error("Error deleting entity with ID {}: {}", compositeId, ex.getMessage(), ex);
            throw new RuntimeException("Failed to delete entity with ID: " + compositeId, ex);
        }
    }

}
