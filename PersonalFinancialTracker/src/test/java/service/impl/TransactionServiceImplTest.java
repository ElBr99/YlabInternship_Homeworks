package service.impl;

import dto.ChangeTransInfoDto;
import dto.CreateTransactionDto;
import dto.FilterTransactionsDto;
import exception.TransactionNotFound;
import listener.CreateTransactionListener;
import listener.DeleteTransactionListener;
import model.Transaction;
import model.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.TransactionRepository;
import utils.SecurityContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    private final String userEmail = "test@example.com";
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private List<CreateTransactionListener> transactionListeners;
    @Mock
    private List<DeleteTransactionListener> deleteTransactionListeners;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void addTransaction_ValidDto_SavesTransactionAndNotifiesListeners() {
        CreateTransactionDto createTransactionDto = createTransactionDto(TransactionType.EXPENDITURE, BigDecimal.valueOf(100), "Groceries", "Description");

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);

            transactionService.addTransaction(createTransactionDto);

            verify(transactionRepository).save(argThat(transaction ->
                    transaction.getUserEmail().equals(userEmail) &&
                            transaction.getTransactionType().equals(TransactionType.EXPENDITURE) &&
                            transaction.getSum().equals(BigDecimal.valueOf(100)) &&
                            transaction.getDescription().equals("Description") &&
                            transaction.getCategory().equals("Groceries")
            ));

        }
    }

    @Test
    void changeTransactionInfo_TransactionExists_UpdatesTransaction() {
        UUID transactionUuid = UUID.randomUUID();
        ChangeTransInfoDto changeTransInfoDto = createChangeTransInfoDto(BigDecimal.valueOf(200), "NewCategory", "NewDescription");
        Transaction existingTransaction = createTransaction(transactionUuid, userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries");
        when(transactionRepository.findById(transactionUuid)).thenReturn(Optional.of(existingTransaction));

        transactionService.changeTransactionInfo(transactionUuid, changeTransInfoDto);

        verify(transactionRepository).update(argThat(transaction ->
                transaction.getUuid().equals(transactionUuid) &&
                        transaction.getSum().equals(BigDecimal.valueOf(200)) &&
                        transaction.getCategory().equals("NewCategory") &&
                        transaction.getDescription().equals("NewDescription")
        ));
    }

    @Test
    void changeTransactionInfo_TransactionNotFound_ThrowsTransactionNotFound() {
        UUID transactionUuid = UUID.randomUUID();
        ChangeTransInfoDto changeTransInfoDto = createChangeTransInfoDto(BigDecimal.valueOf(200), "NewCategory", "NewDescription");
        when(transactionRepository.findById(transactionUuid)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFound.class, () -> transactionService.changeTransactionInfo(transactionUuid, changeTransInfoDto));
        verify(transactionRepository, never()).update(any());
    }

    @Test
    void deleteTransaction_TransactionExists_DeletesTransactionAndNotifiesListeners() {
        UUID transactionUuid = UUID.randomUUID();
        Transaction existingTransaction = createTransaction(transactionUuid, userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries");

        when(transactionRepository.findById(transactionUuid)).thenReturn(Optional.of(existingTransaction));

        transactionService.deleteTransaction(transactionUuid);

        verify(transactionRepository).delete(transactionUuid);
    }

    @Test
    void deleteTransaction_TransactionNotFound_ThrowsTransactionNotFound() {
        UUID transactionUuid = UUID.randomUUID();
        when(transactionRepository.findById(transactionUuid)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFound.class, () -> transactionService.deleteTransaction(transactionUuid));
        verify(transactionRepository, never()).delete(any());
        verify(deleteTransactionListeners, never()).forEach(any());
    }

    @Test
    void viewTransactions_ReturnsTransactionsForCurrentUser() {
        List<Transaction> expectedTransactions = List.of(
                createTransaction(UUID.randomUUID(), userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries"),
                createTransaction(UUID.randomUUID(), userEmail, TransactionType.INCOME, BigDecimal.valueOf(200), OffsetDateTime.now(), "Description", "Salary")
        );

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);
            when(transactionRepository.getTransactionListByUserEmail(userEmail)).thenReturn(expectedTransactions);

            List<Transaction> actualTransactions = transactionService.viewTransactions();

            assertEquals(expectedTransactions, actualTransactions);
            verify(transactionRepository).getTransactionListByUserEmail(userEmail);
        }
    }

    @Test
    void viewTransactionsByUserEmail_ReturnsTransactionsForGivenUserEmail() {
        String otherUserEmail = "other@example.com";
        List<Transaction> expectedTransactions = List.of(
                createTransaction(UUID.randomUUID(), otherUserEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries"),
                createTransaction(UUID.randomUUID(), otherUserEmail, TransactionType.INCOME, BigDecimal.valueOf(200), OffsetDateTime.now(), "Description", "Salary")
        );
        when(transactionRepository.getTransactionListByUserEmail(otherUserEmail)).thenReturn(expectedTransactions);

        List<Transaction> actualTransactions = transactionService.viewTransactionsByUserEmail(otherUserEmail);

        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository).getTransactionListByUserEmail(otherUserEmail);
    }

    @Test
    void filterTransactions_FiltersTransactionsCorrectly() {
        List<Transaction> allTransactions = List.of(
                createTransaction(UUID.randomUUID(), userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries"),
                createTransaction(UUID.randomUUID(), userEmail, TransactionType.INCOME, BigDecimal.valueOf(200), OffsetDateTime.now(), "Description", "Salary"),
                createTransaction(UUID.randomUUID(), userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(50), OffsetDateTime.now(), "Description", "Utilities")
        );

        FilterTransactionsDto filter = FilterTransactionsDto.builder()
                .transactionType(TransactionType.EXPENDITURE)
                .category("Groceries")
                .from(LocalDate.now().minusDays(1))
                .to(LocalDate.now().plusDays(1))
                .build();

        when(transactionRepository.getTransactionListByUserEmail(userEmail)).thenReturn(allTransactions);
        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);
            List<Transaction> filteredTransactions = transactionService.filterTransactions(filter);

            assertEquals(1, filteredTransactions.size());
            assertEquals("Groceries", filteredTransactions.get(0).getCategory());
        }
    }


    private CreateTransactionDto createTransactionDto(TransactionType transactionType, BigDecimal sum, String category, String description) {
        return CreateTransactionDto.builder()
                .transactionType(transactionType)
                .sum(sum)
                .category(category)
                .description(description)
                .build();
    }

    private ChangeTransInfoDto createChangeTransInfoDto(BigDecimal sum, String category, String description) {
        return ChangeTransInfoDto.builder()
                .sum(sum)
                .category(category)
                .description(description)
                .build();
    }

    private Transaction createTransaction(UUID uuid, String userEmail, TransactionType transactionType, BigDecimal sum, OffsetDateTime dateTime, String description, String category) {
        return Transaction.builder()
                .uuid(uuid)
                .userEmail(userEmail)
                .transactionType(transactionType)
                .sum(sum)
                .dateTime(dateTime)
                .description(description)
                .category(category)
                .build();
    }
}