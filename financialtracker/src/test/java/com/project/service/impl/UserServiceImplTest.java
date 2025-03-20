package com.project.service.impl;

import com.project.dtos.ChangeInfoDto;
import com.project.dtos.CreateUserDto;
import com.project.exceptions.UserAlreadyExists;
import com.project.exceptions.UserNotFound;
import com.project.model.Role;
import com.project.model.User;
import com.project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.repository.UserRepository;
import com.project.utils.SecurityContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private final String userEmail = "test@example.com";
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_NewUser_SavesUserToRepository() {
        CreateUserDto createUserDto = createCreateUserDto("Test User", userEmail, "password");

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        userService.createUser(createUserDto);

        verify(userRepository).save(argThat(user ->
                user.getName().equals("Test User") &&
                        user.getEmail().equals(userEmail) &&
                        user.getPassword().equals("password") &&
                        user.getRole() == Role.USER &&
                        !user.getBlocked())
        );
    }

    @Test
    void createUser_ExistingUser_ThrowsUserAlreadyExists() {
        CreateUserDto createUserDto = createCreateUserDto("Test User", userEmail, "password");
        User existingUser = createUser("Existing User", userEmail, "existingPassword", Role.USER, false);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExists.class, () -> userService.createUser(createUserDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void changeInfo_ValidData_UpdatesUserAndSavesToRepository() {
        ChangeInfoDto changeInfoDto = createChangeInfoDto("New Name", "new@example.com", "newPassword", true);
        User initialUser = createUser("Old Name", userEmail, "oldPassword", Role.USER, false);

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserInfo).thenReturn(initialUser);

            userService.changeInfo(changeInfoDto);

            verify(userRepository).update(argThat(user ->
                    user.getName().equals("New Name") &&
                            user.getEmail().equals("new@example.com") &&
                            user.getPassword().equals("newPassword") &&
                            user.getBlocked())
            );
        }
    }

    @Test
    void changeInfo_OnlyNameProvided_UpdatesName() {
        ChangeInfoDto changeInfoDto = createChangeInfoDto("New Name", null, null, null);
        User initialUser = createUser("Old Name", userEmail, "oldPassword", Role.USER, false);

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserInfo).thenReturn(initialUser);

            userService.changeInfo(changeInfoDto);

            verify(userRepository).update(argThat(user ->
                    user.getName().equals("New Name") &&
                            user.getEmail().equals(userEmail) &&
                            user.getPassword().equals("oldPassword") &&
                            !user.getBlocked())
            );
        }
    }

    @Test
    void changeInfo_OnlyBlockProvided_UpdatesBlocked() {
        ChangeInfoDto changeInfoDto = createChangeInfoDto(null, null, null, true);
        User initialUser = createUser("Old Name", userEmail, "oldPassword", Role.USER, false);

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserInfo).thenReturn(initialUser);

            userService.changeInfo(changeInfoDto);

            verify(userRepository).update(argThat(user ->
                    user.getName().equals("Old Name") &&
                            user.getEmail().equals(userEmail) &&
                            user.getPassword().equals("oldPassword") &&
                            user.getBlocked())
            );
        }
    }

    @Test
    void deleteAccount_ExistingUser_DeletesUserFromRepository() {
        User userToDelete = createUser("Test User", userEmail, "password", Role.USER, false);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(userToDelete));

        userService.deleteAccount(userEmail);

        verify(userRepository).delete(userToDelete);
    }

    @Test
    void deleteAccount_NonExistingUser_ThrowsUserNotFound() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> userService.deleteAccount(userEmail));
        verify(userRepository, never()).delete(any());
    }

    @Test
    void findByEmail_UserExists_ReturnsUserFromRepository() {
        User expectedUser = createUser("Test User", userEmail, "password", Role.USER, false);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(expectedUser));

        Optional<User> actualUser = userService.findByEmail(userEmail);

        assertTrue(actualUser.isPresent());
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    void findByEmail_UserDoesNotExist_ReturnsEmptyOptionalFromRepository() {
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        Optional<User> actualUser = userService.findByEmail(userEmail);

        assertFalse(actualUser.isPresent());
    }


    private CreateUserDto createCreateUserDto(String name, String email, String password) {
        return CreateUserDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }


    private ChangeInfoDto createChangeInfoDto(String name, String email, String password, Boolean block) {
        return ChangeInfoDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .block(block)
                .build();
    }

    private User createUser(String name, String email, String password, Role role, boolean blocked) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setBlocked(blocked);
        return user;
    }
}