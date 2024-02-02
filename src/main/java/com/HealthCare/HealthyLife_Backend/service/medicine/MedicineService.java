package com.HealthCare.HealthyLife_Backend.service.medicine;

import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineCodeDto;
import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineDto;
import com.HealthCare.HealthyLife_Backend.entity.MedicineCode;
import com.HealthCare.HealthyLife_Backend.repository.MedicineCodeRepository;
import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

// ConditionalOnProperty는 프로퍼티에 elasticsearch.enabled의 값이 true 일때만 실행하라고 스프링에 명시
@Slf4j
@Service
@Transactional
//@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class MedicineService extends AbstractMedicineService {

    private final MedicineRepository medicineRepository;

    private final MedicineCodeRepository medicineCodeRepository;
    private final RestTemplate restTemplate;

    @Value("${get.medicineCode.url}")
    private String medicineCodeUrl;

    @Value("${get.medicineList.url}")
    private String medicineListUrl;

    public MedicineService(MedicineRepository medicineRepository, MedicineCodeRepository medicineCodeRepository, RestTemplate restTemplate) {
        this.medicineRepository = medicineRepository;
        this.medicineCodeRepository = medicineCodeRepository;
        this.restTemplate = restTemplate;
    }

    public List<MedicineCodeDto> parseCodes() {
        try {
            JsonNode dataArray = getResponseData(medicineCodeUrl, restTemplate);
            if (dataArray == null || !dataArray.isArray()) {
                return Collections.emptyList();
            }

            List<MedicineCodeDto> codes = new ArrayList<>();
            for (JsonNode dataNode : dataArray) {
                MedicineCodeDto code = objectMapper.treeToValue(dataNode, MedicineCodeDto.class);
                codes.add(code);
//                System.out.println(code);
            }

            return codes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void insertCodes(List<MedicineCodeDto> codes) {
        try {
            for (MedicineCodeDto code : codes) {
                MedicineCode medicineCode = code.toEntity();
                medicineCodeRepository.save(medicineCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<MedicineCodeDto>> getCodes() {
        try {
            List<MedicineCode> allCodes = medicineCodeRepository.findAll();
            Map<String, List<MedicineCodeDto>> classifiedCodes = new HashMap<>();

            for (MedicineCode medicineCode : allCodes) {
                String type = medicineCode.getType();
                MedicineCodeDto medicineCodeDto = medicineCode.toDto();

                // 해당 type의 리스트가 이미 Map에 있으면 가져오고, 없으면 새로 생성
                // 그러니까 getType으로 할당한 type이 새로등장한다면 새로운 리스트를 생성후 저장
                // type 이 a,b,c 가 있다면 리스트를 3개 만들고 a,b,c를 key로 사용하여 해당 리스트들을 value로서 접근할 수 있다.
                List<MedicineCodeDto> medicineCodeDtos = classifiedCodes.computeIfAbsent(type, k -> new ArrayList<>());
                medicineCodeDtos.add(medicineCodeDto);
            }

            return classifiedCodes;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }




    public List<MedicineDto> parseMedicineList() {
        log.info("시작");
        List<MedicineDto> allMedicineDtos = new ArrayList<>();

        Map<String, String> codeMap = createCodeMap(parseCodes());

        try {
            int page = 1;
            while (true) {
                String url = medicineListUrl + "?page=" + page + "&limit=100";
                JsonNode dataArray = getResponseData(url, restTemplate);

                if (dataArray == null || !dataArray.isArray() || dataArray.isEmpty()) {
                    break;
                }

                for (JsonNode dataNode : dataArray) {
                    MedicineDto medicineDto = objectMapper.treeToValue(dataNode, MedicineDto.class);
                    convertFunctionalityFields(medicineDto, codeMap);
                    allMedicineDtos.add(medicineDto);
                }
                page++;
            }
        } catch (Exception e) {
            log.error("Error occurred: ", e);
        }

        return allMedicineDtos;
    }



    private Map<String, String> createCodeMap(List<MedicineCodeDto> codes) {
        Map<String, String> codeMap = new HashMap<>();
        for (MedicineCodeDto code : codes) {
            codeMap.put(code.getId(), code.getFunctionality());
            log.info("Functionality: " + code.getFunctionality());
        }
        return codeMap;
    }

    private void convertFunctionalityFields(MedicineDto dto, Map<String, String> codeMap) {
        List<String> functionalities = dto.getFunctionalities().stream()
                .map(codeMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        dto.setFunctionalities(functionalities);
    }


}
