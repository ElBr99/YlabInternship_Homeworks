package service.impl;

import dto.CreateGoalDto;
import dto.UpdateGoalDto;
import model.Goal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GoalRepository;
import utils.SecurityContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoalServiceImplTest {

    private final String userEmail = "test@example.com";
    @Mock
    private GoalRepository goalRepository;
    @InjectMocks
    private GoalServiceImpl goalService;

    @Test
    void createGoal_ValidDto_CallsRepositoryWithGoal() {
        CreateGoalDto createGoalDto = createCreateGoalDto("My Goal", BigDecimal.valueOf(1000));
        try (MockedStatic<SecurityContext> mockedSecurityContext = mockStatic(SecurityContext.class)) {
            mockedSecurityContext.when(SecurityContext::getCurrentUserEmail).thenReturn(userEmail);
            Goal expectedGoal = createGoal(userEmail, "My Goal", BigDecimal.valueOf(1000), BigDecimal.ZERO);

            goalService.createGoal(createGoalDto);

            verify(goalRepository).createGoal(expectedGoal);
        }
    }

    @Test
    void updateGoal_ValidDto_CallsRepositoryWithUpdatedGoal() {
        UpdateGoalDto updateGoalDto = createUpdateGoalDto(BigDecimal.valueOf(500), "Updated Goal", BigDecimal.valueOf(1200));
        Goal expectedGoal = createGoal(userEmail, "Updated Goal", BigDecimal.valueOf(1200), BigDecimal.valueOf(500));

        goalService.updateGoal(userEmail, updateGoalDto);

        verify(goalRepository).updateGoal(expectedGoal);
    }

    @Test
    void updateGoal_OnlyCurrentProvided_CallsRepositoryWithUpdatedCurrent() {
        UpdateGoalDto updateGoalDto = createUpdateGoalDto(BigDecimal.valueOf(500), null, null);
        Goal expectedGoal = createGoal(userEmail, null, null, BigDecimal.valueOf(500));

        goalService.updateGoal(userEmail, updateGoalDto);

        verify(goalRepository).updateGoal(expectedGoal);
    }

    @Test
    void getGoalByUser_UserExists_ReturnsGoalFromRepository() {
        Goal expectedGoal = createGoal(userEmail, "Test Goal", BigDecimal.valueOf(100), BigDecimal.valueOf(500));

        when(goalRepository.getGoalByUser(userEmail))
                .thenReturn(Optional.of(expectedGoal));

        Optional<Goal> actualGoal = goalService.getGoalByUser(userEmail);

        assertTrue(actualGoal.isPresent());
        assertEquals(expectedGoal, actualGoal.get());
    }

    @Test
    void getGoalByUser_UserDoesNotExist_ReturnsEmptyOptional() {
        when(goalRepository.getGoalByUser(userEmail)).thenReturn(Optional.empty());

        Optional<Goal> actualGoal = goalService.getGoalByUser(userEmail);

        assertFalse(actualGoal.isPresent());
    }


    private CreateGoalDto createCreateGoalDto(String goalName, BigDecimal target) {
        return CreateGoalDto.builder()
                .goal(goalName)
                .target(target)
                .build();
    }

    private UpdateGoalDto createUpdateGoalDto(BigDecimal current, String goalName, BigDecimal target) {
        return UpdateGoalDto.builder()
                .current(current)
                .goal(goalName)
                .target(target)
                .build();
    }

    private Goal createGoal(String userId, String goalName, BigDecimal target, BigDecimal current) {
        return Goal.builder()
                .userId(userId)
                .goal(goalName)
                .target(target)
                .current(current)
                .build();
    }
}