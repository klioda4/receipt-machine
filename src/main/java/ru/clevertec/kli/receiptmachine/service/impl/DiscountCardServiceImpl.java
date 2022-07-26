package ru.clevertec.kli.receiptmachine.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.kli.receiptmachine.pojo.dto.DiscountCardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.update.DiscountCardUpdateDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.DiscountCardService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.CallsLog;
import ru.clevertec.kli.receiptmachine.util.aop.annotation.Transactional;
import ru.clevertec.kli.receiptmachine.util.enums.PutResult;

@Service
@RequiredArgsConstructor
@CallsLog
public class DiscountCardServiceImpl implements DiscountCardService {

    private final Repository<DiscountCard> repository;
    private final ModelMapperExt mapper;

    @Override
    public List<DiscountCardDto> getAll() {
        return mapper.mapList(repository.getAll(), DiscountCardDto.class);
    }

    @Override
    public DiscountCardDto get(int number) throws NoSuchElementException {
        return mapper.map(repository.get(number), DiscountCardDto.class);
    }

    @Override
    public DiscountCardDto add(DiscountCardDto discountCardDto) {
        DiscountCard discountCard = mapper.map(discountCardDto, DiscountCard.class);
        repository.add(discountCard);
        return mapper.map(discountCard, DiscountCardDto.class);
    }

    @Override
    @Transactional
    public DiscountCardDto update(int number, DiscountCardUpdateDto updateDto)
        throws NoSuchElementException {

        DiscountCard card = repository.get(number);
        if (updateDto.getDiscount() != null) {
            card.setDiscount(updateDto.getDiscount());
        }
        repository.update(card);
        return mapper.map(card, DiscountCardDto.class);
    }

    @Override
    @Transactional
    public PutResult put(DiscountCardDto newItem) {
        DiscountCard newCard = mapper.map(newItem, DiscountCard.class);
        try {
            repository.get(newCard.getNumber());
            repository.update(newCard);
            return PutResult.UPDATED;
        } catch (NoSuchElementException e) {
            repository.add(newCard);
            return PutResult.CREATED;
        }
    }

    @Override
    public void delete(int number) {
        DiscountCard deleteCard = DiscountCard.builder()
            .number(number)
            .build();
        repository.remove(deleteCard);
    }
}
