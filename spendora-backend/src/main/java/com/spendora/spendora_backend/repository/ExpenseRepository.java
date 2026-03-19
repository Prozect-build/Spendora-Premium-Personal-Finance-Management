package com.spendora.spendora_backend.repository;

import com.spendora.spendora_backend.entity.Expense;
import com.spendora.spendora_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserOrderByDateDesc(User user);

    List<Expense> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate from, LocalDate to);

    List<Expense> findByUserAndTypeAndDateBetweenOrderByDateDesc(
            User user, String type, LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "WHERE e.user = :user AND e.type = :type " +
           "AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    BigDecimal sumByUserAndTypeAndMonthAndYear(
            @Param("user") User user,
            @Param("type") String type,
            @Param("month") int month,
            @Param("year") int year);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "WHERE e.user = :user AND e.type = 'EXPENSE' " +
           "AND e.category.id = :categoryId " +
           "AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    BigDecimal sumExpenseByCategoryAndMonth(
            @Param("user") User user,
            @Param("categoryId") Long categoryId,
            @Param("month") int month,
            @Param("year") int year);

    @Query("SELECT e FROM Expense e WHERE e.user = :user " +
           "ORDER BY e.createdAt DESC LIMIT 10")
    List<Expense> findTop10ByUserOrderByCreatedAtDesc(@Param("user") User user);

    @Query("SELECT MONTH(e.date) as month, SUM(CASE WHEN e.type='INCOME' THEN e.amount ELSE 0 END) as income, " +
           "SUM(CASE WHEN e.type='EXPENSE' THEN e.amount ELSE 0 END) as expense " +
           "FROM Expense e WHERE e.user = :user AND YEAR(e.date) = :year " +
           "GROUP BY MONTH(e.date) ORDER BY MONTH(e.date)")
    List<Object[]> getMonthlyCashflow(@Param("user") User user, @Param("year") int year);

    @Query("SELECT e.category.name, e.category.color, SUM(e.amount) FROM Expense e " +
           "WHERE e.user = :user AND e.type = 'EXPENSE' " +
           "AND MONTH(e.date) = :month AND YEAR(e.date) = :year " +
           "GROUP BY e.category.name, e.category.color ORDER BY SUM(e.amount) DESC")
    List<Object[]> getExpenseBreakdownByCategory(
            @Param("user") User user,
            @Param("month") int month,
            @Param("year") int year);

    @Query("SELECT CAST(e.date AS string), SUM(e.amount) FROM Expense e " +
           "WHERE e.user = :user AND e.type = 'EXPENSE' " +
           "AND MONTH(e.date) = :month AND YEAR(e.date) = :year " +
           "GROUP BY e.date ORDER BY SUM(e.amount) DESC LIMIT 1")
    List<Object[]> getHighestSpendDay(
            @Param("user") User user,
            @Param("month") int month,
            @Param("year") int year);
}
