package com.project.controllerServlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.ChangeInfoDto;
import com.project.exceptions.UserNotFoundException;
import com.project.model.User;
import com.project.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Map;

@WebServlet("/blockUser")
@NoArgsConstructor
public class BlockUserServlet extends HttpServlet {

    private final UserService userService = BeanFactoryProvider.get(UserService.class);
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            String idParam = req.getParameter("email");

            User user = new User();
            try {
                user = userService.findByEmail(idParam)
                        .orElseThrow(() -> new UserNotFoundException("Такого пользователя нет в системе"));
            } catch (UserNotFoundException exception) {
                exception.getMessage();
            }

            ChangeInfoDto changeInfoDto = new ChangeInfoDto();
            changeInfoDto.setName(user.getName());
            changeInfoDto.setEmail(user.getEmail());
            changeInfoDto.setPassword(user.getPassword());
            changeInfoDto.setBlock(true);

            userService.changeInfo(changeInfoDto);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("message", "Пользователь заблокирован успешно")));
        } catch (UserNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("error", e.getMessage())));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "Ошибка при блокировке пользователя")));
        }
    }
}
