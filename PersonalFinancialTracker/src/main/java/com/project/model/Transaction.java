package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private int id;
    private String userEmail;
    private TransactionType transactionType;
    private BigDecimal sum;
    private OffsetDateTime dateTime;
    private String description;
    private String category;


}
