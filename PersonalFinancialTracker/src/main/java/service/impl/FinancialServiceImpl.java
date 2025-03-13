package service.impl;

import dto.DatePeriodDto;
import lombok.RequiredArgsConstructor;
import model.FinancialReport;
import model.Transaction;
import repository.TransactionRepository;
import service.FinancialService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.SecurityContext.getCurrentUserEmail;

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
        financialReport.setCurrentStatement(findCurrentFinancialStatement());
        financialReport.setIncomeForPeriod(transactionRepository.findIncome(getCurrentUserEmail()));
        financialReport.setExpenseForPeriod(transactionRepository.findExpenditure(getCurrentUserEmail()));
        financialReport.setExpenseByCategory(showExpenseByCategories());
        return financialReport;
    }

}

