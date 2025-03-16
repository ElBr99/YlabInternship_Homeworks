package com.project.repository;

import com.project.model.Budget;
import com.project.model.Role;
import com.project.model.User;
import com.project.utils.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class BudgetRepositoryImplTest extends AbstractIntegrationTest {

    private final BudgetRepository budgetRepository = new BudgetRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();


    @Test
    void findByUser_UserExists_ReturnsBudget() {
        User testUser = User.builder()
                .name("testUser")
                .email("testUser")
                .password("testUser123")
                .role(Role.valueOf("USER"))
                .blocked(false)
                .build();


        int budgetId = 1;
        String userId = testUser.getEmail();
        BigDecimal budget = BigDecimal.valueOf(1000).setScale(2);
        Budget expectedBudgetForSet = new Budget(budgetId, userId, budget);


        userRepository.save(testUser);
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

        User testUser = User.builder()
                .name("testUser")
                .email("testUser")
                .password("testUser123")
                .role(Role.valueOf("USER"))
                .blocked(false)
                .build();

        int budgetId = 1;
        String userId = testUser.getEmail();
        BigDecimal budget = BigDecimal.valueOf(500).setScale(2);

        userRepository.save(testUser);
        budgetRepository.setBudgetForUser(new Budget(budgetId, userId, budget));
        Optional<BigDecimal> foundBudget = budgetRepository.findByUser(userId);

        assertTrue(foundBudget.isPresent());
        assertEquals(budget, foundBudget.get());
    }


}