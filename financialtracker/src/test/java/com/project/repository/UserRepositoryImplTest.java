package com.project.repository;

import com.project.model.Role;
import com.project.model.User;
import com.project.utils.AbstractIntegrationTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class UserRepositoryImplTest extends AbstractIntegrationTest {

    private final UserRepositoryImpl userRepository;


    @Test
    void save_NewUser_AddsUserToMap() {
        User newUser = createUser("Test User", "test@example.com", "password", Role.USER, false);

        userRepository.save(newUser);

        assertEquals(newUser, userRepository.findByEmail(newUser.getEmail()).get());
    }

    @Test
    void findByEmail_UserExists_ReturnsUser() {
        String existingEmail = "test@example.com";
        User user = createUser("Test User", existingEmail, "password", Role.USER, false);
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail(existingEmail);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void findByEmail_UserDoesNotExist_ReturnsEmptyOptional() {
        String nonExistentEmail = "nonexistent@example.com";

        Optional<User> foundUser = userRepository.findByEmail(nonExistentEmail);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void update_ExistingUser_UpdatesUserInMap() {
        User testUser = User.builder()
                .name("testUser")
                .email("testUser@example.com")
                .password("testUser123")
                .role(Role.valueOf("USER"))
                .blocked(false)
                .build();

        userRepository.save(testUser);

        User updatedUser = User.builder()
                .name("Updated User")
                .email(testUser.getEmail())
                .password("updatedPassword")
                .role(Role.valueOf("USER"))
                .blocked(false)
                .build();

        userRepository.update(updatedUser);
        Optional<User> foundUserOptional = userRepository.findByEmail(testUser.getEmail());

        assertTrue(foundUserOptional.isPresent());
        User foundUser = foundUserOptional.get();

        assertEquals("Updated User", foundUser.getName());
        assertEquals("updatedPassword", foundUser.getPassword());
        assertEquals(testUser.getEmail(), foundUser.getEmail());
        assertEquals(Role.valueOf("USER"), foundUser.getRole());
        assertFalse(foundUser.getBlocked());

    }

    @Test
    void delete_ExistingUser_RemovesUserFromMap() {
        String existingEmail = "test@example.com";
        User userToDelete = createUser("Test User", existingEmail, "password", Role.USER, false);
        userRepository.save(userToDelete);

        userRepository.delete(userToDelete);

        assertTrue(userRepository.findByEmail(existingEmail).isEmpty());
    }

    @Test
    void delete_NonExistingUser_DoesNothing() {
        User nonExistingUser = createUser("Non Existent", "nonexistent@example.com", "password", Role.USER, false);

        userRepository.delete(nonExistingUser);
        assertDoesNotThrow(() -> userRepository.delete(nonExistingUser));
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