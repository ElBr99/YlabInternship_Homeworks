package com.project.service.impl;

import com.project.dtos.ChangeTransInfoDto;
import com.project.dtos.CreateTransactionDto;
import com.project.dtos.FilterTransactionsDto;
import com.project.exceptions.TransactionNotFoundException;
import com.project.listener.CreateTransactionListener;
import com.project.listener.DeleteTransactionListener;
import com.project.model.Transaction;
import com.project.model.TransactionType;
import com.project.repository.TransactionRepository;
import com.project.utils.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
        int transactionId = new Random().nextInt(10) + 1;
        ChangeTransInfoDto changeTransInfoDto = createChangeTransInfoDto(BigDecimal.valueOf(200), "NewCategory", "NewDescription");
        Transaction existingTransaction = createTransaction(transactionId, userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries");
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));

        transactionService.changeTransactionInfo(transactionId, changeTransInfoDto);

        verify(transactionRepository).update(argThat(transaction ->
                transaction.getId() == (transactionId) &&
                        transaction.getSum().equals(BigDecimal.valueOf(200)) &&
                        transaction.getCategory().equals("NewCategory") &&
                        transaction.getDescription().equals("NewDescription")
        ));
    }

    @Test
    void changeTransactionInfo_TransactionNotFound_ThrowsTransactionNotFound() {
        int transactionId = new Random().nextInt(10) + 1;
        ChangeTransInfoDto changeTransInfoDto = createChangeTransInfoDto(BigDecimal.valueOf(200), "NewCategory", "NewDescription");
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.changeTransactionInfo(transactionId, changeTransInfoDto));
        verify(transactionRepository, never()).update(any());
    }

    @Test
    void deleteTransaction_TransactionExists_DeletesTransactionAndNotifiesListeners() {
        int transactionId = new Random().nextInt(10) + 1;
        Transaction existingTransaction = createTransaction(transactionId, userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries");

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));

        transactionService.deleteTransaction(transactionId);

        verify(transactionRepository).delete(transactionId);
    }

    @Test
    void deleteTransaction_TransactionNotFound_ThrowsTransactionNotFound() {
        int transactionId = new Random().nextInt(10) + 1;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransaction(transactionId));
        verify(transactionRepository, never()).delete(transactionId);

        verify(deleteTransactionListeners, never()).forEach(any());
    }

    @Test
    void viewTransactions_ReturnsTransactionsForCurrentUser() {
        int transactionId = new Random().nextInt(10) + 1;
        List<Transaction> expectedTransactions = List.of(
                createTransaction(transactionId, userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries"),
                createTransaction(transactionId, userEmail, TransactionType.INCOME, BigDecimal.valueOf(200), OffsetDateTime.now(), "Description", "Salary")
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
        int transactionId = new Random().nextInt(10) + 1;
        List<Transaction> expectedTransactions = List.of(
                createTransaction(transactionId, otherUserEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries"),
                createTransaction(transactionId, otherUserEmail, TransactionType.INCOME, BigDecimal.valueOf(200), OffsetDateTime.now(), "Description", "Salary")
        );
        when(transactionRepository.getTransactionListByUserEmail(otherUserEmail)).thenReturn(expectedTransactions);

        List<Transaction> actualTransactions = transactionService.viewTransactionsByUserEmail(otherUserEmail);

        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository).getTransactionListByUserEmail(otherUserEmail);
    }

    @Test
    void filterTransactions_FiltersTransactionsCorrectly() {
        int transactionId = new Random().nextInt(10) + 1;
        List<Transaction> allTransactions = List.of(
                createTransaction(transactionId, userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Description", "Groceries"),
                createTransaction(transactionId, userEmail, TransactionType.INCOME, BigDecimal.valueOf(200), OffsetDateTime.now(), "Description", "Salary"),
                createTransaction(transactionId, userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(50), OffsetDateTime.now(), "Description", "Utilities")
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

    private Transaction createTransaction(int id, String userEmail, TransactionType transactionType, BigDecimal sum, OffsetDateTime dateTime, String description, String category) {
        return Transaction.builder()
                .id(id)
                .userEmail(userEmail)
                .transactionType(transactionType)
                .sum(sum)
                .dateTime(dateTime)
                .description(description)
                .category(category)
                .build();
    }
}