package ru.clevertec.kli.receiptmachine.pojo.dto.update;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductUpdateDto {

    private String name;
    private Boolean promotional;
    private BigDecimal price;
    private Integer count;
}
