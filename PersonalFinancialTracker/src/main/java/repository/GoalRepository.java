package repository;

import model.Goal;

import java.util.Optional;

public interface GoalRepository {
    void createGoal(Goal goal);

    void updateGoal(Goal goal);

    Optional<Goal> getGoalByUser(String userId);
}
