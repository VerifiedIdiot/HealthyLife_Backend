package com.HealthCare.HealthyLife_Backend.Interface;

import com.HealthCare.HealthyLife_Backend.dto.ElasticsearchDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CrudControllerInterface<T, ID> {




    ResponseEntity<List<T>> findAll();
    ResponseEntity<T> findById(ID id);
    ResponseEntity<?> insert();
    ResponseEntity<T> update(ID id, T dto);
    ResponseEntity<Void> delete(ID id);
}
