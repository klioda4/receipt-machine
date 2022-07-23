package ru.clevertec.kli.receiptmachine.pojo.entity;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents one position (line) of a receipt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptPosition implements Entity {

    private int id;
    private int quantity;
    private float productDiscountPercent;
    private BigDecimal productPrice;
    private BigDecimal valueWithoutDiscount;
    private BigDecimal valueWithDiscount;

    private int productId;
    private int receiptId;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
