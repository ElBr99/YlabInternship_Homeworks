package service;

import java.math.BigDecimal;
import java.util.Optional;

public interface BudgetService {
    void setBudgetForCurrentUser(BigDecimal budget);

    Optional<BigDecimal> findBudget(String userEmail);
}
