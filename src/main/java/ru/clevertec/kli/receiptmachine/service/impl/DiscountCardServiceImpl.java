package ru.clevertec.kli.receiptmachine.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.kli.receiptmachine.pojo.dto.DiscountCardDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.DiscountCardService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;

@Service
@RequiredArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {

    private final Repository<DiscountCard> repository;
    private final ModelMapperExt mapper;

    @Override public List<DiscountCardDto> getAll() {
        return mapper.mapList(repository.getAll(), DiscountCardDto.class);
    }

    @Override public DiscountCardDto get(int number) throws NoSuchElementException {
        return mapper.map(repository.get(number), DiscountCardDto.class);
    }
}
