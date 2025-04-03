package com.project.service;

import com.project.dtos.ChangeInfoDto;
import com.project.dtos.CreateUserDto;
import com.project.model.User;

import java.util.Optional;

public interface UserService {

    void createUser(CreateUserDto createUserDto);

    void changeInfo(ChangeInfoDto changeInfoDto);

    void deleteAccount(String email);

    void blockUser (String email);

    Optional<User> findByEmail(String email);

}