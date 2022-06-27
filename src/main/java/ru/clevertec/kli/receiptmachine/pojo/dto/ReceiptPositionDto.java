package ru.clevertec.kli.receiptmachine.pojo.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptPositionDto {

    private int id;
    private String productName;
    private int quantity;
    private float productDiscountPercent;
    private BigDecimal productPrice;
    private BigDecimal valueWithoutDiscount;
    private BigDecimal valueWithDiscount;
}
