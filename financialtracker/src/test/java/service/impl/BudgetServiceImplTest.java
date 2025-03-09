package service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.BudgetRepository;
import utils.SecurityContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceImplTest {

    private final String userEmail = "test@example.com";
    @Mock
    private BudgetRepository budgetRepository;
    @InjectMocks
    private BudgetServiceImpl budgetService;

    @Test
    void setBudgetForCurrentUser_ValidBudget_CallsRepositoryWithCorrectData() {
        BigDecimal budget = BigDecimal.valueOf(1000);

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);

            budgetService.setBudgetForCurrentUser(budget);

            verify(budgetRepository).setBudgetForUser(userEmail, budget);
        }
    }

    @Test
    void findBudget_UserExists_ReturnsBudgetFromRepository() {
        BigDecimal expectedBudget = BigDecimal.valueOf(500);

        when(budgetRepository.findByUser(userEmail))
                .thenReturn(Optional.of(expectedBudget));

        Optional<BigDecimal> actualBudget = budgetService.findBudget(userEmail);

        assertTrue(actualBudget.isPresent());
        assertEquals(expectedBudget, actualBudget.get());
    }

    @Test
    void findBudget_UserDoesNotExist_ReturnsEmptyOptionalFromRepository() {
        when(budgetRepository.findByUser(userEmail))
                .thenReturn(Optional.empty());

        Optional<BigDecimal> actualBudget = budgetService.findBudget(userEmail);

        assertFalse(actualBudget.isPresent());
    }
}