package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.ChangeInfoDto;
import com.project.dtos.EnterUserDto;
import com.project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public class EditAccountServletTest {

    private EditAccountServlet servlet;
    private UserService userService;
    private ObjectMapper objectMapper;

    private HttpServletRequest req;
    private HttpServletResponse resp;
    private PrintWriter writer;

    private static MockedStatic<BeanFactoryProvider> mockStatic;

    @BeforeAll
    static void setUpBeanFactory() {
        mockStatic = mockStatic(BeanFactoryProvider.class);
    }

    @AfterAll
    public static void closeMockStatic() {
        mockStatic.close();
    }

    @BeforeEach
    public void setUp() throws Exception {
        userService = mock(UserService.class);
        objectMapper = new ObjectMapper();

        when(BeanFactoryProvider.get(ObjectMapper.class)).thenReturn(objectMapper);
        when(BeanFactoryProvider.get(UserService.class)).thenReturn(userService);

        servlet = new EditAccountServlet();

        req = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        when(session.getAttribute("user"))
                .thenReturn(new EnterUserDto());
        when(req.getSession()).thenReturn(session);
        resp = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);

        when(resp.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoPut_SuccessfulEdit() throws Exception {
        ChangeInfoDto changeInfoDto = new ChangeInfoDto();
        changeInfoDto.setName("New Name");
        changeInfoDto.setEmail("newemail@example.com");

        when(req.getAttribute("editAccount")).thenReturn(changeInfoDto);

        servlet.doPut(req, resp);

        verify(userService).changeInfo(changeInfoDto);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).setContentType("application/json");

        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).print(argumentCaptor.capture());
        assertEquals(objectMapper.writeValueAsString(changeInfoDto), argumentCaptor.getValue());
    }

    @Test
    public void testDoPut_EditFails() throws Exception {

        ChangeInfoDto changeInfoDto = new ChangeInfoDto();
        changeInfoDto.setName("Invalid Name");

        when(req.getAttribute("editAccount")).thenReturn(changeInfoDto);
        doThrow(new RuntimeException("Update failed")).when(userService).changeInfo(changeInfoDto);


        servlet.doPut(req, resp);


        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(resp).setContentType("application/json");

        String expectedErrorMessage = "{\"error\": \"Update failed\"}";
        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).print(argumentCaptor.capture());
        assertEquals(expectedErrorMessage, argumentCaptor.getValue());
    }
}