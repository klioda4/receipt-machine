package ru.clevertec.kli.receiptmachine.pojo.entity;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Entity<Integer> {

    private int id;
    private String name;
    private boolean promotional;
    private BigDecimal price;

    @Override
    public Integer getId() {
        return id;
    }
}
