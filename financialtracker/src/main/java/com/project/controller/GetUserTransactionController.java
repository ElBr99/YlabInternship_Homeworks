package com.project.controller;

import lombok.RequiredArgsConstructor;
import com.project.service.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class GetUserTransactionController implements ControllerService {

    private final TransactionService transactionService;

    @Override
    public void execute(Scanner scanner) {

        System.out.println("Введите email пользователя, транзакции которого хотите просмотреть");
        String email = scanner.nextLine();

        transactionService.viewTransactionsByUserEmail(email).forEach(transaction -> System.out.println(transaction.getTransactionType() + " "
                + transaction.getDescription() + " " + transaction.getCategory() + " " + transaction.getSum()));

    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите email пользователя, транзакции которого хотите просмотреть");
        String email = in.readLine();

        transactionService.viewTransactionsByUserEmail(email).forEach(transaction -> System.out.println(transaction.getTransactionType() + " "
                + transaction.getDescription() + " " + transaction.getCategory() + " " + transaction.getSum()));

    }
}
