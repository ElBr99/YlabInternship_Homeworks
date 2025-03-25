package com.project.controllerServlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.FilterTransactionsDto;
import com.project.model.User;
import com.project.service.TransactionService;
import com.project.utils.SecurityContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/filterTransactions")
@NoArgsConstructor
public class FilterTransactionsServlet extends HttpServlet {
    private final TransactionService transactionService = BeanFactoryProvider.get(TransactionService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SecurityContext.getCurrentUserInfo();
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "User not authenticated")));
            return;
        }

        try (BufferedReader bufferedReader = req.getReader()) {
            FilterTransactionsDto filterTransactionsDto = objectMapper.readValue(bufferedReader, FilterTransactionsDto.class);

            try {
                transactionService.filterTransactions(filterTransactionsDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                PrintWriter out = resp.getWriter();
                out.print(objectMapper.writeValueAsString(filterTransactionsDto));
                out.flush();

            } catch (Exception exception) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("json");
                PrintWriter out = resp.getWriter();
                out.print("{\"error\": \"" + exception.getMessage() + "\"}");
                out.flush();
            }
        }
    }
}
