package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.model.User;
import com.project.service.FinancialService;
import com.project.utils.SecurityContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet("/viewExpenditure")
@NoArgsConstructor
public class ViewExpendituresServlet extends HttpServlet {
    private final FinancialService financialService = BeanFactoryProvider.get(FinancialService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Map<String, BigDecimal> expenseByCategories = financialService.showExpenseByCategories();
            resp.setContentType("json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(expenseByCategories));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "An unexpected error occurred")));
        }

    }
}
