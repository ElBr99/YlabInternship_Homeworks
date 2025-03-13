package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.TransactionType;

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
