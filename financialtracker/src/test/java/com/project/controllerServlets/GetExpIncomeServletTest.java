package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.BeanFactoryProvider;
import com.project.dtos.DatePeriodDto;
import com.project.dtos.EnterUserDto;
import com.project.service.FinancialService;
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
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

public class GetExpIncomeServletTest {

    private GetExpIncomeServlet servlet;
    private FinancialService financialService;
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
        financialService = mock(FinancialService.class);
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        when(BeanFactoryProvider.get(ObjectMapper.class)).thenReturn(objectMapper);
        when(BeanFactoryProvider.get(FinancialService.class)).thenReturn(financialService);

        servlet = new GetExpIncomeServlet();

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
    public void testDoGet_SuccessfulFetchExpenses() throws Exception {
        DatePeriodDto datePeriodDto = new DatePeriodDto();
        datePeriodDto.setFrom(LocalDate.parse("2023-01-01"));
        datePeriodDto.setTo(LocalDate.parse("2023-01-31"));

        when(req.getAttribute("datePeriod")).thenReturn(datePeriodDto);

        servlet.doGet(req, resp);

        verify(financialService).findExpenseForPeriod(datePeriodDto);
        verify(resp).setStatus(HttpServletResponse.SC_OK);
        verify(resp).setContentType("application/json");

        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).print(argumentCaptor.capture());
        assertEquals(objectMapper.writeValueAsString(datePeriodDto), argumentCaptor.getValue());
    }

    @Test
    public void testDoGet_HandleException() throws Exception {
        DatePeriodDto datePeriodDto = new DatePeriodDto();
        when(req.getAttribute("datePeriod")).thenReturn(datePeriodDto);
        doThrow(new RuntimeException("Failed to fetch expenses")).when(financialService).findExpenseForPeriod(datePeriodDto);

        servlet.doGet(req, resp);

        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(resp).setContentType("application/json");

        ArgumentCaptor<String> argumentCaptor = forClass(String.class);
        verify(writer).print(argumentCaptor.capture());
        assertEquals("{\"error\": \"Failed to fetch expenses\"}", argumentCaptor.getValue());
    }
}