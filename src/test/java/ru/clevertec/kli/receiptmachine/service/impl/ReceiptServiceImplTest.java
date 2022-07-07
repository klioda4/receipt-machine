package ru.clevertec.kli.receiptmachine.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.clevertec.kli.receiptmachine.exception.InvalidInputStringException;
import ru.clevertec.kli.receiptmachine.exception.NotEnoughLeftoverException;
import ru.clevertec.kli.receiptmachine.pojo.dto.DiscountCardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptPositionDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.CardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.PurchaseDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.DiscountCardService;
import ru.clevertec.kli.receiptmachine.service.ProductInnerService;
import ru.clevertec.kli.receiptmachine.service.ProductService;
import ru.clevertec.kli.receiptmachine.service.ReceiptPositionService;
import ru.clevertec.kli.receiptmachine.setting.TestConfig;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.io.print.ReceiptWriter;
import ru.clevertec.kli.receiptmachine.util.parse.args.ParseCartHelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class)
public class ReceiptServiceImplTest {

    @Mock
    private Repository<Receipt> repository;
    @Mock
    private ProductService productService;
    @Mock
    private ProductInnerService productInnerService;
    @Mock
    private DiscountCardService discountCardService;
    @Mock
    private ReceiptPositionService receiptPositionService;
    @Mock
    private ReceiptWriter fileWriter;

    @Autowired
    private ObjectProvider<ParseCartHelper> parseHelperProvider;
    @Autowired
    private ModelMapperExt modelMapper;

    private ReceiptServiceImpl testingService;

    private static final int COUNT_OF_PROMOTIONAL_PRODUCTS_TO_GET_DISCOUNT = 5;
    private static final float PROMOTIONAL_PRODUCT_DISCOUNT_PERCENT = 10;

    List<PurchaseDto> purchases;
    CardDto card;
    List<ProductDto> products;

    @BeforeEach
    void setUp() {
        testingService = new ReceiptServiceImpl(repository, productService, productInnerService,
            discountCardService, receiptPositionService, modelMapper, fileWriter,
            parseHelperProvider, COUNT_OF_PROMOTIONAL_PRODUCTS_TO_GET_DISCOUNT,
            PROMOTIONAL_PRODUCT_DISCOUNT_PERCENT);
        purchases = makePurchaseList();
        card = makeCard();
        products = makeProductList();
    }

