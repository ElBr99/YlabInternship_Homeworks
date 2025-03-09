package repository;

import java.math.BigDecimal;
import java.util.Optional;

public interface BudgetRepository {
    Optional<BigDecimal> findByUser(String userId);

    void setBudgetForUser(String userId, BigDecimal budget);
}
