package com.HealthCare.HealthyLife_Backend.controller;


import com.HealthCare.HealthyLife_Backend.dto.CommunityCategoryDto;
import com.HealthCare.HealthyLife_Backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // 카테고리 등록
    @PostMapping("/new")
    public ResponseEntity<Boolean> categoryRegister(@RequestBody CommunityCategoryDto communityCategoryDto) {
        boolean isTrue = categoryService.saveCategory(communityCategoryDto);
        return ResponseEntity.ok(true);
    }

    // 카테고리 수정
    @PutMapping("/modify/{id}")
    public ResponseEntity<Boolean> categoryModify(@PathVariable Long id, @RequestBody CommunityCategoryDto communityCategoryDto) {
        boolean isTrue = categoryService.modifyCategory(id, communityCategoryDto);
        return ResponseEntity.ok(true);
    }

    // 카테고리 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> categoryDelete(@PathVariable Long id) {
        boolean isTrue = categoryService.deleteCategory(id);
        return ResponseEntity.ok(true);
    }

    // 카테고리 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<CommunityCategoryDto>> categoryList() {
        List<CommunityCategoryDto> list = categoryService.getCategoryList();
        return ResponseEntity.ok(list);
    }
}
