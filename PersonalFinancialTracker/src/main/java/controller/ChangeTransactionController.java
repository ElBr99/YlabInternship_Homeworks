package controller;

import dto.ChangeTransInfoDto;
import lombok.RequiredArgsConstructor;
import service.TransactionService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

@RequiredArgsConstructor
public class ChangeTransactionController implements ControllerService {

    private final TransactionService transactionService;

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите номер транзакции, которую хотите изменить");
        String uuid = scanner.nextLine();
        System.out.println("Введите сумму транзакции, если хотите ее изменить. Иначе пропустить строку, нажав enter");
        BigDecimal sum = scanner.nextBigDecimal();
        System.out.println("Введите категорию, если хотите ее изменить. Иначе пропустить строку, нажав enter");
        String category = scanner.nextLine();
        System.out.println("Введите описание, если хотите его изменить. Иначе пропустить строку, нажав enter");
        String description = scanner.nextLine();

        ChangeTransInfoDto changeTransInfoDto = new ChangeTransInfoDto(
                sum,
                category.isBlank() ? null : category,
                description.isBlank() ? null : description
        );

        transactionService.changeTransactionInfo(UUID.fromString(uuid), changeTransInfoDto);
    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите номер транзакции, которую хотите изменить");
        String uuid = in.readLine();
        out.println("Введите сумму транзакции, если хотите ее изменить. Иначе пропустить строку, нажав enter");
        BigDecimal sum = new BigDecimal(in.readLine());
        out.println("Введите категорию, если хотите ее изменить. Иначе пропустить строку, нажав enter");
        String category = in.readLine();
        out.println("Введите описание, если хотите его изменить. Иначе пропустить строку, нажав enter");
        String description = in.readLine();

        ChangeTransInfoDto changeTransInfoDto = new ChangeTransInfoDto(sum, category, description);
        transactionService.changeTransactionInfo(UUID.fromString(uuid), changeTransInfoDto);
    }
}
