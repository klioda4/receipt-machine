package ru.clevertec.kli.receiptmachine.util.parse.args;

import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.CardDto;

@Component
public class CardOptionalParser implements OptionalParser<CardDto> {

    private static final String START_STRING = "card-";

    @Override
    public Optional<CardDto> tryParse(String item) {
        if (!item.startsWith(START_STRING)) {
            return Optional.empty();
        }

        String numberString = item.substring(START_STRING.length());
        return parseWithoutPrefix(numberString);
    }

    private Optional<CardDto> parseWithoutPrefix(String str) {
        try {
            int number = Integer.parseInt(str);
            CardDto card = makeCard(number);
            return Optional.of(card);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private CardDto makeCard(int number) {
        CardDto card = new CardDto();
        card.setNumber(number);
        return card;
    }
}
