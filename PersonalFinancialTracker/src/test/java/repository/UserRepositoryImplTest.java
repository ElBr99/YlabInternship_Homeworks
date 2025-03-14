package repository;

import model.Role;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest extends AbstractIntegrationTest {

    private final UserRepositoryImpl userRepository = new UserRepositoryImpl();


    @Test
    void save_NewUser_AddsUserToMap() {
        User newUser = createUser("Test User", "test@example.com", "password", Role.USER, false);

        userRepository.save(newUser);

        assertEquals(newUser, userRepository.findByEmail(newUser.getEmail()).get());
    }

    @Test
    void save_ExistingUser_DoesNotOverwrite() {
        String existingEmail = "test@example.com";
        User initialUser = createUser("Initial User", existingEmail, "initialPassword", Role.USER, false);
        User newUser = createUser("New User", existingEmail, "newPassword", Role.ADMIN, true);

        userRepository.save(newUser);
        assertThrows(RuntimeException.class,()->userRepository.save(newUser));

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
        String existingEmail = "test@example.com";
        User initialUser = createUser("Initial User", existingEmail, "initialPassword", Role.USER, false);
        userRepository.update(initialUser);

        User updatedUser = createUser("Updated User", existingEmail, "updatedPassword", Role.ADMIN, true);

        userRepository.update(updatedUser);

        assertEquals(updatedUser, userRepository.findByEmail(existingEmail).get());
    }

    @Test
    void delete_ExistingUser_RemovesUserFromMap() {
        String existingEmail = "test@example.com";
        User userToDelete = createUser("Test User", existingEmail, "password", Role.USER, false);
        userRepository.save(userToDelete);

        userRepository.delete(userToDelete);

        assertNull(userRepository.findByEmail(existingEmail));
    }

    @Test
    void delete_NonExistingUser_DoesNothing() {
        User nonExistingUser = createUser("Non Existent", "nonexistent@example.com", "password", Role.USER, false);

        userRepository.delete(nonExistingUser);

        assertThrows(RuntimeException.class, ()->userRepository.delete(nonExistingUser));
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