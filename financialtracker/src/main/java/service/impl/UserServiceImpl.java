package service.impl;

import dto.ChangeInfoDto;
import dto.CreateUserDto;
import exception.UserAlreadyExists;
import exception.UserNotFound;
import lombok.RequiredArgsConstructor;
import model.Role;
import model.User;
import repository.UserRepository;
import service.UserService;
import utils.SecurityContext;

import java.util.Optional;

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

        userRepository.save(user);
    }


    @Override
    public void changeInfo(ChangeInfoDto changeInfoDto) {
        User currentUser = SecurityContext.getCurrentUserInfo();

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
        User user;
        if (userRepository.findByEmail(email).isPresent()) {
            user = userRepository.findByEmail(email).get();
        } else {
            throw new UserNotFound("Пользователь не найден");
        }
        userRepository.delete(user);

    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
