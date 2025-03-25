package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.exceptions.UserNotFoundException;
import com.project.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

@WebServlet("/getUserTransaction")
@NoArgsConstructor
public class GetUserTransactionServlet extends HttpServlet {
    private final TransactionService transactionService = BeanFactoryProvider.get(TransactionService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("servletClass", GetUserTransactionServlet.class);
        String idParam = req.getParameter("email");


        try {
            transactionService.viewTransactionsByUserEmail(idParam);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("json");
        } catch (UserNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "User not found")));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "An unexpected error occurred")));
        }
    }
}

