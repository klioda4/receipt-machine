package ru.clevertec.kli.receiptmachine.util.serialize.strings.impl;

import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;
import ru.clevertec.kli.receiptmachine.util.serialize.strings.StringConverter;

@Component
public class ProductStringConverter implements StringConverter<Product> {

    @Override
    public String convertToString(Product item) {
        return new StringBuilder()
            .append(item.getId())
            .append(';')
            .append(item.getName())
            .append(';')
            .append(item.getPrice())
            .append(';')
            .append(item.getCount())
            .append(';')
            .append(item.isPromotional())
            .toString();
    }
}
