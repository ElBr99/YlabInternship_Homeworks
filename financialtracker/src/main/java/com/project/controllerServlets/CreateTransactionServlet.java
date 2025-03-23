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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/createTransaction")
@NoArgsConstructor
public class CreateTransactionServlet extends HttpServlet {

    private final TransactionService transactionService = BeanFactoryProvider.get(TransactionService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("servletClass", CreateTransactionServlet.class);
        try (BufferedReader bufferedReader = req.getReader()) {
            CreateTransactionDto createTransactionDto = objectMapper.readValue(bufferedReader, CreateTransactionDto.class);


            transactionService.addTransaction(createTransactionDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(objectMapper.writeValueAsString(createTransactionDto));
            out.flush();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
