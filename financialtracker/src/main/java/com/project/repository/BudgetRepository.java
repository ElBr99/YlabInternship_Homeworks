package com.project.repository;

import com.project.model.Budget;

import java.math.BigDecimal;
import java.util.Optional;

public interface BudgetRepository {
    Optional<BigDecimal> findByUser(String userId);

    void setBudgetForUser(Budget budget);
}
