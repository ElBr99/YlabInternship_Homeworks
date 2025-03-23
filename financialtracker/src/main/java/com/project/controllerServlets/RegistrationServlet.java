package com.project.controllerServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.CreateUserDto;
import com.project.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/registration")
@NoArgsConstructor
public class RegistrationServlet extends HttpServlet {

    private final UserService userService = BeanFactoryProvider.get(UserService.class);

    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setAttribute("servletClass", RegistrationServlet.class);
        try (BufferedReader bufferedReader = req.getReader()) {
            CreateUserDto createUserDto = objectMapper.readValue(bufferedReader, CreateUserDto.class);

            try {
                userService.createUser(createUserDto);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.setContentType("json");
                PrintWriter out = resp.getWriter();
                out.print(objectMapper.writeValueAsString(createUserDto)); // Возвращаем созданного пользователя
                out.flush();

            } catch (Exception exception) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                resp.setContentType("json");
                PrintWriter out = resp.getWriter();
                out.print("{\"error\": \"" + exception.getMessage() + "\"}");
                out.flush();
            }
        }
    }


}
