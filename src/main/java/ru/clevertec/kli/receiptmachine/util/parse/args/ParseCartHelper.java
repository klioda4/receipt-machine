package ru.clevertec.kli.receiptmachine.util.parse.args;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.exception.InvalidInputStringException;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.CardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.PurchaseDto;

@Component
@RequiredArgsConstructor
public class ParseCartHelper {

    private final OptionalParser<PurchaseDto> purchaseParser;
    private final OptionalParser<CardDto> cardParser;

    private final List<PurchaseDto> purchases = new ArrayList<>();
    private CardDto discountCard;

    public List<PurchaseDto> getPurchases() {
        return purchases;
    }

    public CardDto getDiscountCard() {
        if (!isDiscountCardPresent()) {
            throw new NoSuchElementException("No discount card is present");
        }
        return discountCard;
    }

    public boolean isDiscountCardPresent() {
        return discountCard != null;
    }

    public void parseItems(String[] items) throws InvalidInputStringException {
        for (String item : items) {
            Optional<PurchaseDto> purchaseRequest = purchaseParser.tryParse(item);
            if (purchaseRequest.isPresent()) {
                purchases.add(purchaseRequest.get());
            } else {
                Optional<CardDto> card = cardParser.tryParse(item);
                if (card.isPresent()) {
                    this.discountCard = card.get();
                } else {
                    throw new InvalidInputStringException("String has unknown or incorrect format",
                        item);
                }
            }
        }
    }
}
