package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractMedicineService {

    protected final ObjectMapper objectMapper;

    public AbstractMedicineService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    protected JsonNode getResponseData(String url, RestTemplate restTemplate) throws Exception {
        String responseString = restTemplate.getForObject(url, String.class);
        JsonNode rootNode = objectMapper.readTree(responseString);
        return rootNode.path("data");
    }

    // 기타 공통 메서드 추가 가능
}
