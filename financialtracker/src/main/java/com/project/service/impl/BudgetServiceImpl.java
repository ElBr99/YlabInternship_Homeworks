package com.project.service.impl;

import com.project.utils.SecurityContext;
import lombok.RequiredArgsConstructor;
import com.project.model.Budget;
import com.project.repository.BudgetRepository;
import com.project.service.BudgetService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    @Override
    public void setBudgetForCurrentUser(BigDecimal budgetAmount) {
        Budget budget = Budget.builder()
                .userEmail(SecurityContext.getCurrentUserEmail())
                .amount(budgetAmount)
                .build();

        budgetRepository.setBudgetForUser(budget);
    }

    @Override
    public Optional<BigDecimal> findBudget(String userEmail) {
        return budgetRepository.findByUser(userEmail);
    }
}
