package com.project.controller;

import lombok.RequiredArgsConstructor;
import com.project.model.Transaction;
import com.project.service.TransactionService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class ViewTransactionController implements ControllerService {

    private final TransactionService transactionService;

    @Override
    public void execute(Scanner scanner) {
        List<Transaction> transactionList = transactionService.viewTransactions();
        for (Transaction transaction : transactionList) {
            System.out.println(transaction.getCategory() + transaction.getTransactionType() + transaction.getDateTime() + transaction.getSum() + transaction.getDescription());
        }
    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) {
        List<Transaction> transactionList = transactionService.viewTransactions();
        for (Transaction transaction : transactionList) {
            out.println(transaction.getCategory() + transaction.getTransactionType() + transaction.getDateTime() + transaction.getSum() + transaction.getDescription());
        }
    }
}
