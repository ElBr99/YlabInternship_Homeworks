package com.project.utils;

import com.project.model.Role;
import com.project.model.User;
import com.project.utils.SecurityContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityContextTest {

    @AfterEach
    void tearDown() {
        SecurityContext.setCurrentUser(null);
    }

    @Test
    void setCurrentUser_SetsCurrentUserInContext() {
        User testUser = new User("Test", "test@example.com", "password", Role.USER, false);

        SecurityContext.setCurrentUser(testUser);

        assertEquals(testUser, SecurityContext.getCurrentUserInfo());
    }

    @Test
    void getCurrentUser_ReturnsCurrentUser() {
        User testUser = new User("Test", "test@example.com", "password", Role.USER, false);
        SecurityContext.setCurrentUser(testUser);

        User currentUser = SecurityContext.getCurrentUserInfo();

        assertEquals(testUser, currentUser);
    }

    @Test
    void getCurrentUserEmail_ReturnsCurrentUserEmail() {
        String expectedEmail = "test@example.com";
        User testUser = new User("Test", expectedEmail, "password", Role.USER, false);
        SecurityContext.setCurrentUser(testUser);

        String currentUserEmail = SecurityContext.getCurrentUserEmail();

        assertEquals(expectedEmail, currentUserEmail);
    }

    @Test
    void getCurrentUser_NoCurrentUserSet_ReturnsNull() {
        SecurityContext.setCurrentUser(null);

        User currentUser = SecurityContext.getCurrentUserInfo();

        assertNull(currentUser);
    }

    @Test
    void getCurrentUserEmail_NoCurrentUserSet_ThrowsException() {
        SecurityContext.setCurrentUser(null);

        assertThrows(NullPointerException.class, SecurityContext::getCurrentUserEmail);
    }
}