package com.project.service.impl;

import com.project.dtos.EnterUserDto;
import com.project.exceptions.WrongCredentials;
import com.project.model.Role;
import com.project.model.User;
import com.project.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.service.UserService;
import com.project.utils.SecurityContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {

    private final String userEmail = "test@example.com";
    private final String password = "password";
    @Mock
    private UserService userService;
    @InjectMocks
    private LoginServiceImpl loginService;

    @Test
    void enter_CorrectCredentials_SetsCurrentUserInSecurityContext() {
        EnterUserDto enterUserDto = createEnterUserDto(userEmail, password);
        User expectedUser = createUser("Test User", userEmail, password, Role.USER, false);

        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(expectedUser));

        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            loginService.enter(enterUserDto);

            mockedSecurityContext.verify(() -> SecurityContext.setCurrentUser(expectedUser));
        }
    }

    @Test
    void enter_IncorrectPassword_ThrowsWrongCredentials() {
        EnterUserDto enterUserDto = createEnterUserDto(userEmail, "wrongPassword");
        User user = createUser("Test User", userEmail, password, Role.USER, false);

        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));

        assertThrows(WrongCredentials.class, () -> loginService.enter(enterUserDto));
        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.verify(() -> SecurityContext.setCurrentUser(any()), never());
        }

    }

    @Test
    void enter_UserNotFound_ThrowsWrongCredentials() {
        EnterUserDto enterUserDto = createEnterUserDto(userEmail, password);

        when(userService.findByEmail(userEmail)).thenReturn(Optional.empty());

        assertThrows(WrongCredentials.class, () -> loginService.enter(enterUserDto));
        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.verify(() -> SecurityContext.setCurrentUser(any()), never());
        }

    }

    private EnterUserDto createEnterUserDto(String email, String password) {
        return EnterUserDto.builder()
                .email(email)
                .password(password)
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