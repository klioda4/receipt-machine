package ru.clevertec.kli.receiptmachine.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import ru.clevertec.kli.receiptmachine.exception.InvalidInputStringException;
import ru.clevertec.kli.receiptmachine.pojo.dto.DiscountCardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.CardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.request.PurchaseDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.Receipt;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.DiscountCardService;
import ru.clevertec.kli.receiptmachine.service.ProductService;
import ru.clevertec.kli.receiptmachine.service.ReceiptPositionService;
import ru.clevertec.kli.receiptmachine.service.ReceiptService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.io.print.ReceiptWriter;
import ru.clevertec.kli.receiptmachine.util.parse.args.ParseCartHelper;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private static final int COUNT_OF_PROMOTIONAL_PRODUCTS_TO_GET_DISCOUNT = 5;
    private static final float PROMOTIONAL_PRODUCT_DISCOUNT_PERCENT = 10;

    private final Repository<Receipt> repository;
    private final ProductService productService;
    private final DiscountCardService discountCardService;
    private final ReceiptPositionService receiptPositionService;

    private final ModelMapperExt mapper;
    private final ReceiptWriter fileWriter;
    private final ObjectProvider<ParseCartHelper> parseHelperProvider;

    @Override public ReceiptDto get(int id) throws NoSuchElementException {
        Receipt receipt = repository.get(id);
        return convertToDto(receipt);
    }

    @Override public List<ReceiptDto> getAll() {
        List<Receipt> receipts = repository.getAll();
        List<ReceiptDto> receiptDtos = new ArrayList<>();
        for (Receipt receipt : receipts) {
            List<ReceiptPosition> positions = receiptPositionService.getByReceiptId(
                receipt.getId());
            receiptDtos.add(convertToDto(receipt));
        }
        return receiptDtos;
    }

    @Override public ReceiptDto add(List<PurchaseDto> purchaseDtos, CardDto card)
        throws NoSuchElementException {

        List<ReceiptPosition> receiptPositions = mapper.mapList(purchaseDtos,
            ReceiptPosition.class);
        calculatePositions(receiptPositions);

        Receipt newReceipt = new Receipt();
        newReceipt.setId(new Random().nextInt(10000));
        newReceipt.setTime(LocalDateTime.now());

        DiscountCardDto discountCard = null;
        if (card != null) {
            discountCard = discountCardService.get(card.getNumber());
            newReceipt.setDiscountCardNumber(card.getNumber());
        }
        calculateReceipt(newReceipt, receiptPositions, discountCard);

        saveToRepositories(newReceipt, receiptPositions);
        ReceiptDto receiptDtos = convertToDto(newReceipt);
        fileWriter.write(receiptDtos);
        return receiptDtos;
    }

    @Override public ReceiptDto add(String[] receiptArgs) throws InvalidInputStringException {
        if (receiptArgs == null || receiptArgs.length == 0) {
            throw new IllegalArgumentException();
        }

        ParseCartHelper parseCartHelper = parseHelperProvider.getObject();
        parseCartHelper.parseItems(receiptArgs);
        List<PurchaseDto> purchases = parseCartHelper.getPurchases();
        CardDto discountCard = null;
        if (parseCartHelper.isDiscountCardPresent()) {
            discountCard = parseCartHelper.getDiscountCard();
        }

        return add(purchases, discountCard);
    }

    private void calculatePositions(List<ReceiptPosition> positions) throws NoSuchElementException {
        for (ReceiptPosition position : positions) {
            ProductDto product = productService.get(position.getProductId());
            BigDecimal valueWithoutDiscount = product.getPrice()
                .multiply(BigDecimal.valueOf(position.getQuantity()));
            position.setValueWithoutDiscount(valueWithoutDiscount);

            BigDecimal valueWithDiscount = position.getValueWithoutDiscount();
            if (product.isPromotional()
                && (position.getQuantity() > COUNT_OF_PROMOTIONAL_PRODUCTS_TO_GET_DISCOUNT)) {

                position.setProductDiscountPercent(PROMOTIONAL_PRODUCT_DISCOUNT_PERCENT);
                valueWithDiscount = valueWithDiscount
                    .multiply(getDiscountMultiplier(PROMOTIONAL_PRODUCT_DISCOUNT_PERCENT))
                    .setScale(2, RoundingMode.HALF_EVEN);
            }

            position.setValueWithDiscount(valueWithDiscount);
        }
    }

    private void calculateReceipt(Receipt receipt, List<ReceiptPosition> positions,
        DiscountCardDto discountCard) {

        BigDecimal sumWithoutCardDiscount = positions.stream()
            .map(ReceiptPosition::getValueWithDiscount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        receipt.setSumWithoutCardDiscount(sumWithoutCardDiscount);

        if (discountCard == null) {
            receipt.setTotalValue(receipt.getSumWithoutCardDiscount());
        } else {
            receipt.setDiscountCardPercent(discountCard.getDiscount());
            BigDecimal totalValue = receipt.getSumWithoutCardDiscount()
                .multiply(getDiscountMultiplier(discountCard.getDiscount()))
                .setScale(2, RoundingMode.HALF_EVEN);
            receipt.setTotalValue(totalValue);
        }
    }

    private BigDecimal getDiscountMultiplier(float discountPercent) {
        return BigDecimal.valueOf(1 - discountPercent / 100);
    }

    private void saveToRepositories(Receipt receipt, List<ReceiptPosition> positions) {
        repository.add(receipt);
        positions.forEach(p -> p.setReceiptId(receipt.getId()));
        receiptPositionService.addAll(positions);
    }

    private ReceiptDto convertToDto(Receipt receipt) {
        ReceiptDto receiptDto = mapper.map(receipt, ReceiptDto.class);
        receiptDto.setPositions(receiptPositionService.getDtosByReceiptId(receipt.getId()));
        return receiptDto;
    }
}
