package com.spendora.spendora_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseResponse {

    private Long id;
    private String title;
    private BigDecimal amount;
    private LocalDate date;
    private String type;
    private String note;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private String categoryColor;
    private LocalDateTime createdAt;

    public ExpenseResponse() {}

    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }

    public String getTitle()                 { return title; }
    public void setTitle(String t)           { this.title = t; }

    public BigDecimal getAmount()            { return amount; }
    public void setAmount(BigDecimal a)      { this.amount = a; }

    public LocalDate getDate()               { return date; }
    public void setDate(LocalDate d)         { this.date = d; }

    public String getType()                  { return type; }
    public void setType(String t)            { this.type = t; }

    public String getNote()                  { return note; }
    public void setNote(String n)            { this.note = n; }

    public Long getCategoryId()              { return categoryId; }
    public void setCategoryId(Long c)        { this.categoryId = c; }

    public String getCategoryName()          { return categoryName; }
    public void setCategoryName(String n)    { this.categoryName = n; }

    public String getCategoryIcon()          { return categoryIcon; }
    public void setCategoryIcon(String i)    { this.categoryIcon = i; }

    public String getCategoryColor()         { return categoryColor; }
    public void setCategoryColor(String c)   { this.categoryColor = c; }

    public LocalDateTime getCreatedAt()      { return createdAt; }
    public void setCreatedAt(LocalDateTime t){ this.createdAt = t; }
}
