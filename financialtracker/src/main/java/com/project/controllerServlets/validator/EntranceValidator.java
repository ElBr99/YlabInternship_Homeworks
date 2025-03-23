package com.project.controllerServlets.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.EnterUserDto;
import com.project.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class EntranceValidator implements MyValidator<EnterUserDto> {

    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    public void validate(HttpServletRequest request) throws ValidationException, IOException {
        BufferedReader bufferedReader = request.getReader();
        EnterUserDto enterUserDto = objectMapper.readValue(bufferedReader, EnterUserDto.class);

        if (enterUserDto.getEmail() == null || enterUserDto.getPassword() == null) {
            throw new com.project.exceptions.ValidationException("Поля логин и/или пароль не могут быть пустыми");
        }


    }
}
