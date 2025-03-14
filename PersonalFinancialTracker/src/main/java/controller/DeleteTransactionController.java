package controller;

import lombok.RequiredArgsConstructor;
import service.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class DeleteTransactionController implements ControllerService {

    private final TransactionService transactionService;

    @Override
    public void execute(Scanner scanner) {

        System.out.println("Введите номер транзакции, которую хотите удалить");
        int id = scanner.nextInt();

        transactionService.deleteTransaction(id);

    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите номер транзакции, которую хотите удалить");
        int id = in.read();

        transactionService.deleteTransaction(id);
    }
}
