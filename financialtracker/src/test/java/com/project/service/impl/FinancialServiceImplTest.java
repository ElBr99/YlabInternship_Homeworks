package com.project.service.impl;

import com.project.dtos.DatePeriodDto;
import com.project.model.FinancialReport;
import com.project.model.Transaction;
import com.project.model.TransactionType;
import com.project.utils.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinancialServiceImplTest {

    private final String userEmail = "test@example.com";
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private FinancialServiceImpl financialService;

    @Test
    void findCurrentFinancialStatement_ReturnsCorrectStatement() {
        BigDecimal income = BigDecimal.valueOf(2000);
        BigDecimal expenditure = BigDecimal.valueOf(1000);

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);
            when(transactionRepository.findIncome(userEmail)).thenReturn(income);
            when(transactionRepository.findExpenditure(userEmail)).thenReturn(expenditure);

            BigDecimal actualStatement = financialService.findCurrentFinancialStatement();

            assertEquals(BigDecimal.valueOf(1000), actualStatement);
            verify(transactionRepository).findIncome(userEmail);
            verify(transactionRepository).findExpenditure(userEmail);
        }
    }

    @Test
    void findExpenseForPeriod_ReturnsSumOfExpendituresForPeriod() {
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);
        DatePeriodDto datePeriodDto = createDatePeriodDto(from, to);

        List<Transaction> expenditures = List.of(
                createTransaction(userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.of(2024, 1, 15, 0, 0, 0, 0, OffsetDateTime.now().getOffset()), "Category1"),
                createTransaction(userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(200), OffsetDateTime.of(2024, 1, 20, 0, 0, 0, 0, OffsetDateTime.now().getOffset()), "Category2")
        );

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);
            when(transactionRepository.findAllExpenditures(userEmail)).thenReturn(expenditures);

            BigDecimal totalExpense = financialService.findExpenseForPeriod(datePeriodDto);

            assertEquals(BigDecimal.valueOf(300), totalExpense);
            verify(transactionRepository).findAllExpenditures(userEmail);
        }
    }

    @Test
    void findExpenseForPeriod_NoExpenditures_ReturnsZero() {
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);
        DatePeriodDto datePeriodDto = createDatePeriodDto(from, to);

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);
            when(transactionRepository.findAllExpenditures(userEmail)).thenReturn(List.of());

            BigDecimal totalExpense = financialService.findExpenseForPeriod(datePeriodDto);

            assertEquals(BigDecimal.ZERO, totalExpense);
            verify(transactionRepository).findAllExpenditures(userEmail);
        }
    }

    @Test
    void findIncomeForPeriod_ReturnsSumOfIncomesForPeriod() {
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);
        DatePeriodDto datePeriodDto = createDatePeriodDto(from, to);

        List<Transaction> incomes = List.of(
                createTransaction(userEmail, TransactionType.INCOME, BigDecimal.valueOf(500), OffsetDateTime.of(2024, 1, 10, 0, 0, 0, 0, OffsetDateTime.now().getOffset()), "Salary"),
                createTransaction(userEmail, TransactionType.INCOME, BigDecimal.valueOf(300), OffsetDateTime.of(2024, 1, 25, 0, 0, 0, 0, OffsetDateTime.now().getOffset()), "Bonus")
        );

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);
            when(transactionRepository.findAllIncome(userEmail)).thenReturn(incomes);

            BigDecimal totalIncome = financialService.findIncomeForPeriod(datePeriodDto);

            assertEquals(BigDecimal.valueOf(800), totalIncome);
            verify(transactionRepository).findAllIncome(userEmail);
        }
    }

    @Test
    void showExpenseByCategories_ReturnsExpenseByCategoryMap() {
        List<Transaction> expenditures = List.of(
                createTransaction(userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Groceries"),
                createTransaction(userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(50), OffsetDateTime.now(), "Utilities"),
                createTransaction(userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(75), OffsetDateTime.now(), "Groceries")
        );
        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);
            when(transactionRepository.findAllExpenditures(userEmail)).thenReturn(expenditures);

            Map<String, BigDecimal> expenseByCategory = financialService.showExpenseByCategories();

            assertEquals(2, expenseByCategory.size());
            assertEquals(BigDecimal.valueOf(175), expenseByCategory.get("Groceries"));
            assertEquals(BigDecimal.valueOf(50), expenseByCategory.get("Utilities"));
            verify(transactionRepository).findAllExpenditures(userEmail);
        }
    }

    @Test
    void generateReport_ReturnsCompleteFinancialReport() {
        BigDecimal currentStatement = BigDecimal.valueOf(1000);
        BigDecimal incomeForPeriod = BigDecimal.valueOf(2000);
        BigDecimal expenseForPeriod = BigDecimal.valueOf(1000);
        Map<String, BigDecimal> expenseByCategory = Map.of("Groceries", BigDecimal.valueOf(175), "Utilities", BigDecimal.valueOf(50));

        List<Transaction> expenditures = List.of(
                createTransaction(userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(100), OffsetDateTime.now(), "Groceries"),
                createTransaction(userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(50), OffsetDateTime.now(), "Utilities"),
                createTransaction(userEmail, TransactionType.EXPENDITURE, BigDecimal.valueOf(75), OffsetDateTime.now(), "Groceries")
        );

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);
            when(transactionRepository.findIncome(userEmail)).thenReturn(incomeForPeriod);
            when(transactionRepository.findExpenditure(userEmail)).thenReturn(expenseForPeriod);
            when(transactionRepository.findAllExpenditures(userEmail)).thenReturn(expenditures);

            FinancialReport report = financialService.generateReport();

            assertEquals(currentStatement, report.getCurrentStatement());
            assertEquals(incomeForPeriod, report.getIncomeForPeriod());
            assertEquals(expenseForPeriod, report.getExpenseForPeriod());
            assertEquals(expenseByCategory, report.getExpenseByCategory());
        }
    }

    private DatePeriodDto createDatePeriodDto(LocalDate from, LocalDate to) {
        return DatePeriodDto.builder()
                .from(from)
                .to(to)
                .build();
    }

    private Transaction createTransaction(String userEmail, TransactionType transactionType, BigDecimal sum, OffsetDateTime dateTime, String category) {
        return Transaction.builder()
                .userEmail(userEmail)
                .transactionType(transactionType)
                .sum(sum)
                .dateTime(dateTime)
                .category(category)
                .build();
    }
}