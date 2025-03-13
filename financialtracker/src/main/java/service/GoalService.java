package service;

import dto.CreateGoalDto;
import dto.UpdateGoalDto;
import model.Goal;

import java.util.Optional;

public interface GoalService {

    void createGoal(CreateGoalDto createGoalDto);

    void updateGoal(String userId, UpdateGoalDto updateGoalDto);

    Optional<Goal> getGoalByUser(String userId);
}
