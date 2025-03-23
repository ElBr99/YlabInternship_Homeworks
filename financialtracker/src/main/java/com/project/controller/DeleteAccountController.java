package com.project.controller;

import lombok.RequiredArgsConstructor;
import com.project.model.User;
import com.project.service.UserService;
import com.project.utils.SecurityContext;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class DeleteAccountController implements ControllerService {

    private final UserService userService;

    @Override
    public void execute(Scanner scanner) {
        User user = SecurityContext.getCurrentUserInfo();
        userService.deleteAccount(user.getEmail());

        SecurityContext.clearContext();
    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) {
        User user = SecurityContext.getCurrentUserInfo();
        userService.deleteAccount(user.getEmail());
    }
}
