package ru.clevertec.kli.receiptmachine.util.serialize.strings.impl;

import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.util.serialize.strings.StringConverter;

@Component
public class DiscountCardStringConverter implements StringConverter<DiscountCard> {

    @Override
    public String convertToString(DiscountCard item) {
        return new StringBuilder()
            .append(item.getNumber())
            .append(';')
            .append(item.getDiscount())
            .toString();
    }
}
