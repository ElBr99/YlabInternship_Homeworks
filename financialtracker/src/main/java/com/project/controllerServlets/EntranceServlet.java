package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.EnterUserDto;
import com.project.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/entrance")
@NoArgsConstructor
public class EntranceServlet extends HttpServlet {

    private final LoginService loginService = BeanFactoryProvider.get(LoginService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("servletClass", EntranceServlet.class);
        try (BufferedReader bufferedReader = req.getReader()) {
            EnterUserDto enterUserDto = objectMapper.readValue(bufferedReader, EnterUserDto.class);

            loginService.enter(enterUserDto);

        }
    }

    private void onLoginFail(HttpServletRequest req, HttpServletResponse resp) {

        try {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "НЕ удалось войти в систему");

            resp.setContentType("json");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setCharacterEncoding("UTF-8");


            String jsonResponse = objectMapper.writeValueAsString(responseMap);
            resp.getWriter().write(jsonResponse);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void onLoginSuccess(EnterUserDto enterUserDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getSession().setAttribute("user", enterUserDto);

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Пользователь успешно залогинился!");

            response.setContentType("json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            out.print(objectMapper.writeValueAsString(enterUserDto));

            String jsonResponse = objectMapper.writeValueAsString(responseMap);

            response.getWriter().write(jsonResponse);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }


}
