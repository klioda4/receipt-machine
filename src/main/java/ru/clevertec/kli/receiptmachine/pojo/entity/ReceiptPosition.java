package ru.clevertec.kli.receiptmachine.pojo.entity;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 *  Represents one position (line) of a receipt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptPosition implements Entity<Integer> {

    private int id;
    private int quantity;
    private float productDiscountPercent;
    private BigDecimal productPrice;
    private BigDecimal valueWithoutDiscount;
    private BigDecimal valueWithDiscount;

    private int productId;
    private int receiptId;

    @Override
    public Integer getId() {
        return id;
    }
}
