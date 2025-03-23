package com.project.controllerServlets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.ChangeTransInfoDto;
import com.project.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;

public class ChangeTransactionServletTest {

    private ChangeTransactionServlet servlet;
    private TransactionService transactionService;
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
        transactionService = mock(TransactionService.class);
        objectMapper = new ObjectMapper();

        when(BeanFactoryProvider.get(TransactionService.class)).thenReturn(transactionService);
        when(BeanFactoryProvider.get(ObjectMapper.class)).thenReturn(objectMapper);

        servlet = new ChangeTransactionServlet();

        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);

        when(resp.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoPut_TransactionUpdatedSuccessfully() throws Exception {
        String jsonInput = "{\"sum\": 100, \"description\": \"Transaction description\"}";
        String idParam = "123";
        ChangeTransInfoDto changeTransInfoDto = new ChangeTransInfoDto();
        changeTransInfoDto.setSum(BigDecimal.valueOf(100));
        changeTransInfoDto.setDescription("Transaction description");

        when(req.getParameter("id")).thenReturn(idParam);
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));

        servlet.doPut(req, resp);

        verify(transactionService).changeTransactionInfo(123, changeTransInfoDto);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).setContentType("application/json");

        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).write(argumentCaptor.capture());
        assertEquals("{\"message\":\"Transaction updated successfully\"}", argumentCaptor.getValue());
    }

    @Test
    public void testDoPut_MissingIdParameter() throws Exception {
        String jsonInput = "{\"sum\": 100, \"description\": \"Transaction description\"}";

        when(req.getParameter("id")).thenReturn(null);
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));

        assertThrows(
                NumberFormatException.class,
                () -> servlet.doPut(req, resp)
        );
    }

    @Test
    public void testDoPut_InvalidId() throws Exception {
        String jsonInput = "{\"sum\": 100, \"description\": \"Transaction description\"}";
        String idParam = "invalid_id"; // некорректный ID

        when(req.getParameter("id")).thenReturn(idParam);
        when(req.getReader()).thenReturn(new BufferedReader(new StringReader(jsonInput)));

        assertThrows(
                NumberFormatException.class,
                () -> servlet.doPut(req, resp)
        );
    }

}