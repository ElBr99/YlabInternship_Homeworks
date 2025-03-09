package repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BudgetRepositoryImplTest {

    private final BudgetRepository budgetRepository = new BudgetRepositoryImpl();

    @AfterEach
    void tearDown() throws Exception {
        clearBudgetMap();
    }

    @Test
    void findByUser_UserExists_ReturnsBudget() {
        String userId = "testUser";
        BigDecimal budget = BigDecimal.valueOf(1000);
        budgetRepository.setBudgetForUser(userId, budget);

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
        String userId = "newUser";
        BigDecimal budget = BigDecimal.valueOf(500);

        budgetRepository.setBudgetForUser(userId, budget);
        Optional<BigDecimal> foundBudget = budgetRepository.findByUser(userId);

        assertTrue(foundBudget.isPresent());
        assertEquals(budget, foundBudget.get());
    }

    @Test
    void setBudgetForUser_OverwritesExistingBudget() {
        String userId = "existingUser";
        BigDecimal initialBudget = BigDecimal.valueOf(1000);
        BigDecimal newBudget = BigDecimal.valueOf(1500);
        budgetRepository.setBudgetForUser(userId, initialBudget);

        budgetRepository.setBudgetForUser(userId, newBudget);
        Optional<BigDecimal> foundBudget = budgetRepository.findByUser(userId);

        assertTrue(foundBudget.isPresent());
        assertEquals(newBudget, foundBudget.get());
    }


    private void clearBudgetMap() throws Exception {
        Field field = BudgetRepositoryImpl.class.getDeclaredField("budgetMap");
        field.setAccessible(true);
        Map<String, BigDecimal> budgetMap = (Map<String, BigDecimal>) field.get(null);
        budgetMap.clear();
    }
}