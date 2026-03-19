package com.spendora.spendora_backend.controller;

import com.spendora.spendora_backend.dto.ApiResponse;
import com.spendora.spendora_backend.entity.Category;
import com.spendora.spendora_backend.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /** GET /api/v1/categories — All categories (for dropdown menus) */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(categoryRepository.findAll(), "Categories fetched"));
    }

    /** GET /api/v1/categories?type=EXPENSE — Filter by type */
    @GetMapping(params = "type")
    public ResponseEntity<ApiResponse<List<Category>>> getByType(@RequestParam String type) {
        List<Category> cats = categoryRepository.findAll().stream()
                .filter(c -> c.getType().equalsIgnoreCase(type) || c.getType().equalsIgnoreCase("BOTH"))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(cats, "Categories fetched"));
    }
}
