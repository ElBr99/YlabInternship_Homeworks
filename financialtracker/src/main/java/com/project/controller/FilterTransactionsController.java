package com.project.controller;

import com.project.dtos.FilterTransactionsDto;
import lombok.RequiredArgsConstructor;
import com.project.model.TransactionType;
import com.project.service.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;


@RequiredArgsConstructor
public class FilterTransactionsController implements ControllerService {

    private final TransactionService transactionService;

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Заполните поле, по которому хотите отфильтровать транзакции, иначе оставьте поле пустым, нажав enter");
        System.out.println("Введите дату с.. в формате гг.мм.дд");
        String dateFrom = scanner.nextLine();
        LocalDate locDateFrom = LocalDate.parse(dateFrom);

        System.out.println("Введите дату до .. в формате гг.мм.дд.");
        String dateTo = scanner.nextLine();
        LocalDate locDateTo = LocalDate.parse(dateTo);

        System.out.println("Введите категорию, по которой хотите отфильтровать транзакции. Иначе оставьте поле пустым, нажав enter");
        String category = scanner.nextLine();

        System.out.println("Введите тип транзакции, по которому хотите отфильтровать, из предложенных: INCOME/EXPENDITURE. Иначе оставьте поле пустым, нажав enter");
        String transactionType = scanner.nextLine();

        FilterTransactionsDto filterTransactionsDto = new FilterTransactionsDto(locDateFrom, locDateTo, category, TransactionType.valueOf(transactionType));
        transactionService.filterTransactions(filterTransactionsDto);

    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Заполните поле, по которому хотите отфильтровать транзакции, иначе оставьте поле пустым, нажав enter");
        out.println("Введите дату с.. в формате гг.мм.дд");
        String dateFrom = in.readLine();
        LocalDate locDateFrom = LocalDate.parse(dateFrom);

        out.println("Введите дату до .. в формате гг.мм.дд.");
        String dateTo = in.readLine();
        LocalDate locDateTo = LocalDate.parse(dateTo);

        out.println("Введите категорию, по которой хотите отфильтровать транзакции. Иначе оставьте поле пустым, нажав enter");
        String category = in.readLine();

        out.println("Введите тип транзакции, по которому хотите отфильтровать, из предложенных: INCOME/EXPENDITURE. Иначе оставьте поле пустым, нажав enter");
        String transactionType = in.readLine();

        FilterTransactionsDto filterTransactionsDto = new FilterTransactionsDto(locDateFrom, locDateTo, category, TransactionType.valueOf(transactionType));
        transactionService.filterTransactions(filterTransactionsDto);
    }
}
