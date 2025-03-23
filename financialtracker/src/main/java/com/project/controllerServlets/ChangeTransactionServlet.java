package com.project.controllerServlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.ChangeTransInfoDto;
import com.project.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/changeTransaction")
@NoArgsConstructor
public class ChangeTransactionServlet extends HttpServlet {

    private final TransactionService transactionService = BeanFactoryProvider.get(TransactionService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("servletClass", ChangeTransactionServlet.class);
        try (BufferedReader bufferedReader = req.getReader()) {

            ChangeTransInfoDto changeTransInfoDto = objectMapper.readValue(bufferedReader, ChangeTransInfoDto.class);

            String idParam = req.getParameter("id");

            int id;

            id = Integer.parseInt(idParam);

            transactionService.changeTransactionInfo(id, changeTransInfoDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("message", "Transaction updated successfully")));

        }
    }
}
