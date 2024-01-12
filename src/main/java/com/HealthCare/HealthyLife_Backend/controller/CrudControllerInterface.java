package com.HealthCare.HealthyLife_Backend.controller;

import org.springframework.http.ResponseEntity;
import java.util.List;
public interface CrudControllerInterface<T, ID> {
    ResponseEntity<List<T>> findAll();
    ResponseEntity<T> findById(ID id);
    ResponseEntity<T> create(T dto);
    ResponseEntity<T> update(ID id, T dto);
    ResponseEntity<Void> delete(ID id);

}
