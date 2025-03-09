package controller;

import lombok.RequiredArgsConstructor;
import service.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.UUID;

@RequiredArgsConstructor
public class DeleteTransactionController implements ControllerService {

    private final TransactionService transactionService;

    @Override
    public void execute(Scanner scanner) {

        System.out.println("Введите номер транзакции, которую хотите удалить");
        String uuid = scanner.nextLine();

        transactionService.deleteTransaction(UUID.fromString(uuid));

    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите номер транзакции, которую хотите удалить");
        String uuid = in.readLine();

        transactionService.deleteTransaction(UUID.fromString(uuid));
    }
}
