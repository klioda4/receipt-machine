package ru.clevertec.kli.receiptmachine.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.kli.receiptmachine.pojo.dto.DiscountCardDto;
import ru.clevertec.kli.receiptmachine.pojo.entity.DiscountCard;
import ru.clevertec.kli.receiptmachine.repository.Repository;
import ru.clevertec.kli.receiptmachine.service.DiscountCardService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;

@RestController
@RequestMapping("/discountCards")
@RequiredArgsConstructor
public class DiscountCardController {

    private final DiscountCardService service;
    private final ModelMapperExt mapper;

    @GetMapping
    private List<DiscountCardDto> getAll() {
        return service.getAll();
    }
}
