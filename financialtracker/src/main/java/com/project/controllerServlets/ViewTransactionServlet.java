package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.model.Transaction;
import com.project.model.User;
import com.project.service.TransactionService;
import com.project.utils.SecurityContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/viewTransaction")
@NoArgsConstructor
public class ViewTransactionServlet extends HttpServlet {

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

        try {
            // Получение списка транзакций
            List<Transaction> transactionList = transactionService.viewTransactions();
            resp.setContentType("json");
            resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
            resp.getWriter().write(objectMapper.writeValueAsString(transactionList));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "An unexpected error occurred")));
        }
    }
}

