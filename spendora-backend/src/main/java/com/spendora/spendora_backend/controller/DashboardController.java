package com.spendora.spendora_backend.controller;

import com.spendora.spendora_backend.dto.ApiResponse;
import com.spendora.spendora_backend.dto.DashboardResponse;
import com.spendora.spendora_backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * GET /api/v1/dashboard?month=3&year=2026
     * Returns complete dashboard analytics for the logged-in user.
     * month and year default to current month/year if not provided.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        int m = (month != null) ? month : LocalDate.now().getMonthValue();
        int y = (year  != null) ? year  : LocalDate.now().getYear();

        return ResponseEntity.ok(
                ApiResponse.success(dashboardService.getDashboard(m, y), "Dashboard data fetched"));
    }
}
