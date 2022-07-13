package ru.clevertec.kli.receiptmachine.util.parse.args;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.kli.receiptmachine.exception.InvalidInputStringException;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.CardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.PurchaseDto;
import ru.clevertec.kli.receiptmachine.test.setting.TestConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class ParseCartHelperTest {

    @Autowired
    ObjectProvider<ParseCartHelper> parseHelperProvider;

    ParseCartHelper testingHelper;

    @BeforeEach
    void setUp() {
        testingHelper = parseHelperProvider.getObject();
    }

    @Test
    void whenParseCorrectStrings_thenReturnThem() throws InvalidInputStringException {
        String[] arguments = {"1-1", "2-2", "card-123", "3-3"};

        testingHelper.parseItems(arguments);
        List<PurchaseDto> purchases = testingHelper.getPurchases();
        CardDto card = testingHelper.getDiscountCard();

        assertThat(purchases, is(notNullValue()));
        assertThat(purchases, hasSize(3));
        for (int i = 1; i <= 3; i++) {
            assertThat(purchases, hasItem(
                allOf(
                    hasProperty("productId", equalTo(i)),
                    hasProperty("quantity", equalTo(i)))));
        }
        assertThat(card, is(notNullValue()));
        assertThat(card.getNumber(), is(123));
    }
}
