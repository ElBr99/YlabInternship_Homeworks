package com.project.controller;

import lombok.RequiredArgsConstructor;
import com.project.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class DeleteUserController implements ControllerService {

    private final UserService userService;

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите email пользователя, которого хотите удалить");
        String email = scanner.nextLine();
        userService.deleteAccount(email);


    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите email пользователя, которого хотите удалить");
        String email = in.readLine();
        userService.deleteAccount(email);
    }
}
