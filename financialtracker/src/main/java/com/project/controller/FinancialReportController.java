package com.project.controller;

import com.project.dtos.DatePeriodDto;
import com.project.model.FinancialReport;
import com.project.service.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/financial-reports")
public class FinancialReportController {

    private final FinancialService financialService;

    @GetMapping("/statement")
    public BigDecimal findCurrentFinancialStatement() {
        return financialService.findCurrentFinancialStatement();
    }

    @GetMapping("/report")
    public FinancialReport generateReport() {
        return financialService.generateReport();
    }

    @GetMapping("/expense")
    public BigDecimal findExpanseForPeriod(DatePeriodDto datePeriodDto) {
        return financialService.findExpenseForPeriod(datePeriodDto);
    }

    @GetMapping("/expense-by-category")
    public Map<String, BigDecimal> findExpenseByCategories() {
        return financialService.showExpenseByCategories();
    }

}
