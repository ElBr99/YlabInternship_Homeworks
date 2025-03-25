package com.project.controllerServlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.ChangeInfoDto;
import com.project.dtos.EnterUserDto;
import com.project.model.User;
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
import java.util.Optional;

public class BlockUserServletTest {

    private BlockUserServlet servlet;
    private UserService userService;
    private ObjectMapper objectMapper;

    private HttpServletRequest req;
    private HttpServletResponse resp;
    private PrintWriter writer;

    private static MockedStatic<BeanFactoryProvider> mockStatic;

    @AfterAll
    public static void afterEach() {
        mockStatic.close();
    }

    @BeforeAll
    static void setUpBeanFactory() {
        mockStatic = mockStatic(BeanFactoryProvider.class);
    }

    @BeforeEach
    public void setUp() throws Exception {
        userService = mock(UserService.class);
        objectMapper = new ObjectMapper();

        when(BeanFactoryProvider.get(ObjectMapper.class)).thenReturn(objectMapper);
        when(BeanFactoryProvider.get(UserService.class)).thenReturn(userService);

        servlet = new BlockUserServlet();

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
    public void testDoPut_UserBlockedSuccessfully() throws Exception {
        String email = "user@example.com";
        User user = new User(email, "password123");
        ChangeInfoDto changeInfoDto = new ChangeInfoDto();
        changeInfoDto.setName(user.getName());
        changeInfoDto.setEmail(user.getEmail());
        changeInfoDto.setPassword(user.getPassword());
        changeInfoDto.setBlock(true);

        when(req.getParameter("email")).thenReturn(email);
        when(userService.findByEmail(email)).thenReturn(Optional.of(user));

        servlet.doPut(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).setContentType("application/json");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(argumentCaptor.capture());
        assertEquals("{\"message\":\"Пользователь заблокирован успешно\"}", argumentCaptor.getValue());
    }

    @Test
    public void testDoPut_UserNotFound() throws Exception {
        String email = "nonexistent@example.com";

        when(req.getParameter("email")).thenReturn(email);
        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        servlet.doPut(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(resp).setContentType("application/json");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(argumentCaptor.capture());
        assertEquals("{\"error\":\"Такого пользователя нет в системе\"}", argumentCaptor.getValue());
    }

    @Test
    public void testDoPut_AnyOtherException() throws Exception {
        String email = "user@example.com";

        when(req.getParameter("email")).thenReturn(email);
        when(userService.findByEmail(email)).thenThrow(new RuntimeException("Database Error"));

        servlet.doPut(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(resp).setContentType("application/json");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(argumentCaptor.capture());
        assertEquals("{\"error\":\"Ошибка при блокировке пользователя\"}", argumentCaptor.getValue());
    }
}