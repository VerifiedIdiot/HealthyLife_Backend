package com.HealthCare.HealthyLife_Backend.utils;

import com.HealthCare.HealthyLife_Backend.dto.medicine.MedicineCodeDto;
import com.HealthCare.HealthyLife_Backend.service.medicine.MedicineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class Scheduler {

    private final MedicineService medicineService;

    public Scheduler(MedicineService medicineService) {
        this.medicineService = medicineService;
    }


//    @PostConstruct
//    public void init() {
//        // 서비스 시작 시 한 번 실행할 작업
//        try {
//            executeMedicineCodesInsert();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            // 다른 예외에 대한 처리
//            e.printStackTrace();
//        }
//    }




    public void executeMedicineCodesInsert() throws JsonProcessingException {
        try {
            List<MedicineCodeDto> codes = medicineService.parseCodes();
            if (codes.isEmpty()) {
                System.out.println("API 요청 실패");
            }
            long startTime = System.currentTimeMillis(); // 시작 시간 기록

            medicineService.insertCodes(codes);

            long endTime = System.currentTimeMillis(); // 종료 시간 기록
            long duration = endTime - startTime; // 걸린 시간 계산
            System.out.println("Insert 완료. 총 걸린 시간: " + duration + "ms, 총 삽입된 데이터 수: " + codes.size());
        } catch (ResourceAccessException e) {
            // 로그에 예외 정보 기록
            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }
    // 초 분 시 일 월 요일
    @Scheduled(cron = "0 0 6 * * ?") // 한시간마다 실행
    public void executeStrayTasks() throws JsonProcessingException {
        try {

        } catch (ResourceAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
