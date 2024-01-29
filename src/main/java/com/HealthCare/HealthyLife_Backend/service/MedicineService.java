package com.HealthCare.HealthyLife_Backend.service;

import com.HealthCare.HealthyLife_Backend.dto.MedicineDto;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
// ConditionalOnProperty는 프로퍼티에 elasticsearch.enabled의 값이 true 일때만 실행하라고 스프링에 명시
@Service
@ConditionalOnProperty(name = "elasticsearch.enabled", havingValue = "true")
public class MedicineService {

    private final Optional<MedicineRepository> medicineRepository;
    private final RestTemplate restTemplate;

    @Value("${get.medicineCode.url}")
    String medicineCodeUrl;

    @Value("${get.medicineList.url}")
    String medicineListUrl;

    public MedicineService(Optional<MedicineRepository> medicineRepository, RestTemplate restTemplate) {
        this.medicineRepository = medicineRepository;
        this.restTemplate = restTemplate;
    }

    // 예제 메서드: Elasticsearch 관련 작업
    public void someElasticsearchOperation() {
        // Elasticsearch 서버가 가용한 경우에만 작업을 수행
        if (medicineRepository.isPresent()) {
            // MedicineRepository를 사용한 작업 수행
            // 예: medicineRepository.get().save(...);
        } else {
            // Elasticsearch 서버가 가용하지 않을 때의 처리
            System.out.println("Elasticsearch 서버가 가용하지 않습니다.");
        }
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
