package com.project.controllerServlets.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.regex.Pattern;

public class EmailValidator implements MyValidator<String> {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);



    @Override
    public void validate(HttpServletRequest request) throws ValidationException, IOException {
        String userEmailParam = request.getParameter("email");
        if (userEmailParam == null || !isValidEmail(userEmailParam)) {
            throw new ValidationException("email пользователя введен не корректно");
        }
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
