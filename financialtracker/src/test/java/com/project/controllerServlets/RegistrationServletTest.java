package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.CreateUserDto;
import com.project.dtos.EnterUserDto;
import com.project.exceptions.UserAlreadyExists;
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

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;


public class RegistrationServletTest {

    private RegistrationServlet servlet;
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
    public static void afterEach() {
        mockStatic.close();
    }

    @BeforeEach
    public void setUp() throws Exception {
        userService = mock(UserService.class);
        objectMapper = new ObjectMapper();

        when(BeanFactoryProvider.get(ObjectMapper.class)).thenReturn(objectMapper);
        when(BeanFactoryProvider.get(UserService.class)).thenReturn(userService);

        servlet = new RegistrationServlet();

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
    public void testDoPost_UserRegisteredSuccessfully() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("user@example.com");

        when(req.getAttribute("createUser")).thenReturn(createUserDto);

        servlet.doPost(req, resp);

        verify(userService).createUser(createUserDto);
        verify(resp).setStatus(HttpServletResponse.SC_CREATED);
        verify(resp).setContentType("application/json");

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Регистрация прошла успешно");
        expectedResponse.put("createUser", objectMapper.writeValueAsString(createUserDto));

        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).write(argumentCaptor.capture());
        assertEquals(objectMapper.writeValueAsString(expectedResponse), argumentCaptor.getValue());
    }

    @Test
    public void testDoPost_UserAlreadyExists() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("existing@example.com");

        when(req.getAttribute("createUser")).thenReturn(createUserDto);
        doThrow(new UserAlreadyExists("Пользователь уже существует")).when(userService).createUser(createUserDto);

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(resp).setContentType("application/json");

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Пользователь уже существует");

        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).write(argumentCaptor.capture());
        assertEquals(objectMapper.writeValueAsString(expectedResponse), argumentCaptor.getValue());
    }

    @Test
    public void testDoPost_AnyOtherException() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("user@example.com");

        when(req.getAttribute("createUser")).thenReturn(createUserDto);
        doThrow(new UserAlreadyExists("Ошибка при создании пользователя")).when(userService).createUser(createUserDto);

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(resp).setContentType("application/json");

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Ошибка при создании пользователя");

        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).write(argumentCaptor.capture());
        assertEquals(objectMapper.writeValueAsString(expectedResponse), argumentCaptor.getValue());
    }
}