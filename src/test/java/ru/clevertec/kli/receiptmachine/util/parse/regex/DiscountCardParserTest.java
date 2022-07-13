package ru.clevertec.kli.receiptmachine.util.parse.regex;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.kli.receiptmachine.exception.ValidationException;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class DiscountCardParserTest {

    DiscountCardParser parser = new DiscountCardParser();

    @Test
    void whenParseCorrectString_thenReturnParsedItem() throws ValidationException {
        String string = "4321;0.5";

        DiscountCard card = parser.parse(string);

        assertThat(card.getNumber(), equalTo(4321));
        assertThat(card.getDiscount(), equalTo(0.5f));
    }

    @Test
    void whenParseIncorrectString_thenThrowException() {
        String string = "0.5;10";

        Assertions.assertThrows(ValidationException.class,
            () -> parser.parse(string));
    }
}
