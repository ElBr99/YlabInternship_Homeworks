package com.project.controllerServlets.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.ChangeInfoDto;
import com.project.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class EditAccountValidator implements MyValidator<ChangeInfoDto> {
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);


    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    public static boolean isValidName(String name) {

        return name.matches("[A-Z][a-zA-Z]*");
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public void validate(HttpServletRequest request) throws ValidationException, IOException {
        BufferedReader bufferedReader = request.getReader();
        ChangeInfoDto changeInfoDto = objectMapper.readValue(bufferedReader, ChangeInfoDto.class);

        if (changeInfoDto.getName() != null && !isValidName(changeInfoDto.getName())) {
            throw new com.project.exceptions.ValidationException("Имя не должно содержать символы и цифры, а также имя должно начинаться с заглавной буквы");
        }

        if (changeInfoDto.getEmail() != null && !isValidEmail(changeInfoDto.getEmail())) {
            throw new com.project.exceptions.ValidationException("email должен содержать буквы, цифры, а также разделитель @");
        }

        request.setAttribute("editAccount", changeInfoDto);
    }
}
