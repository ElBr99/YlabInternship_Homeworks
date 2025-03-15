package com.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.project.model.TransactionType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterTransactionsDto {

    private LocalDate from = null;
    private LocalDate to = null;
    private String category = null;
    private TransactionType transactionType = null;
}
