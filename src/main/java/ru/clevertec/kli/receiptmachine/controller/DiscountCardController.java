package ru.clevertec.kli.receiptmachine.controller;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.kli.receiptmachine.pojo.dto.DiscountCardDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.update.DiscountCardUpdateDto;
import ru.clevertec.kli.receiptmachine.service.DiscountCardService;
import ru.clevertec.kli.receiptmachine.util.ModelMapperExt;
import ru.clevertec.kli.receiptmachine.util.enums.PutResult;

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

    @GetMapping("/{number}")
    public DiscountCardDto get(@PathVariable int number) throws NoSuchElementException {
        return service.get(number);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DiscountCardDto add(@RequestBody DiscountCardDto discountCardDto) {
        return service.add(discountCardDto);
    }

    @PatchMapping("/{number}")
    public DiscountCardDto update(
        @PathVariable int number,
        @RequestBody DiscountCardUpdateDto updateDto) {

        return service.update(number, updateDto);
    }

    @PutMapping("/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> put(
        @PathVariable int number,
        @RequestBody DiscountCardDto discountCardDto) {

        discountCardDto.setNumber(number);
        PutResult result = service.put(discountCardDto);
        switch (result) {
            case CREATED:
                return new ResponseEntity<>(HttpStatus.CREATED);
            case UPDATED:
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            default:
                throw new IllegalStateException();
        }
    }

    @DeleteMapping("/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int number) {
        service.delete(number);
    }
}
