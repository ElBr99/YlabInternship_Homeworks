package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.CreateUserDto;
import com.project.exceptions.UserAlreadyExists;
import com.project.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/registration")
@NoArgsConstructor
public class RegistrationServlet extends HttpServlet {

    private final UserService userService = BeanFactoryProvider.get(UserService.class);

    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        CreateUserDto createUserDto = (CreateUserDto) req.getAttribute("createUser");

        try {
            userService.createUser(createUserDto);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Регистрация прошла успешно");
            responseMap.put("createUser", objectMapper.writeValueAsString(createUserDto));

            sendResponse(resp,HttpServletResponse.SC_CREATED,responseMap );

        } catch (UserAlreadyExists exception) {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", exception.getMessage());
            sendResponse(resp,HttpServletResponse.SC_BAD_REQUEST, responseMap);
        }
    }

    private void sendResponse(HttpServletResponse resp, int statusCode, Map<String, String> responseMap) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            String jsonResponse = objectMapper.writeValueAsString(responseMap);
            out.print(jsonResponse);
            out.flush();
        }
    }
}

