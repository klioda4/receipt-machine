package ru.clevertec.kli.receiptmachine.pojo.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptPositionDto {

    private String productName;
    private int quantity;
    private float productDiscountPercent;
    private BigDecimal productPrice;
    private BigDecimal valueWithoutDiscount;
    private BigDecimal valueWithDiscount;
}
