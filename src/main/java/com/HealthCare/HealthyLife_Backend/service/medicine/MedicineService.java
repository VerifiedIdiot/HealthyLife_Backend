package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineCodeDto;
import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineDto;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
// ConditionalOnProperty는 프로퍼티에 elasticsearch.enabled의 값이 true 일때만 실행하라고 스프링에 명시
@Slf4j
@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class MedicineService extends AbstractMedicineService {

    private final MedicineRepository medicineRepository;
    private final RestTemplate restTemplate;

    @Value("${get.medicineCode.url}")
    private String medicineCodeUrl;

    @Value("${get.medicineList.url}")
    private String medicineListUrl;

    public MedicineService(MedicineRepository medicineRepository, RestTemplate restTemplate) {
        this.medicineRepository = medicineRepository;
        this.restTemplate = restTemplate;
    }

    public List<MedicineCodeDto> getCodes() {
        try {
            JsonNode dataArray = getResponseData(medicineCodeUrl, restTemplate);
            if (dataArray == null || !dataArray.isArray()) {
                return Collections.emptyList();
            }

            List<MedicineCodeDto> codes = new ArrayList<>();
            for (JsonNode dataNode : dataArray) {
                MedicineCodeDto code = objectMapper.treeToValue(dataNode, MedicineCodeDto.class);
                codes.add(code);
            }

            return codes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<MedicineDto> getMedicineList() {
        List<MedicineDto> allMedicineDtos = new ArrayList<>();

        try {
            int page = 1;
            while (true) {
                String url = medicineListUrl + "?page=" + page + "&limit=100";
                JsonNode dataArray = getResponseData(url, restTemplate);

                if (dataArray == null || !dataArray.isArray()
                        || dataArray.isEmpty() || page ==10) {
                    break;
                }

                for (JsonNode dataNode : dataArray) {
                    MedicineDto medicineDto = objectMapper.treeToValue(dataNode, MedicineDto.class);
                    allMedicineDtos.add(medicineDto);
                }

                page++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allMedicineDtos;
    }
}
