package listener;

import lombok.RequiredArgsConstructor;
import model.Transaction;
import service.BudgetService;
import service.FinancialService;
import service.NotificationService;

import static utils.SecurityContext.getCurrentUserEmail;

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
