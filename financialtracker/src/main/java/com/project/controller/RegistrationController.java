package com.project.controller;

import com.project.dtos.CreateUserDto;
import lombok.RequiredArgsConstructor;
import com.project.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class RegistrationController implements ControllerService {

    private final UserService userService;

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите имя");
        String name = scanner.nextLine();
        System.out.println("Введите email");
        String email = scanner.nextLine();
        System.out.println("Введите пароль");
        String password = scanner.nextLine();

        CreateUserDto createUserDto = new CreateUserDto(name, email, password);
        userService.createUser(createUserDto);

    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите имя");
        String name = in.readLine();
        out.println("Введите email");
        String email = in.readLine();
        out.println("Введите пароль");
        String password = in.readLine();

        CreateUserDto createUserDto = new CreateUserDto(name, email, password);
        userService.createUser(createUserDto);
    }
}
