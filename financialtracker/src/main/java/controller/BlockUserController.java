package controller;

import dto.ChangeInfoDto;
import exception.UserNotFound;
import lombok.RequiredArgsConstructor;
import model.User;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@RequiredArgsConstructor
public class BlockUserController implements ControllerService {

    private final UserService userService;

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Введите email пользователя, которого хотите заблокировать");
        String email = scanner.nextLine();

        User user = userService.findByEmail(email).orElseThrow(() -> new UserNotFound("Такого пользователя нет в системе"));

        ChangeInfoDto changeInfoDto = new ChangeInfoDto();
        changeInfoDto.setName(user.getName());
        changeInfoDto.setEmail(user.getEmail());
        changeInfoDto.setPassword(user.getPassword());
        changeInfoDto.setBlock(true);

        userService.changeInfo(changeInfoDto);
    }

    @Override
    public void execute(PrintWriter out, BufferedReader in) throws IOException {
        out.println("Введите email пользователя, которого хотите заблокировать");
        String email = in.readLine();

        User user = userService.findByEmail(email).orElseThrow(() -> new UserNotFound("Такого пользователя нет в системе"));

        ChangeInfoDto changeInfoDto = new ChangeInfoDto();
        changeInfoDto.setName(user.getName());
        changeInfoDto.setEmail(user.getEmail());
        changeInfoDto.setPassword(user.getPassword());
        changeInfoDto.setBlock(true);

        userService.changeInfo(changeInfoDto);
    }
}
