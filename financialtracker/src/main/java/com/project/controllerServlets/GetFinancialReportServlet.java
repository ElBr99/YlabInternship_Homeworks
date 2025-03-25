package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.service.FinancialService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;


@WebServlet("/viewFinReport")
@NoArgsConstructor
public class GetFinancialReportServlet extends HttpServlet {

    private final FinancialService financialService = BeanFactoryProvider.get(FinancialService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var financialReport = financialService.generateReport();
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(objectMapper.writeValueAsString(financialReport));
    }
}
