package controller;

import dto.EnterUserDto;
import lombok.RequiredArgsConstructor;
import service.LoginService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class EntranceController implements ControllerService {

    private final LoginService loginService;

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите email");
        String login = scanner.nextLine();
        System.out.println("Введите пароль");
        String password = scanner.nextLine();

        EnterUserDto enterUserDto = new EnterUserDto(login, password);
        loginService.enter(enterUserDto);

    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите email");
        String login = in.readLine();
        out.println("Введите пароль");
        String password = in.readLine();

        EnterUserDto enterUserDto = new EnterUserDto(login, password);
        loginService.enter(enterUserDto);
    }
}
