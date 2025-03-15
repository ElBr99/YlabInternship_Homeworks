package com.project.controller;

import com.project.dtos.CreateTransactionDto;
import lombok.RequiredArgsConstructor;
import com.project.model.TransactionType;
import com.project.service.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;


@RequiredArgsConstructor
public class CreateTransactionController implements ControllerService {

    private final TransactionService transactionService;


    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите тип транзакции : INCOME/EXPENDITURE)");
        String type = scanner.nextLine();
        System.out.println("Введите сумму");
        BigDecimal sum = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.println("Введите описание");
        String description = scanner.nextLine();
        System.out.println("Введите категорию расходов: продукты/одежда и  тд.");
        String category = scanner.nextLine();

        CreateTransactionDto createTransactionDto = new CreateTransactionDto(TransactionType.valueOf(type), sum, description, category);
        System.out.println(transactionService.addTransaction(createTransactionDto));

    }


    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите тип транзакции : INCOME/EXPENDITURE)");
        String type = in.readLine();
        out.println("Введите сумму");
        BigDecimal sum = new BigDecimal(in.readLine());
        out.println("Введите описание");
        String description = in.readLine();
        out.println("Введите категорию расходов: продукты/одежда и  тд.");
        String category = in.readLine();

        CreateTransactionDto createTransactionDto = new CreateTransactionDto(TransactionType.valueOf(type), sum, description, category);
        transactionService.addTransaction(createTransactionDto);
    }
}
