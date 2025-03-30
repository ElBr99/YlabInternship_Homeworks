package com.project.service.impl;

import com.project.dtos.DatePeriodDto;
import com.project.model.FinancialReport;
import com.project.model.Transaction;
import com.project.repository.TransactionRepository;
import com.project.service.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import static com.project.utils.SecurityContext.getCurrentUserEmail;

@Service
@RequiredArgsConstructor
public class FinancialServiceImpl implements FinancialService {

    private final TransactionRepository transactionRepository;

    @Override
    public BigDecimal findCurrentFinancialStatement() {
        BigDecimal income = transactionRepository.findIncome(getCurrentUserEmail());
        BigDecimal expenditure = transactionRepository.findExpenditure(getCurrentUserEmail());
        return income.subtract(expenditure);
    }

    @Override
    public BigDecimal findExpenseForPeriod(DatePeriodDto datePeriodDto) {
        return transactionRepository.findAllExpenditures(getCurrentUserEmail()).stream()
                .filter(transaction -> transaction.getDateTime().toLocalDate().isEqual(datePeriodDto.getTo()) ||
                        transaction.getDateTime().toLocalDate().isEqual(datePeriodDto.getFrom()) ||
                        transaction.getDateTime().toLocalDate().isBefore(datePeriodDto.getTo()) ||
                        transaction.getDateTime().toLocalDate().isAfter(datePeriodDto.getFrom()))
                .map(Transaction::getSum)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal findIncomeForPeriod(DatePeriodDto datePeriodDto) {
        return transactionRepository.findAllIncome(getCurrentUserEmail()).stream()
                .filter(transaction -> transaction.getDateTime().toLocalDate().isEqual(datePeriodDto.getTo()) ||
                        transaction.getDateTime().toLocalDate().isEqual(datePeriodDto.getFrom()) ||
                        transaction.getDateTime().toLocalDate().isBefore(datePeriodDto.getTo()) ||
                        transaction.getDateTime().toLocalDate().isAfter(datePeriodDto.getFrom()))
                .map(Transaction::getSum)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public Map<String, BigDecimal> showExpenseByCategories() {
        return transactionRepository.findAllExpenditures(getCurrentUserEmail())
                .stream()
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.mapping(Transaction::getSum, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

    }

    @Override
    public FinancialReport generateReport() {
        FinancialReport financialReport = new FinancialReport();
        financialReport.setCurrentStatement((transactionRepository.findIncome(getCurrentUserEmail())).subtract(transactionRepository.findExpenditure(getCurrentUserEmail())));
        financialReport.setIncomeForPeriod(transactionRepository.findIncome(getCurrentUserEmail()));
        financialReport.setExpenseForPeriod(transactionRepository.findExpenditure(getCurrentUserEmail()));
        financialReport.setExpenseByCategory(transactionRepository.findAllExpenditures(getCurrentUserEmail())
                .stream()
                .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.mapping(Transaction::getSum, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))));

        return financialReport;
    }

}

