//package com.HealthCare.HealthyLife_Backend.service;
//
//import com.HealthCare.HealthyLife_Backend.dto.MedicineDto;
//import com.HealthCare.HealthyLife_Backend.repository.MedicineRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class MedicineService {
//
//    private final MedicineRepository medicineRepository;
//    private final RestTemplate restTemplate;
//
//    @Value("${get.medicineCode.url}")
//    String medicineCodeUrl;
//
//    @Value("${get.medicineList.url}")
//    String medicineListUrl;
//
//    public MedicineService(MedicineRepository medicineRepository, RestTemplate restTemplate) {
//        this.medicineRepository = medicineRepository;
//        this.restTemplate = restTemplate;
//    }
//
//
//    // 건강기능식품 정보 포털 백엔드에서 각 건강식품의 효능과 효능의 ID값을 얻어오는 서비스 메서드
//    public Map<String, String> getFunctionalities() {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            // API URL을 설정합니다.
//            String requestUrl = medicineCodeUrl;
//
//            // RestTemplate을 사용하여 API 요청을 수행하고 응답을 받습니다.
//            ResponseEntity<MedicineDto.FunctionalityCodes[]> responseEntity = restTemplate.exchange(
//                    requestUrl,
//                    HttpMethod.GET,
//                    entity,
//                    MedicineDto.FunctionalityCodes[].class // 이너 클래스로 응답을 받습니다.
//            );
//
//            // API 결과값 중에서 BODY 내용만 추출합니다.
//            MedicineDto.FunctionalityCodes[] response = responseEntity.getBody();
//
//            // id와 functionality를 매핑하는 Map을 생성합니다.
//            Map<String, String> functionalities = new HashMap<>();
//
//            if (response != null) {
//                for (MedicineDto.FunctionalityCodes code : response) {
//                    functionalities.put(code.getId(), code.getFunctionality());
//                }
//            }
//
//            return functionalities;
//
//        } catch (Exception e) {
//            System.out.println("효능코드 가져오기 실패: " + e.getMessage());
//            e.printStackTrace();
//            return Collections.emptyMap();
//        }
//    }
//
//
//    public Map<String, String> getMedicineList() {
//
//        Map<String, String> MedicineList = new HashMap<>();
//        return MedicineList;
//    }
//
//
//
//
//
//
//
//    //    public void insert() {
////        HttpHeaders headers = new HttpHeaders();
////        // 사용자의 API 키를 헤더에 설정합니다. (API 키는 문자열 변수 `apiKey`로 가정합니다.)
//////        headers.set("X-Api-Key", apiKey);
////        // 컨텐츠 타입을 JSON으로 설정합니다.
////        headers.setContentType(MediaType.APPLICATION_JSON);
////
////        // 헤더를 포함한 HTTP 엔티티를 생성합니다.
////        HttpEntity<String> entity = new HttpEntity<>(headers);
////        // id 값이 1인 레코드를 찾는 리퍼지토리
////
////        // 개 짖음의 강도가 1에서 5까지 있다고 가정하고, 각각의 강도에 대한 정보를 가져오는 반복문을 시작합니다.
////        for (int barking = 1; barking <= 5; barking++) {
////            // 더 많은 데이터가 있는지 확인하는 데 사용되는 플래그입니다.
////            boolean moreData = true;
////            // API 페이지네이션을 위한 오프셋 초기화입니다.
////            int offset = 0;
////
////            // 데이터가 더 있는 동안 계속 API 호출을 하기 위한 while 루프입니다.
////            while (moreData) {
////                // 요청 URL을 구성합니다. 짖음 강도와 페이지네이션을 위한 오프셋 값을 포함합니다.
////                String requestUrl = apiUrl + "?barking=" + barking + "&offset=" + offset;
////
////                // `RestTemplate`의 `exchange` 메서드를 사용하여 HTTP 요청을 실행하고, 응답을 `ResponseEntity` 객체로 받습니다.
////                ResponseEntity<DogDto[]> responseEntity = restTemplate.exchange(
////                        requestUrl, // 1. requestUrl: 요청을 보낼 URL입니다. 이 URL은 요청을 처리할 서버의 주소와 요청할 리소스의 경로, 그리고 필요한 쿼리 파라미터를 포함합니다.
////                        HttpMethod.GET, // 2. HttpMethod.GET: 사용할 HTTP 메서드를 지정합니다. 여기서는 GET 메서드를 사용하여 데이터를 조회합니다.
////                        entity, // 3. entity: `HttpEntity` 객체로, 요청에 포함할 헤더와 바디를 포함합니다. 이 경우 `headers` 객체만 설정되어 있으며, 바디는 null이거나 비어 있는 상태입니다. `HttpEntity`는 `HttpHeaders` 객체를 생성자의 인자로 받아 요청 헤더를 구성할 수 있게 해줍니다. 예를 들어, API 키나 컨텐츠 타입 등을 설정할 수 있습니다.
////                        DogDto[].class // 4. DogDto[].class: `ResponseEntity`가 반환될 때 응답 본문을 변환할 객체 타입을 지정합니다. 여기서는 `DogDto` 타입의 배열을 나타내는 `DogDto[].class`를 사용하여, JSON 응답이 `DogDto[]` 타입의 객체 배열로 변환되도록 지정합니다. `exchange` 메서드는 제네릭 타입으로 `ResponseEntity<T>`를 반환하므로, 이 타입 파라미터에 대응하는 클래스 객체를 넘겨줌으로써 응답 본문의 변환을 지시합니다.
////                );
////                // API 결과값 중에서 BODY 내용만 추출합니다.
////                DogDto[] response = responseEntity.getBody();
////
////                // 응답이 null이거나 빈 배열인 경우 더 이상 처리할 데이터가 없으므로 플래그를 false로 설정
////                if (response == null || response.length == 0) {
////                    moreData = false;
////                } else {
////                    // 응답 배열을 순회하면서 각 `DogDto` 객체를 `Dog` 엔티티로 변환하고 저장
////                    for (DogDto dogDto : response) {
////                        DogDto korDogDto = engToKorService.dogToKor(dogDto);
////                        Dog dog = mapToDogEntity(korDogDto, dogType);
////                        // 변환된 Dog 엔티티를 리포지토리를 통해 데이터베이스에 저장
////                        dogRepository.save(dog);
////                        // 저장된 강아지 정보를 콘솔에 출력합니다.
////                        System.out.println("doggy!! : " + dog);
////                    }
////                    // 오프셋을 증가시켜 다음 페이지의 데이터를 요청
////                    offset += response.length;
////                }
////            }
////        }
////    }
//
//
//}
