package ru.clevertec.kli.receiptmachine.pojo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDto {

    private int productId;
    private int quantity;
}
