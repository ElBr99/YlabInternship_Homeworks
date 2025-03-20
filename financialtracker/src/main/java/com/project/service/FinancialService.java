package com.project.service;

import com.project.dtos.DatePeriodDto;
import com.project.model.FinancialReport;

import java.math.BigDecimal;
import java.util.Map;

public interface FinancialService {

    BigDecimal findCurrentFinancialStatement();

    BigDecimal findExpenseForPeriod(DatePeriodDto datePeriodDto);

    BigDecimal findIncomeForPeriod(DatePeriodDto datePeriodDto);

    Map<String, BigDecimal> showExpenseByCategories();

    FinancialReport generateReport();

}
