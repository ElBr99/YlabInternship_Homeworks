package com.project.controller;

import com.project.dtos.ChangeInfoDto;
import lombok.RequiredArgsConstructor;
import com.project.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class EditAccountController implements ControllerService {

    private final UserService userService;

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите имя, если хотите его изменить. Иначе пропустить строку, нажав enter");
        String name = scanner.nextLine();
        System.out.println("Введите email, если хотите его изменить. Иначе пропустить строку, нажав enter");
        String email = scanner.nextLine();
        System.out.println("Введите пароль, если хотите его изменить. Иначе пропустить строку, нажав enter");
        String password = scanner.nextLine();

        ChangeInfoDto changeInfoDto = new ChangeInfoDto(name, email, password, false);
        userService.changeInfo(changeInfoDto);

    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите имя, если хотите его изменить. Иначе пропустить строку, нажав enter");
        String name = in.readLine();
        out.println("Введите email, если хотите его изменить. Иначе пропустить строку, нажав enter");
        String email = in.readLine();
        out.println("Введите пароль, если хотите его изменить. Иначе пропустить строку, нажав enter");
        String password = in.readLine();

        ChangeInfoDto changeInfoDto = new ChangeInfoDto(name, email, password, false);
        userService.changeInfo(changeInfoDto);
    }
}
