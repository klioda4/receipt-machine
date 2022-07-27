package ru.clevertec.kli.receiptmachine.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptPositionDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.ProductService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReceiptPositionServiceImplTest {

    @Mock
    private Repository<ReceiptPosition> repository;
    @Mock
    private ProductService productService;

    private ReceiptPositionServiceImpl testingService;

    @BeforeEach
    void setUp() {
        testingService = new ReceiptPositionServiceImpl(repository, productService,
            new ModelMapperExt());
    }

    @Test
    void whenAdd_thenAddToRepository() {
        ReceiptPosition position = makeReceiptPosition();

        testingService.add(position);

        verify(repository).add(position);
    }

    @Test
    void whenAddAll_thenAddAllItemsToRepository() {
        List<ReceiptPosition> list = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            list.add(makeReceiptPosition(i));
        }

        testingService.addAll(list);

        for (ReceiptPosition item : list) {
            verify(repository).add(item);
        }
    }

    @Test
    void whenRequestByReceiptId_thenReturnThem() {
        List<ReceiptPosition> testList = makeReceiptPositionsList();
        when(repository.getAll()).thenReturn(testList);

        List<ReceiptPosition> list = testingService.getByReceiptId(1);

        assertThat(list, hasSize(2));
        assertThat(list, hasItem(hasProperty("id", is(1))));
        assertThat(list, hasItem(hasProperty("id", is(2))));
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void whenRequestDtosByReceiptId_thenReturnDtosWithLinks() {
        List<ReceiptPosition> testList = makeReceiptPositionsList();
        ProductDto productDto1 = ProductDto.builder()
            .id(1)
            .price(BigDecimal.ONE)
            .name("dto1")
            .build();
        ProductDto productDto2 = ProductDto.builder()
            .id(2)
            .price(BigDecimal.TEN)
            .name("dto2")
            .build();
        when(repository.getAll()).thenReturn(testList);
        when(productService.get(1)).thenReturn(productDto1);
        when(productService.get(2)).thenReturn(productDto2);

        List<ReceiptPositionDto> list = testingService.getDtosByReceiptId(1);

        assertThat(list, hasSize(2));
        assertThat(list, hasItem(hasProperty("productName", is("dto1"))));
        assertThat(list, hasItem(hasProperty("productName", is("dto2"))));
        ReceiptPositionDto result1 = list.stream()
            .filter(x -> x.getProductName().equals("dto1"))
            .findFirst().get();
        ReceiptPositionDto result2 = list.stream()
            .filter(x -> x.getProductName().equals("dto2"))
            .findFirst().get();
        assertThat(result1.getProductPrice(), is(equalTo(productDto1.getPrice())));
        assertThat(result2.getProductPrice(), is(equalTo(productDto2.getPrice())));
    }

    private ReceiptPosition makeReceiptPosition() {
        return makeReceiptPosition(0);
    }

    private ReceiptPosition makeReceiptPosition(int id) {
        return ReceiptPosition.builder()
            .id(id)
            .receiptId(1)
            .quantity(1)
            .productId(1)
            .productPrice(BigDecimal.ONE)
            .valueWithDiscount(BigDecimal.ONE)
            .valueWithoutDiscount(BigDecimal.ONE)
            .build();
    }

    private List<ReceiptPosition> makeReceiptPositionsList() {
        List<ReceiptPosition> list = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            ReceiptPosition item = makeReceiptPosition(i);
            item.setReceiptId(1);
            item.setProductId(i);
            list.add(item);
        }
        for (int i = 3; i <= 5; i++) {
            ReceiptPosition item = makeReceiptPosition(i);
            item.setReceiptId(2);
            item.setProductId(i);
            list.add(item);
        }
        return list;
    }
}