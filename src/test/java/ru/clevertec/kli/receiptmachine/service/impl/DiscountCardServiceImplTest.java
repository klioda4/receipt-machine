package ru.clevertec.kli.receiptmachine.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.kli.receiptmachine.pojo.dto.DiscountCardDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.parse.regex.DiscountCardParser;
import ru.clevertec.kli.receiptmachine.util.serialize.strings.impl.DiscountCardStringConverter;
import ru.clevertec.kli.receiptmachine.util.validate.impl.ValidatorImpl;

@ExtendWith(MockitoExtension.class)
public class DiscountCardServiceImplTest {

    @Mock
    private Repository<DiscountCard> repository;

    private DiscountCardServiceImpl testingService;

    List<DiscountCard> cards;

    @BeforeEach
    void setUp() {
        testingService = new DiscountCardServiceImpl(repository, new ModelMapperExt(),
            new ValidatorImpl<>(new DiscountCardStringConverter(), new DiscountCardParser()));
        cards = makeDiscountCardList();
    }

    @Test
    void whenRequestItem_thenReturnIt() {
        int id = 1;
        when(repository.get(id)).thenReturn(cards.get(id - 1));

        DiscountCardDto card = testingService.get(id);

        assertThat(card.getNumber(), is(id));
    }

    @Test
    void whenRequestForAll_thenReturnThemWithLinks() {
        when(repository.getAll()).thenReturn(cards);

        List<DiscountCardDto> list = testingService.getAll();

        assertThat(list, hasSize(cards.size()));
        for (DiscountCard card : cards) {
            assertThat(list, hasItem(hasProperty("number", is(equalTo(card.getNumber())))));
        }
    }

    private List<DiscountCard> makeDiscountCardList() {
        List<DiscountCard> cards = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            cards.add(new DiscountCard(i, i * 10));
        }
        return cards;
    }
}
