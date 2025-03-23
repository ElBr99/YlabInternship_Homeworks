package com.project.service;

import com.project.dtos.EnterUserDto;
import com.project.model.User;

import java.util.Optional;

public interface LoginService {
    Optional<EnterUserDto> enter(EnterUserDto enterUserDto);


}
