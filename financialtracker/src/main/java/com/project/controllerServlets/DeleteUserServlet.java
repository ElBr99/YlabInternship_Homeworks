package com.project.controllerServlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.exceptions.UserNotFoundException;
import com.project.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

@WebServlet("/deleteUser")
@NoArgsConstructor
public class DeleteUserServlet extends HttpServlet {

    private final UserService userService = BeanFactoryProvider.get(UserService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idParam = req.getParameter("email");

        try {
            userService.deleteAccount(idParam);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (UserNotFoundException exception) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "User not found")));
        }
    }
}

