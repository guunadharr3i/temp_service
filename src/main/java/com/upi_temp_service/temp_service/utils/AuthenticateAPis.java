package com.upi_temp_service.temp_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticateAPis {

    @Value("${refreshTokenAuth.url}")
    private String authenticationUrl;

    private static final Logger logger = LogManager.getLogger(AuthenticateAPis.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public synchronized String validateAndRefreshToken(Map<String, String> tokenRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(tokenRequest, headers);
            logger.debug("Calling authentication service at URL: {}", authenticationUrl);

            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(authenticationUrl, requestEntity,
                    String.class);

            if (tokenResponse.getStatusCode().is2xxSuccessful()) {
                logger.info("Token refresh successful, status: {}", tokenResponse.getStatusCodeValue());

                String tokenJson = tokenResponse.getBody();
                return objectMapper.readTree(tokenJson).get("accessToken").asText();
            } else {
                logger.warn("Token refresh failed, status code: {}", tokenResponse.getStatusCodeValue());
                return null;
            }

        } catch (HttpClientErrorException ex) {
            logger.error("HTTP client error during token refresh: {}, response: {}", ex.getStatusCode(),
                    ex.getResponseBodyAsString(), ex);
            return null;
        } catch (RestClientException ex) {
            logger.error("RestClientException during token refresh: {}", ex.getMessage(), ex);
            return null;
        } catch (Exception ex) {
            logger.error("Unexpected error during token refresh: {}", ex.getMessage(), ex);
            return null;
        }
    }

}
