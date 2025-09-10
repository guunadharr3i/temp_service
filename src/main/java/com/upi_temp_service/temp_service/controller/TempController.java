package com.upi_temp_service.temp_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import com.upi_temp_service.temp_service.Entity.UIUPITmpEntity;
import com.upi_temp_service.temp_service.pojo.ApproveRejectAuditModel;
import com.upi_temp_service.temp_service.pojo.DataTablePayloadModel;
import com.upi_temp_service.temp_service.pojo.FilterModel;
import com.upi_temp_service.temp_service.pojo.IdRequest;
import com.upi_temp_service.temp_service.pojo.ResponsePojo;
import com.upi_temp_service.temp_service.pojo.UIUPITempModel;
import com.upi_temp_service.temp_service.pojo.UpiTempCountRequest;
import com.upi_temp_service.temp_service.service.ActionAuditService;
import com.upi_temp_service.temp_service.service.UpiTempService;
import com.upi_temp_service.temp_service.service.UpiTmpCountService;
import com.upi_temp_service.temp_service.utils.AuthenticateAPis;
import com.upi_temp_service.temp_service.utils.ErrorCode;
import com.upi_temp_service.temp_service.utils.ErrorResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping()
public class TempController {
    private static final Logger logger = LogManager.getLogger(TempController.class);

    @Autowired
    AuthenticateAPis authenticateAPis;

    @Autowired
    UpiTmpCountService upiTmpCountService;

    @Autowired
    UpiTempService upiTempTableService;

    @Autowired
    private UpiTempService tempService;

    @Autowired
    private ActionAuditService actionAuditService;

    @GetMapping()
    public String initialCallApi() {
        return "Hello developer Welcome";
    }

    @PostMapping("/tempTableData")
    public ResponseEntity<?> getTempTableData(@RequestBody DataTablePayloadModel payload,
            @RequestHeader("Authorization") String token,
            @RequestHeader("DeviceHash") String deviceHash) {

        logger.info("Received request for /temp/tableData with payload: {}", payload);

        Map<String, String> tokenValidation = new HashMap<>();
        tokenValidation.put("token", token);
        tokenValidation.put("deviceHash", deviceHash);
        logger.info("before accessTokenCalled");
        String accessToken =  authenticateAPis.validateAndRefreshToken(tokenValidation);
        logger.info("after accessTokenCalled");

        if (accessToken == null) {
            return ResponseEntity.badRequest().body(ErrorCode.TOKEN_INVALID);
        }

        try {
            String tempTableName = payload.getTempTableName();
            int limit = payload.getLimit();
            int offset = payload.getOffset();
            String sortColumn = payload.getSort() != null ? payload.getSort().getColumn() : null;
            String sortDirection = payload.getSort() != null ? payload.getSort().getDirection() : null;
            List<FilterModel> filters = payload.getFilters();
            String tableCode = payload.getTableCode();
            logger.info(
                    "Extracted Params - tempTableName: {}, limit: {}, offset: {}, sortColumn: {}, sortDirection: {}, filters: {}",
                    tempTableName, limit, offset, sortColumn, sortDirection, filters);

            ResponsePojo result = upiTempTableService.getFilteredUpiTempData(
                    tableCode, limit, offset, sortColumn, sortDirection, filters, payload.getTempTableName(),
                    payload.getUserId(), payload.getFilterByCurrentUser());

            result.setToken(accessToken);
            logger.info("Successfully retrieved temp table data for tempTableName: {}", tempTableName);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            logger.error("Error occurred while processing /temp/tableData request", e);
            return ResponseEntity.badRequest().body(ErrorCode.INTERNAL_ERROR);
        }
    }

