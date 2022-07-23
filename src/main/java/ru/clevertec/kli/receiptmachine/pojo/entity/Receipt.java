package ru.clevertec.kli.receiptmachine.pojo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receipt implements Entity {

    private int id;
    private LocalDateTime time;
    private BigDecimal sumWithoutCardDiscount;
    private float discountCardPercent;
    private BigDecimal totalValue;

    private int discountCardNumber;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
