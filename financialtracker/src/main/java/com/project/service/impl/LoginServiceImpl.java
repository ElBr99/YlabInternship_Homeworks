package com.project.service.impl;

import com.project.dtos.EnterUserDto;
import com.project.exceptions.WrongCredentials;
import com.project.mapper.UserToEnterUserDtoMapper;
import lombok.RequiredArgsConstructor;
import com.project.service.LoginService;
import com.project.service.UserService;
import com.project.utils.SecurityContext;

import java.util.Optional;

@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserService userService;
    private final UserToEnterUserDtoMapper userToEnterUserDtoMapper = UserToEnterUserDtoMapper.INSTANCE;

    @Override
    public Optional<EnterUserDto> enter(EnterUserDto enterUserDto) {
       return Optional.ofNullable(userService.findByEmail(enterUserDto.getEmail())
               .filter(user -> user.getPassword().equals(enterUserDto.getPassword()))
               .map(user -> {
                   EnterUserDto enterUserDto1 = userToEnterUserDtoMapper.userToEnterUserDto(user);
                   SecurityContext.setCurrentUser(user);
                   return enterUserDto1;
               })
               .orElseThrow(() -> new WrongCredentials("Введены неверные логин и/или пароль")));
    }
}
