package service;

import dto.ChangeInfoDto;
import dto.CreateUserDto;
import model.User;

import java.util.Optional;

public interface UserService {
    void createUser(CreateUserDto createUserDto);

    //void enter (EnterUserDto enterUserDto);
    void changeInfo(ChangeInfoDto changeInfoDto);

    void deleteAccount(String email);

    Optional<User> findByEmail(String email);

}

//при блокировании сделать update и туда передать blockedUser