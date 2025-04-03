package com.project.listener;

import com.project.model.Transaction;
import com.project.utils.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.BudgetService;
import com.project.service.FinancialService;
import com.project.service.NotificationService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationTransactionListenerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private BudgetService budgetService;

    @Mock
    private FinancialService financialService;

    @InjectMocks
    private NotificationTransactionListener notificationTransactionListener;

    @Test
    void onCreate_BudgetPresentAndLessThanFinancialStatement_SendsNotification() {
        String userEmail = "test@example.com";
        Transaction transaction = Transaction.builder().userEmail(userEmail).build();
        BigDecimal budgetValue = BigDecimal.valueOf(100);
        BigDecimal financialStatementValue = BigDecimal.valueOf(200);

        when(budgetService.findBudget(userEmail)).thenReturn(Optional.of(budgetValue));
        when(financialService.findCurrentFinancialStatement()).thenReturn(financialStatementValue);

        try (MockedStatic<SecurityContext> mockedUserContext = Mockito.mockStatic(SecurityContext.class)) {
            mockedUserContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);

            notificationTransactionListener.onCreate(transaction);
            verify(notificationService).notify(userEmail, "Превышение баланса!");
        }
    }

    @Test
    void onCreate_BudgetPresentAndGreaterThanFinancialStatement_DoesNotSendNotification() {
        String userEmail = "test@example.com";
        Transaction transaction = Transaction.builder().userEmail(userEmail).build();
        BigDecimal budgetValue = BigDecimal.valueOf(300);
        BigDecimal financialStatementValue = BigDecimal.valueOf(200);

        when(budgetService.findBudget(userEmail))
                .thenReturn(Optional.of(budgetValue));
        when(financialService.findCurrentFinancialStatement())
                .thenReturn(financialStatementValue);

        notificationTransactionListener.onCreate(transaction);

        verify(notificationService, never()).notify(anyString(), anyString());
    }

    @Test
    void onCreate_BudgetNotPresent_DoesNotSendNotification() {
        String userEmail = "test@example.com";
        Transaction transaction = Transaction.builder().userEmail(userEmail).build();

        when(budgetService.findBudget(userEmail))
                .thenReturn(Optional.empty());

        notificationTransactionListener.onCreate(transaction);

        verify(notificationService, never()).notify(anyString(), anyString());
    }

    private Transaction createTransaction(String userEmail) {
        return Transaction.builder().userEmail(userEmail).build();
    }
}