    @PostMapping("/upiTempCount")
    public ResponseEntity<?> getCounts(
            @RequestBody UpiTempCountRequest request,
            @RequestHeader("Authorization") String token,
            @RequestHeader("DeviceHash") String deviceHash) {

        String userId = request.getUserId();
        List<String> tableIds = request.getTableIds();

        logger.info("Received request to /upiTempCount with userId: {} and tableIds: {}", userId, tableIds);
        logger.debug("Authorization token: {}", token);
        logger.debug("DeviceHash: {}", deviceHash);

        try {
            List<Map<String, Object>> counts = upiTmpCountService.getCounts(userId, tableIds,
                    request.getFilterByCurrentUser());
            logger.info("Counts retrieved successfully. Result size: {}", counts.size());

            int totalCount = counts.stream()
                    .mapToInt(m -> ((Number) m.get("NAME_COUNT")).intValue())
                    .sum();
            logger.info("Total count calculated: {}", totalCount);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("counts", counts);
            response.put("totalCount", totalCount);

            logger.info("Returning successful response.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error occurred while fetching counts: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    new ErrorResponse<>(ErrorCode.INTERNAL_ERROR, e.getMessage(), null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveTempData")
    public ResponseEntity<?> saveEntity(@RequestBody UIUPITempModel model) {
        try {
            tempService.saveEntity(model);
            logger.info("Temp data saved successfully for ID: {}", model.getId());
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            logger.error("Error occurred while saving temp data for ID {}: {}", model.getId(), e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("Error saving entity: " + e.getMessage());
        }
    }

    @PostMapping("/tempTableData/exists")
    public ResponseEntity<?> checkIfExists(@RequestBody IdRequest idRequest) {
        try {
            String id = idRequest.getId();
            boolean exists = tempService.checkIfExists(id);
            logger.info("Existence check for ID {}: {}", id, exists);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            logger.error("Error occurred while checking existence for ID {}: {}", idRequest.getId(), e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body("Error checking existence: " + e.getMessage());
        }
    }

    @PostMapping("/tempTableData/get")
    public ResponseEntity<?> getTempData(@RequestBody IdRequest idRequest) {
        try {
            String id = idRequest.getId();

            Optional<UIUPITmpEntity> entity = tempService.getTempData(id);

            if (entity.isPresent()) {
                logger.info("Returning temp data for ID {}", id);

                UIUPITmpEntity e = entity.get();

                UIUPITempModel model = new UIUPITempModel(
                        e.getId(),
                        e.getRawJson(),
                        e.getCreatedBy(),
                        e.getDateTime(),
                        e.getStatus(),
                        e.getActionType(),
                        e.getTableId(),
                        e.getRecordId());

                return ResponseEntity.ok(model);
            } else {
                logger.warn("No temp data found for ID {}", id);
                return ResponseEntity.status(404).body("No temp data found for ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching temp data for ID {}: {}", idRequest.getId(), e.getMessage(), e);
            return ResponseEntity.status(500).body("Error fetching temp data: " + e.getMessage());
        }
    }

    @PostMapping("/tempTableData/delete")
    public ResponseEntity<?> deleteTempData(@RequestBody IdRequest idRequest) {
        try {
            String id = idRequest.getId();
            tempService.deleteById(id);
            logger.info("Deleted temp data with ID: {}", id);
            return ResponseEntity.ok("Entity deleted successfully with ID: " + id);
        } catch (RuntimeException ex) {
            logger.warn("Delete failed for ID {}: {}", idRequest.getId(), ex.getMessage());
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected error while deleting ID {}: {}", idRequest.getId(), ex.getMessage(), ex);
            return ResponseEntity.status(500).body("Error deleting entity: " + ex.getMessage());
        }
    }

    @PostMapping("/approveRejectAuditSave")
    public ResponseEntity<String> saveAudit(@RequestBody ApproveRejectAuditModel request) {
        try {
            actionAuditService.saveAudit(
                    request);

            return ResponseEntity.ok("Audit saved successfully");
        } catch (Exception e) {
            logger.error("Audit log issue", e.getMessage());

            return ResponseEntity.ok(e.getMessage());
        }
    }

}
