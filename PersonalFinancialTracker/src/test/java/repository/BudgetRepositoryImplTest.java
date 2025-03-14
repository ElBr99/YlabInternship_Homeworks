package repository;

import model.Budget;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BudgetRepositoryImplTest extends AbstractIntegrationTest {

    private final BudgetRepository budgetRepository = new BudgetRepositoryImpl();


    @Test
    void findByUser_UserExists_ReturnsBudget() {
        int budgetId = 1;
        String userId = "testUser";
        BigDecimal budget = BigDecimal.valueOf(1000);
        Budget expectedBudgetForSet = new Budget(budgetId, userId, budget);

        budgetRepository.setBudgetForUser(expectedBudgetForSet);

        Optional<BigDecimal> foundBudget = budgetRepository.findByUser(userId);

        assertTrue(foundBudget.isPresent());
        assertEquals(budget, foundBudget.get());
    }

    @Test
    void findByUser_UserDoesNotExist_ReturnsEmptyOptional() {
        String userId = "nonExistentUser";

        Optional<BigDecimal> foundBudget = budgetRepository.findByUser(userId);

        assertFalse(foundBudget.isPresent());
    }

    @Test
    void setBudgetForUser_SetsBudgetSuccessfully() {
        int budgetId = 1;
        String userId = "newUser";
        BigDecimal budget = BigDecimal.valueOf(500);

        budgetRepository.setBudgetForUser(new Budget(budgetId,userId,budget));
        Optional<BigDecimal> foundBudget = budgetRepository.findByUser(userId);

        assertTrue(foundBudget.isPresent());
        assertEquals(budget, foundBudget.get());
    }


}