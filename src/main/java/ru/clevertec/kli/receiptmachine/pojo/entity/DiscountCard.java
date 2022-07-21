package ru.clevertec.kli.receiptmachine.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountCard implements Entity<Integer> {

    private int number;
    private float discount;

    @Override
    public Integer getId() {
        return number;
    }
}
