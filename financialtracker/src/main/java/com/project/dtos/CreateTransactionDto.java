package com.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.project.model.TransactionType;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDto {

    private TransactionType transactionType;
    private BigDecimal sum;
    private String description;
    private String category;

}
