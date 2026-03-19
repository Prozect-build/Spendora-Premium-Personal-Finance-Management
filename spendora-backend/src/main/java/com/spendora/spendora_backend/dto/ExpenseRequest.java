package com.spendora.spendora_backend.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Type is required (INCOME or EXPENSE)")
    @Pattern(regexp = "INCOME|EXPENSE", message = "Type must be INCOME or EXPENSE")
    private String type;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String note;

    public ExpenseRequest() {}

    public String getTitle()            { return title; }
    public void setTitle(String t)      { this.title = t; }

    public BigDecimal getAmount()             { return amount; }
    public void setAmount(BigDecimal a)       { this.amount = a; }

    public LocalDate getDate()               { return date; }
    public void setDate(LocalDate d)         { this.date = d; }

    public String getType()             { return type; }
    public void setType(String t)       { this.type = t; }

    public Long getCategoryId()         { return categoryId; }
    public void setCategoryId(Long c)   { this.categoryId = c; }

    public String getNote()             { return note; }
    public void setNote(String n)       { this.note = n; }
}
