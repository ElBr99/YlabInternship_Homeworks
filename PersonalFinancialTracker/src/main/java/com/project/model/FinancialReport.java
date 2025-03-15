package com.project.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinancialReport {

    private BigDecimal currentStatement;
    private BigDecimal expenseForPeriod;
    private BigDecimal incomeForPeriod;
    private Map<String, BigDecimal> expenseByCategory;

}
