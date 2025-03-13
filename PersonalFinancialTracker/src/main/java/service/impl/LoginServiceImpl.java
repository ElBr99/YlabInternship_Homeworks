package service.impl;

import dto.EnterUserDto;
import exception.WrongCredentials;
import lombok.RequiredArgsConstructor;
import service.LoginService;
import service.UserService;
import utils.SecurityContext;

@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserService userService;

    @Override
    public void enter(EnterUserDto enterUserDto) {
        userService.findByEmail(enterUserDto.getEmail())
                .filter(user -> user.getPassword().equals(enterUserDto.getPassword()))
                .map(user -> {
                    SecurityContext.setCurrentUser(user);
                    return user;
                })
                .orElseThrow(() -> new WrongCredentials("Введены неверные логин и/или пароль"));

    }
}
