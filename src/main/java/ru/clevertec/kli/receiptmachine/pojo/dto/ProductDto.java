package ru.clevertec.kli.receiptmachine.pojo.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private int id;
    private String name;
    private boolean promotional;
    private BigDecimal price;
    private int count;
}
