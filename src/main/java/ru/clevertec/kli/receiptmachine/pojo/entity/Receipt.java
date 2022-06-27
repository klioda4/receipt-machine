package ru.clevertec.kli.receiptmachine.pojo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt implements Entity<Integer> {

    private int id;
    private LocalDateTime time;
    private BigDecimal sumWithoutCardDiscount;
    private float discountCardPercent;
    private BigDecimal totalValue;

    private int discountCardNumber;

    @Override
    public Integer getId() {
        return id;
    }
}
