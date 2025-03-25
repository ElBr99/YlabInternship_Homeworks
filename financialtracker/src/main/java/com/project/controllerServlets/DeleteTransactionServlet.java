package com.project.controllerServlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.exceptions.TransactionNotFoundException;
import com.project.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

@WebServlet("/deleteTransaction")
@NoArgsConstructor
public class DeleteTransactionServlet extends HttpServlet {

    private final TransactionService transactionService = BeanFactoryProvider.get(TransactionService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idParam = req.getParameter("id");

        int id = Integer.parseInt(idParam);

        try {
            transactionService.deleteTransaction(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (TransactionNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "Transaction not found")));
        }
    }
}
