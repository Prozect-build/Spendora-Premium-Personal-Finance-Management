package com.spendora.spendora_backend.service;

import com.spendora.spendora_backend.dto.BudgetRequest;
import com.spendora.spendora_backend.dto.BudgetResponse;
import com.spendora.spendora_backend.entity.Budget;
import com.spendora.spendora_backend.entity.Category;
import com.spendora.spendora_backend.entity.User;
import com.spendora.spendora_backend.repository.BudgetRepository;
import com.spendora.spendora_backend.repository.CategoryRepository;
import com.spendora.spendora_backend.repository.ExpenseRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetService(BudgetRepository budgetRepository,
                         CategoryRepository categoryRepository,
                         ExpenseRepository expenseRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.expenseRepository = expenseRepository;
    }

    private User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /* ── CREATE ── */
    @Transactional
    public BudgetResponse create(BudgetRequest req) {
        User user = currentUser();

        if (budgetRepository.existsByUserAndCategoryIdAndMonthAndYear(
                user, req.getCategoryId(), req.getMonth(), req.getYear())) {
            throw new IllegalArgumentException(
                "Budget already exists for this category and month. Use UPDATE instead.");
        }

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.getCategoryId()));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category);
        budget.setLimitAmount(req.getLimitAmount());
        budget.setMonth(req.getMonth());
        budget.setYear(req.getYear());

        return toResponse(budgetRepository.save(budget));
    }

    /* ── READ — all budgets for a month ── */
    public List<BudgetResponse> getByMonth(int month, int year) {
        return budgetRepository.findByUserAndMonthAndYear(currentUser(), month, year)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /* ── GET ONE ── */
    public BudgetResponse getById(Long id) {
        return toResponse(findOwned(id));
    }

    /* ── UPDATE ── */
    @Transactional
    public BudgetResponse update(Long id, BudgetRequest req) {
        Budget budget = findOwned(id);

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.getCategoryId()));

        budget.setCategory(category);
        budget.setLimitAmount(req.getLimitAmount());
        budget.setMonth(req.getMonth());
        budget.setYear(req.getYear());

        return toResponse(budgetRepository.save(budget));
    }

    /* ── DELETE ── */
    @Transactional
    public void delete(Long id) {
        budgetRepository.delete(findOwned(id));
    }

    /* ── HELPERS ── */
    private Budget findOwned(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found: " + id));
        if (!budget.getUser().getId().equals(currentUser().getId())) {
            throw new SecurityException("Access denied");
        }
        return budget;
    }

    public BudgetResponse toResponse(Budget b) {
        User user = currentUser();
        BigDecimal spent = expenseRepository.sumExpenseByCategoryAndMonth(
                user, b.getCategory().getId(), b.getMonth(), b.getYear());

        if (spent == null) { spent = BigDecimal.ZERO; }
        BigDecimal remaining = b.getLimitAmount().subtract(spent);
        double usedPct = b.getLimitAmount().compareTo(BigDecimal.ZERO) == 0 ? 0
                : spent.multiply(BigDecimal.valueOf(100))
                       .divide(b.getLimitAmount(), 2, RoundingMode.HALF_UP)
                       .doubleValue();

        BudgetResponse res = new BudgetResponse();
        res.setId(b.getId());
        res.setCategoryId(b.getCategory().getId());
        res.setCategoryName(b.getCategory().getName());
        res.setCategoryIcon(b.getCategory().getIcon());
        res.setCategoryColor(b.getCategory().getColor());
        res.setLimitAmount(b.getLimitAmount());
        res.setSpentAmount(spent);
        res.setRemaining(remaining);
        res.setUsedPercent(usedPct);
        res.setMonth(b.getMonth());
        res.setYear(b.getYear());
        return res;
    }
}
