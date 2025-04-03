package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.EnterUserDto;
import com.project.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public class EntranceServletTest {

    private EntranceServlet servlet;
    private LoginService loginService;
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
        loginService = mock(LoginService.class);
        objectMapper = new ObjectMapper();

        when(BeanFactoryProvider.get(ObjectMapper.class)).thenReturn(objectMapper);
        when(BeanFactoryProvider.get(LoginService.class)).thenReturn(loginService);

        servlet = new EntranceServlet();

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
    public void testDoPost_LoginFail() throws Exception {
        EnterUserDto enterUserDto = new EnterUserDto();
        enterUserDto.setEmail("wrong@example.com");
        enterUserDto.setPassword("wrongpassword");

        when(req.getAttribute("user")).thenReturn(enterUserDto);
        when(loginService.enter(enterUserDto)).thenReturn(Optional.empty());

        servlet.doPost(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(resp).setContentType("application/json");

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "НЕ удалось войти в систему");

        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).write(argumentCaptor.capture());
        assertEquals(objectMapper.writeValueAsString(expectedResponse), argumentCaptor.getValue());
    }

    @Test
    public void testDoPost_IOException() throws Exception {
        EnterUserDto enterUserDto = new EnterUserDto();
        enterUserDto.setEmail("user@example.com");
        enterUserDto.setPassword("password123");

        when(req.getAttribute("user")).thenReturn(enterUserDto);
        when(loginService.enter(enterUserDto)).thenReturn(Optional.of(enterUserDto));
        doThrow(new IOException("Writing error")).when(resp).getWriter();

        Assertions.assertThrows(RuntimeException.class, () -> {
            servlet.doPost(req, resp);
        });
    }
}