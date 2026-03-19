package com.spendora.spendora_backend.controller;

import com.spendora.spendora_backend.dto.ApiResponse;
import com.spendora.spendora_backend.dto.ExpenseRequest;
import com.spendora.spendora_backend.dto.ExpenseResponse;
import com.spendora.spendora_backend.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /** POST /api/v1/transactions — Create a new income or expense */
    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> create(
            @Valid @RequestBody ExpenseRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(expenseService.create(req), "Transaction added successfully"));
    }

    /** GET /api/v1/transactions — Get all transactions for the logged-in user
     *  Optional query params: from=2026-03-01&to=2026-03-31&type=EXPENSE
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String type) {

        List<ExpenseResponse> data;
        if (from != null && to != null && type != null) {
            data = expenseService.getByTypeAndRange(type, from, to);
        } else if (from != null && to != null) {
            data = expenseService.getByRange(from, to);
        } else {
            data = expenseService.getAll();
        }
        return ResponseEntity.ok(ApiResponse.success(data, "Transactions fetched"));
    }

    /** GET /api/v1/transactions/{id} — Get single transaction */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.getById(id), "Transaction fetched"));
    }

    /** PUT /api/v1/transactions/{id} — Update a transaction */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest req) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.update(id, req), "Transaction updated"));
    }

    /** DELETE /api/v1/transactions/{id} — Delete a transaction */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Transaction deleted"));
    }
}
