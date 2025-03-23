package com.project.controllerServlets.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.ChangeTransInfoDto;
import com.project.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

public class ChangeTransactionInfoValidator implements MyValidator<ChangeTransInfoDto> {
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);


    @Override
    public void validate(HttpServletRequest request) throws ValidationException, IOException {
        BufferedReader bufferedReader = request.getReader();
        ChangeTransInfoDto changeTransInfoDto = objectMapper.readValue(bufferedReader, ChangeTransInfoDto.class);
        if (changeTransInfoDto.getSum().compareTo(BigDecimal.ZERO) < 0) {
            throw new com.project.exceptions.ValidationException("сумма транзакции не может быть меньше 0");
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            throw new com.project.exceptions.ValidationException("Необходимо ввести id транзакции");
        }

    }
}
