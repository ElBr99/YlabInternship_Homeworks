package com.project.service.impl;

import com.project.dtos.EnterUserDto;
import com.project.exceptions.WrongCredentials;
import lombok.RequiredArgsConstructor;
import com.project.service.LoginService;
import com.project.service.UserService;
import com.project.utils.SecurityContext;

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