    @Test
    void whenRequestItem_thenReturnIt() {
        int id = 1;
        when(repository.get(id)).thenReturn(makeReceipt(id));

        ReceiptDto result = testingService.get(id);

        assertThat(result.getId(), equalTo(id));
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    void whenRequestForAll_thenReturnThemWithLinks() {
        List<Receipt> receipts = new ArrayList<>();
        int length = 2;
        for (int i = 1; i <= length; i++) {
            receipts.add(makeReceipt(i));
        }
        ReceiptPositionDto position1 = makeReceiptPositionDto("pos1");
        ReceiptPositionDto position2 = makeReceiptPositionDto("pos2");
        when(repository.getAll()).thenReturn(receipts);
        when(receiptPositionService.getDtosByReceiptId(1)).thenReturn(List.of(position1));
        when(receiptPositionService.getDtosByReceiptId(2)).thenReturn(List.of(position2));

        List<ReceiptDto> resultList = testingService.getAll();

        assertThat(resultList, hasSize(length));
        for (int i = 1; i <= length; i++) {
            assertThat(resultList, hasItem(hasProperty("id", equalTo(i))));
        }

        ReceiptDto result1 = resultList.stream()
            .filter(x -> x.getId() == 1)
            .findFirst().get();
        ReceiptDto result2 = resultList.stream()
            .filter(x -> x.getId() == 2)
            .findFirst().get();
        assertThat(result1.getPositions(), hasSize(1));
        assertThat(result2.getPositions(), hasSize(1));
        assertThat(result1.getPositions().get(0).getProductName(), is(equalTo("pos1")));
        assertThat(result2.getPositions().get(0).getProductName(), is(equalTo("pos2")));
    }

    @Test
    void whenAdd_thenRequestProductServiceToReduceQuantity() throws NotEnoughLeftoverException {
        stubProductService();

        testingService.add(purchases, null);

        for (int i = 1; i <= 3; i++) {
            verify(productInnerService).writeOff(1, 1);
        }
    }

    @Test
    void whenAddWithoutCard_thenHaveNoCheckDiscount() {
        stubProductService();

        ReceiptDto receipt = testingService.add(purchases, null);

        assertThat(receipt.getSumWithoutCardDiscount(), equalTo(receipt.getTotalValue()));
    }

    @Test
    void whenAdd_thenCorrectValueCalculation() {
        stubProductService();

        ReceiptDto receipt = testingService.add(purchases, null);

        BigDecimal expectedSum = BigDecimal.valueOf(2 + 4 * 2 + 6 * 3);
        assertThat(receipt.getTotalValue(), is(equalTo(expectedSum)));
    }

    @Test
    void whenAddWithCard_thenCorrectDiscountCalculation() {
        stubProductService();
        stubDiscountCardService();

        ReceiptDto receipt = testingService.add(purchases, card);

        BigDecimal expectedSum = new BigDecimal(2 + 4 * 2 + 6 * 3);
        BigDecimal expectedDiscountedValue = expectedSum.multiply(
                BigDecimal.valueOf(0.7))
            .setScale(2, RoundingMode.HALF_EVEN);
        assertThat(receipt.getSumWithoutCardDiscount(), is(equalTo(expectedSum)));
        assertThat(receipt.getTotalValue(), is(equalTo(expectedDiscountedValue)));
    }

    @Test
    void whenAddWithLessThanSixPromotionalItems_thenHaveNoProductDiscount() {
        products.forEach(x -> x.setPromotional(true));
        stubProductService();
        stubDiscountCardService();

        ReceiptDto receipt = testingService.add(purchases, card);

        BigDecimal expectedSum = BigDecimal.valueOf(2 + 4 * 2 + 6 * 3);
        assertThat(receipt.getSumWithoutCardDiscount(), is(equalTo(expectedSum)));
        for (ReceiptPositionDto position : receipt.getPositions()) {
            assertThat(position.getValueWithDiscount(),
                is(equalTo(position.getValueWithoutDiscount())));
            assertThat(position.getProductDiscountPercent(), is(0));
        }
    }

    @Test
    void whenAddWithSixPromotionalItems_thenHaveProductDiscount() {
        products.get(0).setPromotional(true);
        products.get(0).setCount(6);
        stubProductService();
        purchases.get(0).setQuantity(6);

        ReceiptDto receipt = testingService.add(purchases, null);

        BigDecimal expectedSum = BigDecimal.valueOf(2 * 6 * 0.9 + 4 * 2 + 6 * 3)
            .setScale(2, RoundingMode.HALF_EVEN);
        assertThat(receipt.getTotalValue(), is(equalTo(expectedSum)));
    }

    @Test
    void whenAddWithString_thenCorrectCalculation() throws InvalidInputStringException {
        stubProductService();
        stubDiscountCardService();
        String[] arguments = getStringArguments(purchases, card);

        ReceiptDto receipt = testingService.add(arguments);

        BigDecimal expectedSum = new BigDecimal(2 + 4 * 2 + 6 * 3);
        BigDecimal expectedDiscountedValue = expectedSum.multiply(
                BigDecimal.valueOf(0.7))
            .setScale(2, RoundingMode.HALF_EVEN);
        assertThat(receipt.getSumWithoutCardDiscount(), is(equalTo(expectedSum)));
        assertThat(receipt.getTotalValue(), is(equalTo(expectedDiscountedValue)));
    }

    private Receipt makeReceipt(int id) {
        return Receipt.builder()
            .id(id)
            .build();
    }

    private ReceiptPositionDto makeReceiptPositionDto(String name) {
        return ReceiptPositionDto.builder()
            .productName(name)
            .build();
    }

    private List<PurchaseDto> makePurchaseList() {
        List<PurchaseDto> list = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            list.add(new PurchaseDto(i, i));
        }
        return list;
    }

    private CardDto makeCard() {
        return new CardDto(new Random().nextInt(1000) + 1);
    }

    private List<ProductDto> makeProductList() {
        List<ProductDto> products = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            products.add(ProductDto.builder()
                .id(i)
                .name("product" + i)
                .count(i)
                .price(BigDecimal.valueOf(i * 2))
                .build());
        }
        return products;
    }

    private String[] getStringArguments(List<PurchaseDto> purchases, CardDto card) {
        String[] args = new String[purchases.size()
            + (card == null ? 0 : 1)];
        for (int i = 0; i < args.length - 1; i++) {
            args[i] = purchases.get(i).getProductId()
                + "-"
                + purchases.get(i).getQuantity();
        }
        if (card != null) {
            args[args.length - 1] = "card-" + card.getNumber();
        }
        return args;
    }

    private void stubProductService() {
        for (int i = 1; i <= 3; i++) {
            when(productService.get(i)).thenReturn(products.get(i - 1));
        }
    }

    private void stubDiscountCardService() {
        DiscountCardDto discountCard = new DiscountCardDto(card.getNumber(), 30);
        when(discountCardService.get(card.getNumber()))
            .thenReturn(discountCard);
    }
}
