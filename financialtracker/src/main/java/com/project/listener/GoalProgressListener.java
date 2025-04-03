package com.project.listener;

import com.project.dtos.UpdateGoalDto;
import lombok.RequiredArgsConstructor;
import com.project.model.Transaction;
import com.project.model.TransactionType;
import com.project.service.GoalService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class GoalProgressListener implements CreateTransactionListener, DeleteTransactionListener {

    private final GoalService goalService;

    private static BigDecimal calculatePercentage(BigDecimal current, BigDecimal target) {
        return current
                .divide(target, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    @Override
    public void onCreate(Transaction transaction) {
        updateGoalProgress(transaction);
    }

    @Override
    public void onDelete(Transaction transaction) {
        updateGoalProgress(transaction);
    }

    private void updateGoalProgress(Transaction transaction) {
        String user = transaction.getUserEmail();
        goalService.getGoalByUser(user)
                .ifPresent(goal -> {
                    BigDecimal current = goal.getCurrent();
                    BigDecimal target = goal.getTarget();

                    if (TransactionType.INCOME.equals(transaction.getTransactionType())) {
                        current = current.add(transaction.getSum());
                    }
                    if (TransactionType.EXPENDITURE.equals(transaction.getTransactionType())) {
                        current = current.subtract(transaction.getSum());
                    }

                    BigDecimal progressPercentage = calculatePercentage(current, target);
                    System.out.println("Прогресс цели '" + goal.getGoal() + "': " + progressPercentage + "%");

                    goalService.updateGoal(user, UpdateGoalDto.builder().current(current).build());
                });
    }
}
