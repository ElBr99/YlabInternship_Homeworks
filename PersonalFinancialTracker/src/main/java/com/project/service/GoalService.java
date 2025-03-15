package com.project.service;

import com.project.dtos.CreateGoalDto;
import com.project.dtos.UpdateGoalDto;
import com.project.model.Goal;

import java.util.Optional;

public interface GoalService {

    void createGoal(CreateGoalDto createGoalDto);

    void updateGoal(String userId, UpdateGoalDto updateGoalDto);

    Optional<Goal> getGoalByUser(String userId);
}
