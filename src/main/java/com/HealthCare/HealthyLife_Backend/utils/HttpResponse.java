package com.HealthCare.HealthyLife_Backend.utils;

import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

public class HttpResponse {

    // 성공적인 응답 (200 OK)을 생성하는 메소드
    public static <T> ResponseEntity<T> okResponse(T data) {
        return ResponseEntity.ok(data);
    }

    // 데이터가 비어있는 경우 (204 No Content) 응답을 생성하는 메소드
    public static <T> ResponseEntity<T> noContentResponse() {
        return ResponseEntity.noContent().build();
    }

    // 데이터가 없는 경우 (404 Not Found) 응답을 생성하는 메소드
    public static <T> ResponseEntity<T> notFoundResponse() {
        return ResponseEntity.notFound().build();
    }

    // 서버 오류 (500 Internal Server Error) 응답을 생성하는 메소드
    public static ResponseEntity<String> serverErrorResponse() {

        return ResponseEntity.internalServerError().body("500 Internal Server Error");
    }

    // List 데이터를 처리하는 유틸리티 메소드
    public static <T> ResponseEntity<List<T>> responseForList(List<T> list) {
        return list.isEmpty() ? noContentResponse() : okResponse(list);
    }

    // Optional 데이터를 처리하는 유틸리티 메소드
    public static <T> ResponseEntity<T> responseForOptional(Optional<T> optional) {
        return optional.map(HttpResponse::okResponse)
                .orElseGet(HttpResponse::notFoundResponse);
    }
}
