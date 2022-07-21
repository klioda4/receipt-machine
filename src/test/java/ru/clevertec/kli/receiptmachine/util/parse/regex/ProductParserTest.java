package ru.clevertec.kli.receiptmachine.util.parse.regex;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.kli.receiptmachine.exception.ValidationException;
import ru.clevertec.kli.receiptmachine.pojo.entity.Product;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
public class ProductParserTest {

    ProductParser parser = new ProductParser();

    @Test
    void whenParseCorrectString_thenReturnParsedItem() throws ValidationException {
        String string = "28;Apple;1.12;2;true";

        Product product = parser.parse(string);

        Product expectedProduct = Product.builder()
            .id(28)
            .name("Apple")
            .price(BigDecimal.valueOf(1.12))
            .count(2)
            .promotional(true)
            .build();
        assertThat(product, equalTo(expectedProduct));
    }

    @Test
    void whenParseIncorrectId_thenThrowException() {
        String string = "110;Apple;1.12;2;true";

        Assertions.assertThrows(ValidationException.class,
            () -> parser.parse(string));
    }

    @Test
    void whenParseIncorrectName_thenThrowException() {
        String string = "28;apple;1.12;2;true";

        Assertions.assertThrows(ValidationException.class,
            () -> parser.parse(string));
    }

    @Test
    void whenParseIncorrectPrice_thenThrowException() {
        String string = "28;Apple;1.121;2;true";

        Assertions.assertThrows(ValidationException.class,
            () -> parser.parse(string));
    }

    @Test
    void whenParseIncorrectCount_thenThrowException() {
        String string = "28;Apple;1.12;21;true";

        Assertions.assertThrows(ValidationException.class,
            () -> parser.parse(string));
    }

    @Test
    void whenParseIncorrectPromotional_thenThrowException() {
        String string = "28;Apple;1.12;2;sure";

        Assertions.assertThrows(ValidationException.class,
            () -> parser.parse(string));
    }
}
