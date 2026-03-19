package com.spendora.spendora_backend.repository;

import com.spendora.spendora_backend.entity.Budget;
import com.spendora.spendora_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserAndMonthAndYear(User user, Integer month, Integer year);

    Optional<Budget> findByUserAndCategoryIdAndMonthAndYear(
            User user, Long categoryId, Integer month, Integer year);

    boolean existsByUserAndCategoryIdAndMonthAndYear(
            User user, Long categoryId, Integer month, Integer year);
}
