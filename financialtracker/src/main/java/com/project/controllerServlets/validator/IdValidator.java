package com.project.controllerServlets.validator;

import com.project.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class IdValidator implements MyValidator<String> {

    @Override
    public void validate(HttpServletRequest request) throws ValidationException, IOException {
        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            throw new ValidationException("id транзакции введен не корректно");
        }

    }
}
