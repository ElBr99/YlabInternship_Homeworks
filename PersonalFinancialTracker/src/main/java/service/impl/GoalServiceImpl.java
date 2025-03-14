package service.impl;

import dto.CreateGoalDto;
import dto.UpdateGoalDto;
import lombok.RequiredArgsConstructor;
import model.Goal;
import repository.GoalRepository;
import service.GoalService;
import utils.SecurityContext;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    @Override
    public void createGoal(CreateGoalDto createGoalDto) {

        Goal goal = Goal.builder()
                .userEmail(SecurityContext.getCurrentUserEmail())
                .goal(createGoalDto.getGoal())
                .target(createGoalDto.getTarget())
                .current(BigDecimal.ZERO)
                .build();

        goalRepository.createGoal(goal);
    }

    @Override
    public void updateGoal(String userId, UpdateGoalDto updateGoalDto) {
        Goal goal = new Goal();
        goal.setUserEmail(userId);

        if (updateGoalDto.getCurrent() != null) {
            goal.setCurrent(updateGoalDto.getCurrent());
        }

        if (updateGoalDto.getGoal() != null) {
            goal.setGoal(updateGoalDto.getGoal());
        }

        if (updateGoalDto.getTarget() != null) {
            goal.setTarget(updateGoalDto.getTarget());
        }

        goalRepository.updateGoal(goal);
    }

    @Override
    public Optional<Goal> getGoalByUser(String userId) {
        return goalRepository.getGoalByUser(userId);
    }
}
