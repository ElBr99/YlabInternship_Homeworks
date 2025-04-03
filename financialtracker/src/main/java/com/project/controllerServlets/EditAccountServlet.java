package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.ChangeInfoDto;
import com.project.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/editAccount")
@NoArgsConstructor
public class EditAccountServlet extends HttpServlet {

    private final UserService userService = BeanFactoryProvider.get(UserService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ChangeInfoDto changeInfoDto = (ChangeInfoDto) req.getAttribute("editAccount");

        try {
            userService.changeInfo(changeInfoDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(objectMapper.writeValueAsString(changeInfoDto));
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

