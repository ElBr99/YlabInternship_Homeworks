package com.project.service.impl;

import com.project.dtos.ChangeInfoDto;
import com.project.dtos.CreateUserDto;
import com.project.exceptions.UserAlreadyExists;
import com.project.exceptions.UserNotFoundException;
import com.project.model.Role;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.service.UserService;
import com.project.utils.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new UserAlreadyExists("Пользователь уже зарегистрирован");
        }

        User user = new User();
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword());
        user.setRole(Role.USER);
        user.setBlocked(false);

        //маппер мапстракт

        userRepository.save(user);
    }


    @Override
    public void changeInfo(ChangeInfoDto changeInfoDto) {

        User currentUser = userRepository.findByEmail(SecurityContext.getCurrentUserEmail())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (changeInfoDto.getName() != null) {
            currentUser.setName(changeInfoDto.getName());
        }

        if (changeInfoDto.getEmail() != null) {
            currentUser.setEmail(changeInfoDto.getEmail());
        }

        if (changeInfoDto.getPassword() != null) {
            currentUser.setPassword(changeInfoDto.getPassword());
        }

        if (changeInfoDto.getBlock() != null) {
            currentUser.setBlocked(changeInfoDto.getBlock());
        }

        userRepository.update(currentUser);
    }

    @Override
    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        userRepository.delete(user);

    }

    @Override
    public void blockUser() {
        User user = findByEmail(SecurityContext.getCurrentUserEmail())
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя нет в системе"));

        user.setBlocked(true);

        userRepository.update(user);

    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
