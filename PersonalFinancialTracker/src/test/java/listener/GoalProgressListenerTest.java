package listener;

import com.project.dtos.UpdateGoalDto;
import com.project.listener.GoalProgressListener;
import com.project.model.Goal;
import com.project.model.Transaction;
import com.project.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.GoalService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalProgressListenerTest {

    @Mock
    private GoalService goalService;

    @InjectMocks
    private GoalProgressListener goalProgressListener;

    private final String userEmail = "test@example.com";

    @Test
    void onCreate_IncomeTransaction_UpdatesGoalProgress() {
        BigDecimal transactionSum = BigDecimal.valueOf(50);
        Transaction transaction = createTransaction(userEmail, TransactionType.INCOME, transactionSum);
        BigDecimal initialCurrent = BigDecimal.valueOf(100);
        BigDecimal target = BigDecimal.valueOf(200);
        Goal goal = createGoal(initialCurrent, target);

        when(goalService.getGoalByUser(userEmail))
                .thenReturn(Optional.of(goal));

        goalProgressListener.onCreate(transaction);

        BigDecimal expectedCurrent = initialCurrent.add(transactionSum);
        verify(goalService).updateGoal(userEmail, UpdateGoalDto.builder().current(expectedCurrent).build());
    }

    @Test
    void onCreate_ExpenditureTransaction_UpdatesGoalProgress() {
        BigDecimal transactionSum = BigDecimal.valueOf(30);
        Transaction transaction = createTransaction(userEmail, TransactionType.EXPENDITURE, transactionSum);
        BigDecimal initialCurrent = BigDecimal.valueOf(150);
        BigDecimal target = BigDecimal.valueOf(200);
        Goal goal = createGoal(initialCurrent, target);
        when(goalService.getGoalByUser(userEmail)).thenReturn(Optional.of(goal));


        goalProgressListener.onCreate(transaction);


        BigDecimal expectedCurrent = initialCurrent.subtract(transactionSum);
        verify(goalService).updateGoal(userEmail, UpdateGoalDto.builder().current(expectedCurrent).build());
    }

    @Test
    void onCreate_NoGoalFound_DoesNotUpdateGoal() {
        BigDecimal transactionSum = BigDecimal.valueOf(50);
        Transaction transaction = createTransaction(userEmail, TransactionType.INCOME, transactionSum);

        when(goalService.getGoalByUser(userEmail)).thenReturn(Optional.empty());

        goalProgressListener.onCreate(transaction);

        verify(goalService, never()).updateGoal(anyString(), any(UpdateGoalDto.class));
    }

    @Test
    void onDelete_IncomeTransaction_UpdatesGoalProgress() {
        BigDecimal transactionSum = BigDecimal.valueOf(50);
        Transaction transaction = createTransaction(userEmail, TransactionType.INCOME, transactionSum);
        BigDecimal initialCurrent = BigDecimal.valueOf(100);
        BigDecimal target = BigDecimal.valueOf(200);
        Goal goal = createGoal(initialCurrent, target);

        when(goalService.getGoalByUser(userEmail)).thenReturn(Optional.of(goal));

        goalProgressListener.onDelete(transaction);

        BigDecimal expectedCurrent = initialCurrent.add(transactionSum);
        verify(goalService).updateGoal(userEmail, UpdateGoalDto.builder().current(expectedCurrent).build());
    }

    @Test
    void onDelete_ExpenditureTransaction_UpdatesGoalProgress() {
        BigDecimal transactionSum = BigDecimal.valueOf(30);
        Transaction transaction = createTransaction(userEmail, TransactionType.EXPENDITURE, transactionSum);
        BigDecimal initialCurrent = BigDecimal.valueOf(150);
        BigDecimal target = BigDecimal.valueOf(200);
        Goal goal = createGoal(initialCurrent, target);

        when(goalService.getGoalByUser(userEmail))
                .thenReturn(Optional.of(goal));

        goalProgressListener.onDelete(transaction);

        BigDecimal expectedCurrent = initialCurrent.subtract(transactionSum);
        verify(goalService).updateGoal(userEmail, UpdateGoalDto.builder().current(expectedCurrent).build());
    }

    @Test
    void onDelete_NoGoalFound_DoesNotUpdateGoal() {
        BigDecimal transactionSum = BigDecimal.valueOf(50);
        Transaction transaction = createTransaction(userEmail, TransactionType.INCOME, transactionSum);

        when(goalService.getGoalByUser(userEmail)).thenReturn(Optional.empty());

        goalProgressListener.onDelete(transaction);

        verify(goalService, never()).updateGoal(anyString(), any(UpdateGoalDto.class));
    }

    @Test
    void calculatePercentage_ValidCurrentAndTarget_OutputsCorrectPercentage() {
        BigDecimal transactionSum = BigDecimal.valueOf(50);
        Transaction transaction = createTransaction(userEmail, TransactionType.INCOME, transactionSum);
        BigDecimal initialCurrent = BigDecimal.valueOf(150);
        BigDecimal target = BigDecimal.valueOf(200);
        Goal goal = createGoal(initialCurrent, target);

        when(goalService.getGoalByUser(userEmail)).thenReturn(Optional.of(goal));

        goalProgressListener.onCreate(transaction);

        BigDecimal expectedCurrent = initialCurrent.add(transactionSum);
        verify(goalService).updateGoal(userEmail, UpdateGoalDto.builder().current(expectedCurrent).build());
    }

    @Test
    void calculatePercentage_targetIsZero_OutputsZero() {
        BigDecimal transactionSum = BigDecimal.valueOf(50);
        Transaction transaction = createTransaction(userEmail, TransactionType.INCOME, transactionSum);
        BigDecimal initialCurrent = BigDecimal.valueOf(150);
        BigDecimal target = BigDecimal.ZERO;
        Goal goal = createGoal(initialCurrent, target);

        when(goalService.getGoalByUser(userEmail))
                .thenReturn(Optional.of(goal));

        assertThrows(
                ArithmeticException.class,
                () -> goalProgressListener.onCreate(transaction)
        );
    }

    private Transaction createTransaction(String userEmail, TransactionType transactionType, BigDecimal sum) {
        return Transaction.builder()
                .userEmail(userEmail)
                .transactionType(transactionType)
                .sum(sum)
                .build();
    }

    private Goal createGoal(BigDecimal current, BigDecimal target) {
        return Goal.builder()
                .current(current)
                .target(target)
                .goal("Test Goal")
                .build();
    }
}