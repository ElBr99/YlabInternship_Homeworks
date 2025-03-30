package com.project.controller;

import com.project.dtos.ChangeInfoDto;
import com.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void deleteUser_ShouldCallUserServiceDeleteAccount() throws Exception {
        String email = "test@example.com";

        mockMvc.perform(delete("/api/v1/users/" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteAccount(email);
    }

    @Test
    void blockCurrentUser_ShouldCallUserServiceBlockUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).blockUser();
    }

    @Test
    void updateUser_ShouldCallUserServiceChangeInfo() throws Exception {
        ChangeInfoDto changeInfoDto = new ChangeInfoDto();

        mockMvc.perform(put("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"field\":\"value\"}")) // Пример JSON, измените в зависимости от вашего DTO
                .andExpect(status().isOk());

        verify(userService, times(1)).changeInfo(any(ChangeInfoDto.class));
    }
}