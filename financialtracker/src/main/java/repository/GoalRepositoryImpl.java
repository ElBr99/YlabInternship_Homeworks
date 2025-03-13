package repository;

import model.Goal;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GoalRepositoryImpl implements GoalRepository {

    private static final Map<String, Goal> goalMap = new ConcurrentHashMap<>();

    @Override
    public Goal createGoal(Goal goal) {
        return goalMap.putIfAbsent(goal.getUserId(), goal);
    }

    @Override
    public Goal updateGoal(Goal goal) {
        return goalMap.put(goal.getUserId(), goal);
    }

    @Override
    public Optional<Goal> getGoalByUser(String userId) {
        return Optional.ofNullable(goalMap.get(userId));
    }
}
