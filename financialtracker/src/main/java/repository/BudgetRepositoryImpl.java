package repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BudgetRepositoryImpl implements BudgetRepository {

    private static final Map<String, BigDecimal> budgetMap = new ConcurrentHashMap<>();

    @Override
    public Optional<BigDecimal> findByUser(String userId) {
        return Optional.ofNullable(budgetMap.get(userId));
    }

    @Override
    public void setBudgetForUser(String userId, BigDecimal budget) {
        budgetMap.put(userId, budget);
    }

}
