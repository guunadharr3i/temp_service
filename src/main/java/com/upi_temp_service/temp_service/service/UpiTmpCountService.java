package com.upi_temp_service.temp_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upi_temp_service.temp_service.repo.UpiTmpCountRepo;
import java.util.List;
import java.util.Map;

@Service
public class UpiTmpCountService {

    private final UpiTmpCountRepo repository;

    @Autowired
    public UpiTmpCountService(UpiTmpCountRepo repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> getCounts(String userId, List<String> tableIds, Boolean filterByCurrentUser) {
        return repository.getCounts(userId, tableIds, filterByCurrentUser);
    }
}
