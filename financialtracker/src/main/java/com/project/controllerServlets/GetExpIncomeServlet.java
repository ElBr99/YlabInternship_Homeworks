package com.project.controllerServlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.DatePeriodDto;
import com.project.service.FinancialService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getExpensesAndIncomes")
@NoArgsConstructor
public class GetExpIncomeServlet extends HttpServlet {

    private final FinancialService financialService = BeanFactoryProvider.get(FinancialService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DatePeriodDto datePeriodDto = (DatePeriodDto) req.getAttribute("datePeriod");

        try {
            financialService.findExpenseForPeriod(datePeriodDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(objectMapper.writeValueAsString(datePeriodDto));
            out.flush();

        } catch (Exception exception) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print("{\"error\": \"" + exception.getMessage() + "\"}");
            out.flush();
        }
    }
}


