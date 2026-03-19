package com.spendora.spendora_backend.service;

import com.spendora.spendora_backend.dto.ExpenseRequest;
import com.spendora.spendora_backend.dto.ExpenseResponse;
import com.spendora.spendora_backend.entity.Category;
import com.spendora.spendora_backend.entity.Expense;
import com.spendora.spendora_backend.entity.User;
import com.spendora.spendora_backend.repository.CategoryRepository;
import com.spendora.spendora_backend.repository.ExpenseRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
                          CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    private User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /* ── CREATE ── */
    @Transactional
    public ExpenseResponse create(ExpenseRequest req) {
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.getCategoryId()));

        Expense expense = new Expense();
        expense.setTitle(req.getTitle());
        expense.setAmount(req.getAmount());
        expense.setDate(req.getDate());
        expense.setType(req.getType().toUpperCase());
        expense.setNote(req.getNote());
        expense.setUser(currentUser());
        expense.setCategory(category);

        return toResponse(expenseRepository.save(expense));
    }

    /* ── READ ALL (for logged-in user) ── */
    public List<ExpenseResponse> getAll() {
        return expenseRepository.findByUserOrderByDateDesc(currentUser())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /* ── READ FILTERED BY DATE RANGE ── */
    public List<ExpenseResponse> getByRange(LocalDate from, LocalDate to) {
        return expenseRepository
                .findByUserAndDateBetweenOrderByDateDesc(currentUser(), from, to)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /* ── READ FILTERED BY TYPE + DATE RANGE ── */
    public List<ExpenseResponse> getByTypeAndRange(String type, LocalDate from, LocalDate to) {
        return expenseRepository
                .findByUserAndTypeAndDateBetweenOrderByDateDesc(currentUser(), type.toUpperCase(), from, to)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    /* ── READ ONE ── */
    public ExpenseResponse getById(Long id) {
        Expense expense = findOwned(id);
        return toResponse(expense);
    }

    /* ── UPDATE ── */
    @Transactional
    public ExpenseResponse update(Long id, ExpenseRequest req) {
        Expense expense = findOwned(id);

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + req.getCategoryId()));

        expense.setTitle(req.getTitle());
        expense.setAmount(req.getAmount());
        expense.setDate(req.getDate());
        expense.setType(req.getType().toUpperCase());
        expense.setNote(req.getNote());
        expense.setCategory(category);

        return toResponse(expenseRepository.save(expense));
    }

    /* ── DELETE ── */
    @Transactional
    public void delete(Long id) {
        Expense expense = findOwned(id);
        expenseRepository.delete(expense);
    }

    /* ── HELPERS ── */
    private Expense findOwned(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + id));
        if (!expense.getUser().getId().equals(currentUser().getId())) {
            throw new SecurityException("Access denied");
        }
        return expense;
    }

    public ExpenseResponse toResponse(Expense e) {
        ExpenseResponse res = new ExpenseResponse();
        res.setId(e.getId());
        res.setTitle(e.getTitle());
        res.setAmount(e.getAmount());
        res.setDate(e.getDate());
        res.setType(e.getType());
        res.setNote(e.getNote());
        res.setCreatedAt(e.getCreatedAt());
        if (e.getCategory() != null) {
            res.setCategoryId(e.getCategory().getId());
            res.setCategoryName(e.getCategory().getName());
            res.setCategoryIcon(e.getCategory().getIcon());
            res.setCategoryColor(e.getCategory().getColor());
        }
        return res;
    }
}
