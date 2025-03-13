package service.impl;

import lombok.RequiredArgsConstructor;
import repository.BudgetRepository;
import service.BudgetService;

import java.math.BigDecimal;
import java.util.Optional;

import static utils.SecurityContext.getCurrentUserEmail;

@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    @Override
    public void setBudgetForCurrentUser(BigDecimal budget) {
        budgetRepository.setBudgetForUser(getCurrentUserEmail(), budget);
    }

    @Override
    public Optional<BigDecimal> findBudget(String userEmail) {
        return budgetRepository.findByUser(userEmail);
    }
}
