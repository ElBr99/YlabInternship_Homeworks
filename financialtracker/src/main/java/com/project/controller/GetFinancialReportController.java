package com.project.controller;

import lombok.RequiredArgsConstructor;
import com.project.model.FinancialReport;
import com.project.service.FinancialService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class GetFinancialReportController implements ControllerService {

    private final FinancialService financialService;

    @Override
    public void execute(Scanner scanner) {
        FinancialReport financialReport = financialService.generateReport();
        System.out.println(financialReport.getCurrentStatement().toPlainString() + " " +
                financialReport.getExpenseForPeriod().toPlainString() + " " +
                financialReport.getIncomeForPeriod().toPlainString());
    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) {
        FinancialReport financialReport = financialService.generateReport();
        out.println(financialReport.getCurrentStatement().toPlainString() + " " +
                financialReport.getExpenseForPeriod().toPlainString() + " " +
                financialReport.getIncomeForPeriod().toPlainString());
    }
}
