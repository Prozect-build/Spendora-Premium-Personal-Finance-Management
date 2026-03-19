package com.spendora.spendora_backend.service;

import com.spendora.spendora_backend.dto.DashboardResponse;
import com.spendora.spendora_backend.dto.DashboardResponse.CategoryBreakdown;
import com.spendora.spendora_backend.dto.DashboardResponse.MonthlyFlow;
import com.spendora.spendora_backend.entity.User;
import com.spendora.spendora_backend.repository.ExpenseRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private static final String[] MONTH_NAMES =
        {"", "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    private final ExpenseRepository expenseRepository;
    private final ExpenseService expenseService;

    public DashboardService(ExpenseRepository expenseRepository, ExpenseService expenseService) {
        this.expenseRepository = expenseRepository;
        this.expenseService    = expenseService;
    }

    private User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public DashboardResponse getDashboard(int month, int year) {
        User user = currentUser();
        DashboardResponse res = new DashboardResponse();
        res.setMonth(month);
        res.setYear(year);

        // ── Income & Expense ──
        BigDecimal income  = expenseRepository.sumByUserAndTypeAndMonthAndYear(user, "INCOME",  month, year);
        BigDecimal expense = expenseRepository.sumByUserAndTypeAndMonthAndYear(user, "EXPENSE", month, year);
        if (income  == null) income  = BigDecimal.ZERO;
        if (expense == null) expense = BigDecimal.ZERO;

        BigDecimal savings = income.subtract(expense);
        BigDecimal savingsRate = income.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : savings.multiply(BigDecimal.valueOf(100)).divide(income, 2, RoundingMode.HALF_UP);

        res.setTotalIncome(income);
        res.setTotalExpense(expense);
        res.setNetSavings(savings);
        res.setSavingsRate(savingsRate);

        // ── Monthly cashflow for the current year (chart data) ──
        List<Object[]> cashflows = expenseRepository.getMonthlyCashflow(user, year);
        List<MonthlyFlow> flows  = new ArrayList<>();

        // fill all 12 months (some may have no data)
        BigDecimal[] incomeByMonth  = new BigDecimal[13];
        BigDecimal[] expenseByMonth = new BigDecimal[13];
        for (int i = 1; i <= 12; i++) {
            incomeByMonth[i]  = BigDecimal.ZERO;
            expenseByMonth[i] = BigDecimal.ZERO;
        }
        for (Object[] row : cashflows) {
            int m = ((Number) row[0]).intValue();
            incomeByMonth[m]  = row[1] == null ? BigDecimal.ZERO : (BigDecimal) row[1];
            expenseByMonth[m] = row[2] == null ? BigDecimal.ZERO : (BigDecimal) row[2];
        }
        for (int i = 1; i <= 12; i++) {
            flows.add(new MonthlyFlow(MONTH_NAMES[i], incomeByMonth[i], expenseByMonth[i]));
        }
        res.setCashflowByMonth(flows);

        // ── Category breakdown ──
        List<Object[]> catRows = expenseRepository.getExpenseBreakdownByCategory(user, month, year);
        BigDecimal totalExpenseForPct = expense.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : expense;

        List<CategoryBreakdown> breakdowns = catRows.stream().map(row -> {
            CategoryBreakdown cb = new CategoryBreakdown();
            cb.setCategoryName( (String) row[0]);
            cb.setCategoryColor((String) row[1]);
            cb.setAmount((BigDecimal) row[2]);
            double pct = cb.getAmount()
                           .multiply(BigDecimal.valueOf(100))
                           .divide(totalExpenseForPct, 1, RoundingMode.HALF_UP)
                           .doubleValue();
            cb.setPercent(pct);
            return cb;
        }).collect(Collectors.toList());
        res.setExpenseByCategory(breakdowns);

        // ── Avg daily spend ──
        int daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth();
        res.setAvgDailySpend(expense.divide(BigDecimal.valueOf(daysInMonth), 2, RoundingMode.HALF_UP));

        // ── Highest spend day ──
        List<Object[]> hsdRows = expenseRepository.getHighestSpendDay(user, month, year);
        if (!hsdRows.isEmpty()) {
            res.setHighestSpendDay(String.valueOf(hsdRows.get(0)[0]));
            res.setHighestSpendAmount((BigDecimal) hsdRows.get(0)[1]);
        }

        // ── Recent 10 transactions ──
        res.setRecentTransactions(
            expenseRepository.findTop10ByUserOrderByCreatedAtDesc(user)
                .stream().map(expenseService::toResponse).collect(Collectors.toList()));

        return res;
    }
}
