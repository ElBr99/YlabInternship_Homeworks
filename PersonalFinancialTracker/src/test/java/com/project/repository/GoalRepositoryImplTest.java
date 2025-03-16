package com.project.repository;

import com.project.model.Goal;
import com.project.model.Role;
import com.project.model.User;
import com.project.utils.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GoalRepositoryImplTest extends AbstractIntegrationTest {

    private final GoalRepository goalRepository = new GoalRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();


    @Test
    void createGoal_NewGoal_ReturnsNull() {

        User testUser = User.builder()
                .name("testUser")
                .email("testUser")
                .password("testUser123")
                .role(Role.valueOf("USER"))
                .blocked(false)
                .build();

        String userId = testUser.getEmail();
        Goal goal = createGoal(5, userId, "goal", BigDecimal.valueOf(100).setScale(2), BigDecimal.valueOf(500).setScale(2));

        userRepository.save(testUser);
        goalRepository.createGoal(goal);

        assertTrue(goalRepository.getGoalByUser(userId).isPresent());
        assertEquals(goal, goalRepository.getGoalByUser(userId).get());

    }

    @Test
    void createGoal_ExistingGoal_ReturnsExistingGoalAndNewGoal() {
        User testUser = User.builder()
                .name("testUser")
                .email("testUser")
                .password("testUser123")
                .role(Role.valueOf("USER"))
                .blocked(false)
                .build();
        userRepository.save(testUser);
        String userId = testUser.getEmail();

        Goal initialGoal = createGoal( 5, userId, "goal", BigDecimal.valueOf(100).setScale(2), BigDecimal.valueOf(500).setScale(2));
        goalRepository.createGoal(initialGoal);
        Goal newGoal = createGoal(6, userId, "goal", BigDecimal.valueOf(200).setScale(2), BigDecimal.valueOf(600).setScale(2));

        goalRepository.createGoal(newGoal);

        assertEquals(initialGoal, goalRepository.getGoalByUser(userId).get());

    }

    @Test
    void updateGoal_ExistingGoal_ReturnsNull() {
        User testUser = User.builder()
                .name("testUser")
                .email("testUser")
                .password("testUser123")
                .role(Role.valueOf("USER"))
                .blocked(false)
                .build();
        userRepository.save(testUser);
        String userId = testUser.getEmail();

        Goal initialGoal = createGoal(5, userId, "goal", BigDecimal.valueOf(100).setScale(2), BigDecimal.valueOf(500).setScale(2));
        goalRepository.createGoal(initialGoal);

        Goal updatedGoal = createGoal(5, userId, "goal", BigDecimal.valueOf(200).setScale(2), BigDecimal.valueOf(600).setScale(2));
        goalRepository.updateGoal(updatedGoal);


        assertEquals(updatedGoal, goalRepository.getGoalByUser(userId).get());
    }

    @Test
    void getGoalByUser_UserExists_ReturnsGoal() {
        User testUser = User.builder()
                .name("testUser")
                .email("testUser")
                .password("testUser123")
                .role(Role.valueOf("USER"))
                .blocked(false)
                .build();
        userRepository.save(testUser);
        String userId = testUser.getEmail();

        Goal goal = createGoal(5, userId, "goal", BigDecimal.valueOf(100).setScale(2), BigDecimal.valueOf(500).setScale(2));
        goalRepository.createGoal(goal);

        Optional<Goal> foundGoal = goalRepository.getGoalByUser(userId);

        assertTrue(foundGoal.isPresent());
        assertEquals(goal, foundGoal.get());
    }

    @Test
    void getGoalByUser_UserDoesNotExist_ReturnsEmptyOptional() {
        String userId = "nonExistentUser";

        Optional<Goal> foundGoal = goalRepository.getGoalByUser(userId);

        assertFalse(foundGoal.isPresent());
    }

    private Goal createGoal(int id, String userId, String goal, BigDecimal current, BigDecimal target) {
        return Goal.builder()
                .id(id)
                .userEmail(userId)
                .goal(goal)
                .current(current)
                .target(target)
                .build();
    }

}