package com.HealthCare.HealthyLife_Backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class Scheduler {





//    @PostConstruct
//    public void init() {
//        // 서비스 시작 시 한 번 실행할 작업
//        try {
//            executeWeatherTasks();
////            executeStrayTasks();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            // 다른 예외에 대한 처리
//            e.printStackTrace();
//        }
//    }


    // 초 분 시 일 월 요일
    @Scheduled(cron = "0 0 6 * * ?") // 매일 아침 6시에 실행
    public void executeWeatherTasks() throws JsonProcessingException {
        try {
            System.out.println("날씨 스케쥴러 시작 ! ! ! !");

            System.out.println("날씨 정보 insert 작동 ! ! ! ! !");
        } catch (ResourceAccessException e) {
            // 로그에 예외 정보 기록
            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

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
