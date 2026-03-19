package com.spendora.spendora_backend.dto;

import java.math.BigDecimal;

public class BudgetResponse {

    private Long id;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private String categoryColor;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;   // computed from expenses
    private BigDecimal remaining;     // limitAmount - spentAmount
    private double usedPercent;       // 0–100
    private Integer month;
    private Integer year;

    public BudgetResponse() {}

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public Long getCategoryId()               { return categoryId; }
    public void setCategoryId(Long c)         { this.categoryId = c; }

    public String getCategoryName()           { return categoryName; }
    public void setCategoryName(String n)     { this.categoryName = n; }

    public String getCategoryIcon()           { return categoryIcon; }
    public void setCategoryIcon(String i)     { this.categoryIcon = i; }

    public String getCategoryColor()          { return categoryColor; }
    public void setCategoryColor(String c)    { this.categoryColor = c; }

    public BigDecimal getLimitAmount()        { return limitAmount; }
    public void setLimitAmount(BigDecimal l)  { this.limitAmount = l; }

    public BigDecimal getSpentAmount()        { return spentAmount; }
    public void setSpentAmount(BigDecimal s)  { this.spentAmount = s; }

    public BigDecimal getRemaining()          { return remaining; }
    public void setRemaining(BigDecimal r)    { this.remaining = r; }

    public double getUsedPercent()            { return usedPercent; }
    public void setUsedPercent(double p)      { this.usedPercent = p; }

    public Integer getMonth()                 { return month; }
    public void setMonth(Integer m)           { this.month = m; }

    public Integer getYear()                  { return year; }
    public void setYear(Integer y)            { this.year = y; }
}
