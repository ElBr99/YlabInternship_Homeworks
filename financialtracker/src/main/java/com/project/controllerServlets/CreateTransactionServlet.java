package com.project.controllerServlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.CreateTransactionDto;
import com.project.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/createTransactionServlet")
@NoArgsConstructor
public class CreateTransactionServlet extends HttpServlet {

    private final TransactionService transactionService = BeanFactoryProvider.get(TransactionService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        CreateTransactionDto createTransactionDto = (CreateTransactionDto) req.getAttribute("createTransaction");

        transactionService.addTransaction(createTransactionDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(objectMapper.writeValueAsString(createTransactionDto));
        out.flush();
    }
}
