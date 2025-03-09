package repository;

import model.Goal;

import java.util.Optional;

public interface GoalRepository {
    Goal createGoal(Goal goal);

    Goal updateGoal(Goal goal);

    Optional<Goal> getGoalByUser(String userId);
}
