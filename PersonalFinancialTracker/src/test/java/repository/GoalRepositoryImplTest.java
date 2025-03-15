package repository;

import com.project.model.Goal;
import com.project.repository.GoalRepository;
import com.project.repository.GoalRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GoalRepositoryImplTest extends AbstractIntegrationTest {

    private final GoalRepository goalRepository = new GoalRepositoryImpl();


    @Test
    void createGoal_NewGoal_ReturnsNull() {
        String userId = "newUser";
        Goal goal = createGoal(1,userId, "goal", BigDecimal.valueOf(100), BigDecimal.valueOf(500));

        goalRepository.createGoal(goal);

        assertTrue(goalRepository.getGoalByUser(userId).isPresent());
        assertEquals(goal, goalRepository.getGoalByUser(userId).get());
    }

    @Test
    void createGoal_ExistingGoal_ReturnsExistingGoal() {
        String userId = "existingUser";
        Goal initialGoal = createGoal(1, userId,"goal", BigDecimal.valueOf(100), BigDecimal.valueOf(500));
        goalRepository.createGoal(initialGoal);
        Goal newGoal = createGoal(2,userId,"goal", BigDecimal.valueOf(200), BigDecimal.valueOf(600));

        goalRepository.createGoal(newGoal);

        assertEquals(initialGoal, goalRepository.getGoalByUser(userId).get());
    }

    @Test
    void updateGoal_ExistingGoal_ReturnsNull() {
        String userId = "existingUser";
        Goal initialGoal = createGoal(1,userId,"goal", BigDecimal.valueOf(100), BigDecimal.valueOf(500));
        goalRepository.createGoal(initialGoal);

        Goal updatedGoal = createGoal(1,userId,"goal", BigDecimal.valueOf(200), BigDecimal.valueOf(600));
        goalRepository.updateGoal(updatedGoal);


        assertEquals(updatedGoal, goalRepository.getGoalByUser(userId).get());
    }

    @Test
    void getGoalByUser_UserExists_ReturnsGoal() {
        String userId = "testUser";
        Goal goal = createGoal(1, userId,"goal", BigDecimal.valueOf(100), BigDecimal.valueOf(500));
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