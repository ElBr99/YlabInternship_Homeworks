package com.project.controller;

import lombok.RequiredArgsConstructor;
import com.project.service.FinancialService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class ViewCurrentFinStatementController implements ControllerService {

    private final FinancialService financialService;


    @Override
    public void execute(Scanner scanner) {
        System.out.println("Текущее финансовое состояние: " + " " + financialService.findCurrentFinancialStatement());
    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) {
        out.println("Текущее финансовое состояние: " + " " + financialService.findCurrentFinancialStatement());
    }
}
