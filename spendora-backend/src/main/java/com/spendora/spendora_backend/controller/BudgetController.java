package com.spendora.spendora_backend.controller;

import com.spendora.spendora_backend.dto.ApiResponse;
import com.spendora.spendora_backend.dto.BudgetRequest;
import com.spendora.spendora_backend.dto.BudgetResponse;
import com.spendora.spendora_backend.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    /** POST /api/v1/budgets — Set a budget for a category */
    @PostMapping
    public ResponseEntity<ApiResponse<BudgetResponse>> create(
            @Valid @RequestBody BudgetRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(budgetService.create(req), "Budget created"));
    }

    /** GET /api/v1/budgets?month=3&year=2026 — Budgets for a given month */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getByMonth(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getMonthValue()}") int month,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}")      int year) {

        // fallback: use current month/year if not provided
        if (month == 0) { month = LocalDate.now().getMonthValue(); }
        if (year  == 0) { year  = LocalDate.now().getYear(); }

        return ResponseEntity.ok(ApiResponse.success(budgetService.getByMonth(month, year), "Budgets fetched"));
    }

    /** GET /api/v1/budgets/{id} — Single budget with live spent amount */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BudgetResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(budgetService.getById(id), "Budget fetched"));
    }

    /** PUT /api/v1/budgets/{id} — Update budget limit */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BudgetResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest req) {
        return ResponseEntity.ok(ApiResponse.success(budgetService.update(id, req), "Budget updated"));
    }

    /** DELETE /api/v1/budgets/{id} — Remove a budget */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        budgetService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Budget deleted"));
    }
}
