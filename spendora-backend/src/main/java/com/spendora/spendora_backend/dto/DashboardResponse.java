package com.spendora.spendora_backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netSavings;
    private BigDecimal savingsRate;        // percentage
    private int month;
    private int year;

    /* Cashflow – monthly breakdown for the year */
    private List<MonthlyFlow> cashflowByMonth;

    /* Expense breakdown by category */
    private List<CategoryBreakdown> expenseByCategory;

    /* Quick stats */
    private BigDecimal avgDailySpend;
    private String highestSpendDay;        // "2026-03-12"
    private BigDecimal highestSpendAmount;
    private String topExpenseTitle;
    private BigDecimal topExpenseAmount;

    /* Recent 10 transactions */
    private List<ExpenseResponse> recentTransactions;

    public DashboardResponse() {}

    // ── getters / setters ──────────────────────────────

    public BigDecimal getTotalIncome()              { return totalIncome; }
    public void setTotalIncome(BigDecimal v)        { this.totalIncome = v; }

    public BigDecimal getTotalExpense()             { return totalExpense; }
    public void setTotalExpense(BigDecimal v)       { this.totalExpense = v; }

    public BigDecimal getNetSavings()               { return netSavings; }
    public void setNetSavings(BigDecimal v)         { this.netSavings = v; }

    public BigDecimal getSavingsRate()              { return savingsRate; }
    public void setSavingsRate(BigDecimal v)        { this.savingsRate = v; }

    public int getMonth()                           { return month; }
    public void setMonth(int v)                     { this.month = v; }

    public int getYear()                            { return year; }
    public void setYear(int v)                      { this.year = v; }

    public List<MonthlyFlow> getCashflowByMonth()    { return cashflowByMonth; }
    public void setCashflowByMonth(List<MonthlyFlow> v) { this.cashflowByMonth = v; }

    public List<CategoryBreakdown> getExpenseByCategory() { return expenseByCategory; }
    public void setExpenseByCategory(List<CategoryBreakdown> v) { this.expenseByCategory = v; }

    public BigDecimal getAvgDailySpend()            { return avgDailySpend; }
    public void setAvgDailySpend(BigDecimal v)      { this.avgDailySpend = v; }

    public String getHighestSpendDay()              { return highestSpendDay; }
    public void setHighestSpendDay(String v)        { this.highestSpendDay = v; }

    public BigDecimal getHighestSpendAmount()       { return highestSpendAmount; }
    public void setHighestSpendAmount(BigDecimal v) { this.highestSpendAmount = v; }

    public String getTopExpenseTitle()              { return topExpenseTitle; }
    public void setTopExpenseTitle(String v)        { this.topExpenseTitle = v; }

    public BigDecimal getTopExpenseAmount()         { return topExpenseAmount; }
    public void setTopExpenseAmount(BigDecimal v)   { this.topExpenseAmount = v; }

    public List<ExpenseResponse> getRecentTransactions() { return recentTransactions; }
    public void setRecentTransactions(List<ExpenseResponse> v) { this.recentTransactions = v; }

    // ── Nested static classes ──────────────────────────

    public static class MonthlyFlow {
        private String month;   // "Jan", "Feb" …
        private BigDecimal income;
        private BigDecimal expense;

        public MonthlyFlow() {}
        public MonthlyFlow(String month, BigDecimal income, BigDecimal expense) {
            this.month = month; this.income = income; this.expense = expense;
        }

        public String getMonth()               { return month; }
        public void setMonth(String v)         { this.month = v; }
        public BigDecimal getIncome()          { return income; }
        public void setIncome(BigDecimal v)    { this.income = v; }
        public BigDecimal getExpense()         { return expense; }
        public void setExpense(BigDecimal v)   { this.expense = v; }
    }

    public static class CategoryBreakdown {
        private String categoryName;
        private String categoryColor;
        private BigDecimal amount;
        private double percent;

        public CategoryBreakdown() {}

        public String getCategoryName()           { return categoryName; }
        public void setCategoryName(String v)     { this.categoryName = v; }
        public String getCategoryColor()          { return categoryColor; }
        public void setCategoryColor(String v)    { this.categoryColor = v; }
        public BigDecimal getAmount()             { return amount; }
        public void setAmount(BigDecimal v)       { this.amount = v; }
        public double getPercent()                { return percent; }
        public void setPercent(double v)          { this.percent = v; }
    }
}
