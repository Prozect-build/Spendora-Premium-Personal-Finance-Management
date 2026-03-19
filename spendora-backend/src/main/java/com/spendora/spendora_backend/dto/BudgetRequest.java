package com.spendora.spendora_backend.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class BudgetRequest {

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Limit amount is required")
    @DecimalMin(value = "1.00", message = "Limit must be at least ₹1")
    private BigDecimal limitAmount;

    @NotNull(message = "Month is required")
    @Min(value = 1) @Max(value = 12)
    private Integer month;

    @NotNull(message = "Year is required")
    @Min(value = 2020)
    private Integer year;

    public BudgetRequest() {}

    public Long getCategoryId()             { return categoryId; }
    public void setCategoryId(Long c)       { this.categoryId = c; }

    public BigDecimal getLimitAmount()      { return limitAmount; }
    public void setLimitAmount(BigDecimal l){ this.limitAmount = l; }

    public Integer getMonth()               { return month; }
    public void setMonth(Integer m)         { this.month = m; }

    public Integer getYear()                { return year; }
    public void setYear(Integer y)          { this.year = y; }
}
