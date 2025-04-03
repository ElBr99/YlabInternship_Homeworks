package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.CreateTransactionDto;
import com.project.dtos.EnterUserDto;
import com.project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteAccountServletTest {

    private DeleteAccountServlet servlet;
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

        servlet = new DeleteAccountServlet();

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
    public void testDoDelete_SuccessfulAccountDelete() throws Exception {
        String email = "user@example.com";
        EnterUserDto enterUserDto = new EnterUserDto(email, "123");

        when(req.getSession().getAttribute("user")).thenReturn(enterUserDto);

        servlet.doDelete(req, resp);

        verify(userService).deleteAccount(email);
        verify(resp).setStatus(HttpServletResponse.SC_NO_CONTENT);
        verify(writer).write("{\"deleted\":\"Аккаунт успешно удален\"}");
    }
}

