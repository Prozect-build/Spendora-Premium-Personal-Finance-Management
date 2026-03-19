package com.spendora.spendora_backend.repository;

import com.spendora.spendora_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

