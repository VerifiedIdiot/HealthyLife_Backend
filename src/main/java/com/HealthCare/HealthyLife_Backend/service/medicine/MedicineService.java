package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineDto;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
// ConditionalOnProperty는 프로퍼티에 elasticsearch.enabled의 값이 true 일때만 실행하라고 스프링에 명시

@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
@Service
public class MedicineService extends AbstractMedicineService {
    @Value("${get.medicineCode.url}")
    String medicineCodeUrl;

    @Value("${get.medicineList.url}")
    String medicineListUrl;
    public MedicineService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    // 건강기능식품 정보 포털 백엔드에서 각 건강식품의 효능과 효능의 ID값을 얻어오는 서비스 메서드
    public Map<String, String> getCodes() {
        MedicineDto.FunctionalityCodes[] response = makeApiCall(
                medicineCodeUrl,
                HttpMethod.GET,
                MedicineDto.FunctionalityCodes[].class);

        System.out.println(Arrays.toString(response));

        Map<String, String> functionalities = new HashMap<>();
        if (response != null) {
            for (MedicineDto.FunctionalityCodes code : response) {
                functionalities.put(code.getId(), code.getFunctionality());
            }
        }
        return functionalities;
    }

    // 외부 API에서 모든 의약품 정보를 가져오는 메서드
//    public List<MedicineDto> getMedicineList() {
//        List<MedicineDto> allMedicines = new ArrayList<>();
//        int page = 1;
//        int limit = 100;
//        boolean dataAvailable = true;
//
//        while (dataAvailable) {
//            String requestUrl = String.format("%s?page=%d&limit=%d", medicineListUrl, page, limit);
//            ResponseEntity<List<MedicineDto>> response = makeApiCall(
//                    requestUrl,
//                    HttpMethod.GET,
//                    MedicineDto.FunctionalityCodes[].class
//            );
//
//            List<MedicineDto> currentPageData = response != null ? response.getBody() : null;
//            if (currentPageData != null && !currentPageData.isEmpty()) {
//                allMedicines.addAll(currentPageData);
//                page++;
//            } else {
//                dataAvailable = false;
//            }
//        }
//        return allMedicines;
//    }
}
