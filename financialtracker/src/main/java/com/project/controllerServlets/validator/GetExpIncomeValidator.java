package com.project.controllerServlets.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.DatePeriodDto;
import com.project.dtos.EnterUserDto;
import com.project.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class GetExpIncomeValidator implements MyValidator<DatePeriodDto> {

    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);

    @Override
    public void validate(HttpServletRequest request) throws ValidationException, IOException {

        BufferedReader bufferedReader = request.getReader();
        DatePeriodDto datePeriodDto = objectMapper.readValue(bufferedReader, DatePeriodDto.class);
        if (datePeriodDto.getTo()==null && datePeriodDto.getFrom()==null) {
            throw new com.project.exceptions.ValidationException("Поля должны быть заполнены для генерации доходов и расходов за период");
        }
    }
}
