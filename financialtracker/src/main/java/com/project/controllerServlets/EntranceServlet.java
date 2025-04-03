package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.EnterUserDto;
import com.project.service.LoginService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        EnterUserDto enterUserDto = (EnterUserDto) req.getAttribute("user");

        loginService.enter(enterUserDto)
                .ifPresentOrElse(user -> onLoginSuccess(user, req, resp),
                        () -> onLoginFail(resp));
    }

    private void onLoginFail(HttpServletResponse resp) {

        try {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "НЕ удалось войти в систему");

            sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, responseMap);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void onLoginSuccess(EnterUserDto enterUserDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            enterUserDto = (EnterUserDto) request.getAttribute("user");

            request.getSession().setAttribute("user", enterUserDto);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Пользователь успешно залогинился!");
            responseMap.put("user", objectMapper.writeValueAsString(enterUserDto));

            sendResponse(response, HttpServletResponse.SC_OK, responseMap);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    private void sendResponse(HttpServletResponse resp, int statusCode, Map<String, String> responseMap) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(responseMap));
    }
}



