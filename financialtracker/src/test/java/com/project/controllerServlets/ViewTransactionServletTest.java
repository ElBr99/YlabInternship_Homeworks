package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.BeanFactoryProvider;
import com.project.dtos.EnterUserDto;
import com.project.model.Transaction;
import com.project.model.TransactionType;
import com.project.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ViewTransactionServletTest {

    private TransactionService transactionService;
    private ObjectMapper objectMapper;
    private ViewTransactionServlet viewTransactionServlet;

    private HttpServletRequest req;
    private HttpServletResponse resp;
    private PrintWriter writer;

    private static MockedStatic<BeanFactoryProvider> mockStatic;

    @AfterAll
    public static void closeBeanFactory() {
        mockStatic.close();
    }

    @BeforeAll
    public static void setUpBeanFactory() {
        mockStatic = Mockito.mockStatic(BeanFactoryProvider.class);
    }


    @BeforeEach
    public void setUp() throws Exception {
        transactionService = mock(TransactionService.class);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        when(BeanFactoryProvider.get(ObjectMapper.class)).thenReturn(objectMapper);
        when(BeanFactoryProvider.get(TransactionService.class)).thenReturn(transactionService);

        viewTransactionServlet = new ViewTransactionServlet();

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
    public void testDoGetUserTransactions() throws Exception {
        String email = "user@example.com";
        EnterUserDto enterUserDto = new EnterUserDto(email, "123");
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = Transaction.builder()
                .id(2)
                .transactionType(TransactionType.valueOf("INCOME"))
                .sum(BigDecimal.valueOf(100))
                .userEmail(enterUserDto.getEmail())
                .description("продукты")
                .build();

        transactionList.add(transaction);

        when(req.getSession().getAttribute("user")).thenReturn(enterUserDto);
        when(transactionService.viewTransactions()).thenReturn(transactionList);

        viewTransactionServlet.doGet(req, resp);

        verify(transactionService).viewTransactions();
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).setContentType("application/json");

    }
}
