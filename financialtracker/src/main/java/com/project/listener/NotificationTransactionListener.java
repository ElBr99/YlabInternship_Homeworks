package com.project.listener;

import lombok.RequiredArgsConstructor;
import com.project.model.Transaction;
import com.project.service.BudgetService;
import com.project.service.FinancialService;
import com.project.service.NotificationService;
import org.springframework.stereotype.Service;

import static com.project.utils.SecurityContext.getCurrentUserEmail;

@Service
@RequiredArgsConstructor
public class NotificationTransactionListener implements CreateTransactionListener {

    private final NotificationService notificationService;
    private final BudgetService budgetService;
    private final FinancialService financialService;

    @Override
    public void onCreate(Transaction transaction) {
        budgetService.findBudget(transaction.getUserEmail())
                .ifPresent(value -> {
                    if (value.compareTo(financialService.findCurrentFinancialStatement()) < 0) {
                        notificationService.notify(getCurrentUserEmail(), "Превышение баланса!");
                    }
                });
    }
}
