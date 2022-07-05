package ru.clevertec.kli.receiptmachine.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCardDto {

    private int number;
    private float discount;
}
