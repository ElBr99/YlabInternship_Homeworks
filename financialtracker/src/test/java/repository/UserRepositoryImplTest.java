package repository;

import model.Role;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryImplTest {

    private final UserRepositoryImpl userRepository = new UserRepositoryImpl();
    private final Map<String, User> usersMap = getUsersMap();

    @AfterEach
    void tearDown() throws Exception {
        usersMap.clear();
        User admin = new User("Admin", "admin@mail.ru", "admin", Role.ADMIN, false);
        usersMap.put(admin.getEmail(), admin);
    }

    private Map<String, User> getUsersMap() {
        try {
            Field field = UserRepositoryImpl.class.getDeclaredField("usersMap");
            field.setAccessible(true);
            return (Map<String, User>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not access usersMap", e);
        }
    }


    @Test
    void save_NewUser_AddsUserToMap() {
        User newUser = createUser("Test User", "test@example.com", "password", Role.USER, false);

        userRepository.save(newUser);

        assertTrue(usersMap.containsKey(newUser.getEmail()));
        assertEquals(newUser, usersMap.get(newUser.getEmail()));
    }

    @Test
    void save_ExistingUser_DoesNotOverwrite() {
        String existingEmail = "test@example.com";
        User initialUser = createUser("Initial User", existingEmail, "initialPassword", Role.USER, false);
        User newUser = createUser("New User", existingEmail, "newPassword", Role.ADMIN, true);

        usersMap.put(existingEmail, initialUser);

        userRepository.save(newUser);

        assertEquals(initialUser, usersMap.get(existingEmail));
    }

    @Test
    void findByEmail_UserExists_ReturnsUser() {
        String existingEmail = "test@example.com";
        User user = createUser("Test User", existingEmail, "password", Role.USER, false);
        usersMap.put(existingEmail, user);

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
        usersMap.put(existingEmail, initialUser);

        User updatedUser = createUser("Updated User", existingEmail, "updatedPassword", Role.ADMIN, true);

        userRepository.update(updatedUser);

        assertEquals(updatedUser, usersMap.get(existingEmail));
    }

    @Test
    void delete_ExistingUser_RemovesUserFromMap() {
        String existingEmail = "test@example.com";
        User userToDelete = createUser("Test User", existingEmail, "password", Role.USER, false);
        usersMap.put(existingEmail, userToDelete);

        userRepository.delete(userToDelete);

        assertFalse(usersMap.containsKey(existingEmail));
    }

    @Test
    void delete_NonExistingUser_DoesNothing() {
        User nonExistingUser = createUser("Non Existent", "nonexistent@example.com", "password", Role.USER, false);
        int initialSize = usersMap.size();

        userRepository.delete(nonExistingUser);

        assertEquals(initialSize, usersMap.size());
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