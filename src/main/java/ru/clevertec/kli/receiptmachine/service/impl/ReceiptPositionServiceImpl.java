package ru.clevertec.kli.receiptmachine.service.impl;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.kli.receiptmachine.pojo.dto.ProductDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptPositionDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.ReceiptPosition;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.ProductService;
import ru.clevertec.kli.receiptmachine.service.ReceiptPositionService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.spring.aop.annotation.CallsLog;

@Service
@RequiredArgsConstructor
@CallsLog
public class ReceiptPositionServiceImpl implements ReceiptPositionService {

    private final Repository<ReceiptPosition> repository;
    private final ProductService productService;
    private final ModelMapperExt mapper;

    @Override
    public void add(ReceiptPosition position) {
        position.setId(new Random().nextInt(10000));
        repository.add(position);
    }

    @Override
    public void addAll(List<ReceiptPosition> positions) {
        for (ReceiptPosition position : positions) {
            add(position);
        }
    }

    @Override
    public List<ReceiptPosition> getByReceiptId(int receiptId) {
        return repository.getAll().stream()
            .filter(item -> item.getReceiptId() == receiptId)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReceiptPositionDto> getDtosByReceiptId(int receiptId) {
        List<ReceiptPosition> positions = getByReceiptId(receiptId);
        List<ReceiptPositionDto> positionDtos = mapper.mapList(positions, ReceiptPositionDto.class);
        for (int i = 0; i < positionDtos.size(); i++) {
            int productId = positions.get(i).getProductId();
            ProductDto product = productService.get(productId);
            positionDtos.get(i)
                .setProductName(product.getName());
            positionDtos.get(i)
                .setProductPrice(product.getPrice());
        }
        return positionDtos;
    }
}
