package ru.clevertec.kli.receiptmachine.util.parse;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.PurchaseDto;

@Component
public class PurchaseParser implements Parsable<PurchaseDto> {

    private static final int CORRECT_FIELDS_COUNT = 2;

    @Override
    public Optional<PurchaseDto> tryParse(String item) {
        String[] fieldValues = item.split("-");
        if (fieldValues.length != CORRECT_FIELDS_COUNT) {
            return Optional.empty();
        }

        return parseFields(fieldValues);
    }

    private Optional<PurchaseDto> parseFields(String[] fieldValues) {
        try {
            int productId = Integer.parseInt(fieldValues[0]);
            int quantity = Integer.parseInt(fieldValues[1]);
            return Optional.of(new PurchaseDto(productId, quantity));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
