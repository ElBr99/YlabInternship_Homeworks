package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.EnterUserDto;
import com.project.model.FinancialReport;
import com.project.service.FinancialService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GetFinancialReportServletTest {

    private GetFinancialReportServlet servlet;
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
        objectMapper = new ObjectMapper();

        when(BeanFactoryProvider.get(ObjectMapper.class)).thenReturn(objectMapper);
        when(BeanFactoryProvider.get(FinancialService.class)).thenReturn(financialService);

        servlet = new GetFinancialReportServlet();

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
    public void testDoGet_SuccessfulReportGeneration() throws Exception {
        FinancialReport expectedReport = new FinancialReport();
        when(financialService.generateReport()).thenReturn(expectedReport);

        servlet.doGet(req, resp);

        verify(resp).setContentType("application/json");
        verify(resp).setStatus(HttpServletResponse.SC_OK);

        String expectedJsonResponse = objectMapper.writeValueAsString(expectedReport);
        verify(writer).write(expectedJsonResponse);
    }
}