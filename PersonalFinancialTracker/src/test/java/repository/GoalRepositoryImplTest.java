package repository;

import model.Goal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GoalRepositoryImplTest {

    private final GoalRepository goalRepository = new GoalRepositoryImpl();

    @AfterEach
    void tearDown() throws Exception {
        clearGoalMap();
    }

    @Test
    void createGoal_NewGoal_ReturnsNull() {
        String userId = "newUser";
        Goal goal = createGoal(userId, BigDecimal.valueOf(100), BigDecimal.valueOf(500));

        Goal createdGoal = goalRepository.createGoal(goal);

        assertNull(createdGoal);
        assertTrue(goalRepository.getGoalByUser(userId).isPresent());
        assertEquals(goal, goalRepository.getGoalByUser(userId).get());
    }

    @Test
    void createGoal_ExistingGoal_ReturnsExistingGoal() {
        String userId = "existingUser";
        Goal initialGoal = createGoal(userId, BigDecimal.valueOf(100), BigDecimal.valueOf(500));
        goalRepository.createGoal(initialGoal);
        Goal newGoal = createGoal(userId, BigDecimal.valueOf(200), BigDecimal.valueOf(600));

        Goal returnedGoal = goalRepository.createGoal(newGoal);

        assertEquals(initialGoal, returnedGoal);
        assertEquals(initialGoal, goalRepository.getGoalByUser(userId).get());
    }

    @Test
    void updateGoal_ExistingGoal_ReturnsNull() {
        String userId = "existingUser";
        Goal initialGoal = createGoal(userId, BigDecimal.valueOf(100), BigDecimal.valueOf(500));
        goalRepository.createGoal(initialGoal);

        Goal updatedGoal = createGoal(userId, BigDecimal.valueOf(200), BigDecimal.valueOf(600));
        Goal returnedGoal = goalRepository.updateGoal(updatedGoal);

        assertEquals(initialGoal, returnedGoal);
        assertEquals(updatedGoal, goalRepository.getGoalByUser(userId).get());
    }

    @Test
    void getGoalByUser_UserExists_ReturnsGoal() {
        String userId = "testUser";
        Goal goal = createGoal(userId, BigDecimal.valueOf(100), BigDecimal.valueOf(500));
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

    private Goal createGoal(String userId, BigDecimal current, BigDecimal target) {
        return Goal.builder()
                .userEmail(userId)
                .current(current)
                .target(target)
                .goal("Test Goal")
                .build();
    }

    private void clearGoalMap() throws Exception {
        Field field = GoalRepositoryImpl.class.getDeclaredField("goalMap");
        ReflectionUtils.makeAccessible(field);
        Map<String, Goal> goalMap = (Map<String, Goal>) field.get(null);
        goalMap.clear();
    }
}