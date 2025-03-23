package com.project.controllerServlets.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.BeanFactoryProvider;
import com.project.dtos.CreateTransactionDto;
import com.project.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

public class CreateTransactionValidator implements MyValidator<CreateTransactionDto> {
    private final ObjectMapper objectMapper = BeanFactoryProvider.get(ObjectMapper.class);


    @Override
    public void validate(HttpServletRequest request) throws ValidationException, IOException {
        BufferedReader bufferedReader = request.getReader();
        CreateTransactionDto createTransactionDto = objectMapper.readValue(bufferedReader, CreateTransactionDto.class);
        if (createTransactionDto.getTransactionType() == null || createTransactionDto.getCategory() == null || createTransactionDto.getDescription() == null || createTransactionDto.getSum() == null) {
            throw new com.project.exceptions.ValidationException("Не указаны данные одного из полей при заполнении");
        }
        if (!(createTransactionDto.getTransactionType().name().equals("INCOME") || createTransactionDto.getTransactionType().name().equals("EXPENDITURE"))) {
            throw new com.project.exceptions.ValidationException("Указан неверный тип транзакции. Выберите один из ууазанных типов: INCOME или EXPENDITURE");
        }
        if (createTransactionDto.getSum().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Сумма транзакции не может быть меньше 0");
        }


    }
}

