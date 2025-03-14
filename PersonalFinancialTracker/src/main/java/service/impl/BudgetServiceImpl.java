package service.impl;

import lombok.RequiredArgsConstructor;
import model.Budget;
import repository.BudgetRepository;
import service.BudgetService;
import utils.SecurityContext;

import java.math.BigDecimal;
import java.util.Optional;

import static utils.SecurityContext.getCurrentUserEmail;

@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    @Override
    public void setBudgetForCurrentUser(BigDecimal budget_amount) {
        Budget budget = Budget.builder()
                .userEmail(SecurityContext.getCurrentUserEmail())
                .amount(budget_amount)
                .build();
        budgetRepository.setBudgetForUser(budget);
    }

    @Override
    public Optional<BigDecimal> findBudget(String userEmail) {
        return budgetRepository.findByUser(userEmail);
    }
}
