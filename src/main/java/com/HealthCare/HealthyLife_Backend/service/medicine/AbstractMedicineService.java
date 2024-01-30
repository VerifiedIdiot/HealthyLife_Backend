package com.HealthCare.HealthyLife_Backend.service.medicine;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class AbstractMedicineService {


    protected final RestTemplate restTemplate;

    public AbstractMedicineService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected <T> T makeApiCall(String url, HttpMethod method, Class<T> responseType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    method,
                    entity,
                    responseType
            );

            return response.getBody();
        } catch (Exception e) {
            System.out.println("API 호출 실패: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
