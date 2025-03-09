package controller;

import lombok.RequiredArgsConstructor;
import service.FinancialService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class ViewExpendituresController implements ControllerService {

    private final FinancialService financialService;

    @Override
    public void execute(Scanner scanner) {
        financialService.showExpenseByCategories()
                .forEach((key, value) -> System.out.println(key + value));
    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) {
        financialService.showExpenseByCategories()
                .forEach((key, value) -> out.println(key + value));
    }
}
