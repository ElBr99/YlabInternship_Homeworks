package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.aop.AuditAspect;
import com.project.dtos.CreateTransactionDto;
import com.project.dtos.EnterUserDto;
import com.project.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.io.PrintWriter;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public class CreateTransactionServletTest {

    private CreateTransactionServlet servlet;
    private TransactionService transactionService;
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
        transactionService = mock(TransactionService.class);
        objectMapper = new ObjectMapper();

        when(BeanFactoryProvider.get(ObjectMapper.class)).thenReturn(objectMapper);
        when(BeanFactoryProvider.get(TransactionService.class)).thenReturn(transactionService);

        servlet = new CreateTransactionServlet();

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
    public void testDoPost_SuccessfulTransactionCreation() throws Exception {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto();
        createTransactionDto.setSum(BigDecimal.valueOf(100.0));
        createTransactionDto.setDescription("Sample transaction");

        when(req.getAttribute("createTransaction")).thenReturn(createTransactionDto);

        servlet.doPost(req, resp);

        verify(transactionService).addTransaction(createTransactionDto);
        verify(resp).setStatus(HttpServletResponse.SC_CREATED);
        verify(resp).setContentType("application/json");

        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).print(argumentCaptor.capture());
        assertEquals(objectMapper.writeValueAsString(createTransactionDto), argumentCaptor.getValue());
    }

    @Test
    public void testDoPost_HandleException() throws Exception {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto();
        when(req.getAttribute("createTransaction")).thenReturn(createTransactionDto);
        doThrow(new RuntimeException("Transaction failed")).when(transactionService).addTransaction(createTransactionDto);

        Assertions.assertThrows(RuntimeException.class, () -> {
            servlet.doPost(req, resp);
        });

        verify(resp, never()).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer, never()).print(anyString());
    }
}