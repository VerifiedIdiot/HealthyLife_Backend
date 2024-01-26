package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.MedicineDto;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final RestTemplate restTemplate;

    @Value("${get.medicineCode.url}")
    String medicineCodeUrl;

    @Value("${get.medicineList.url}")
    String medicineListUrl;

    public MedicineService(MedicineRepository medicineRepository, RestTemplate restTemplate) {
        this.medicineRepository = medicineRepository;
        this.restTemplate = restTemplate;
    }


    // 건강기능식품 정보 포털 백엔드에서 각 건강식품의 효능과 효능의 ID값을 얻어오는 서비스 메서드
    public Map<String, String> getFunctionalities() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API URL을 설정합니다.
            String requestUrl = medicineCodeUrl;

            // RestTemplate을 사용하여 API 요청을 수행하고 응답을 받습니다.
            ResponseEntity<MedicineDto.FunctionalityCodes[]> responseEntity = restTemplate.exchange(
                    requestUrl,
                    HttpMethod.GET,
                    entity,
                    MedicineDto.FunctionalityCodes[].class // 이너 클래스로 응답을 받습니다.
            );

            // API 결과값 중에서 BODY 내용만 추출합니다.
            MedicineDto.FunctionalityCodes[] response = responseEntity.getBody();

            // id와 functionality를 매핑하는 Map을 생성합니다.
            Map<String, String> functionalities = new HashMap<>();

            if (response != null) {
                for (MedicineDto.FunctionalityCodes code : response) {
                    functionalities.put(code.getId(), code.getFunctionality());
                }
            }

            return functionalities;

        } catch (Exception e) {
            System.out.println("효능코드 가져오기 실패: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }


    public List<MedicineDto> getMedicineList() {
        List<MedicineDto> allMedicines = new ArrayList<>();
        int page = 1;
        int limit = 100;
        boolean dataAvailable = true;

        while (dataAvailable) {
            String requestUrl = String.format("%s?page=%d&limit=%d", medicineListUrl, page, limit);

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<List<MedicineDto>> response = restTemplate.exchange(
                        requestUrl,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<MedicineDto>>() {}
                );

                List<MedicineDto> currentPageData = response.getBody();
                if (currentPageData != null && !currentPageData.isEmpty()) {
                    allMedicines.addAll(currentPageData);
                    page++;
                } else {
                    dataAvailable = false;
                }
            } catch (Exception e) {
                System.out.println("의약품 목록 가져오기 실패: " + e.getMessage());
                e.printStackTrace();
                dataAvailable = false;
            }
        }

        return allMedicines;
    }

}
