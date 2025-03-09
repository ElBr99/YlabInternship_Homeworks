package controller;

import dto.DatePeriodDto;
import lombok.RequiredArgsConstructor;
import service.FinancialService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

@RequiredArgsConstructor
public class GetExpIncomeController implements ControllerService {

    private final FinancialService financialService;

    @Override
    public void execute(Scanner scanner) {


        System.out.println("Введите период с в формате гг.мм.чч.");
        String dateFrom = scanner.nextLine();
        LocalDate locDateFrom = LocalDate.parse(dateFrom);
        System.out.println("Введите период по в формате гг.мм.чч.");
        String dateTo = scanner.nextLine();
        LocalDate locDateTo = LocalDate.parse(dateTo);

        DatePeriodDto datePeriodDto = new DatePeriodDto(locDateFrom, locDateTo);
        financialService.findExpenseForPeriod(datePeriodDto);

    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите период с в формате гг.мм.чч.");
        String dateFrom = in.readLine();
        LocalDate locDateFrom = LocalDate.parse(dateFrom);
        out.println("Введите период по в формате гг.мм.чч.");
        String dateTo = in.readLine();
        LocalDate locDateTo = LocalDate.parse(dateTo);

        DatePeriodDto datePeriodDto = new DatePeriodDto(locDateFrom, locDateTo);
        financialService.findExpenseForPeriod(datePeriodDto);
    }
}
