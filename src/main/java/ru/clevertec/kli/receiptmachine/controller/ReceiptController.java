package ru.clevertec.kli.receiptmachine.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.kli.receiptmachine.exception.InvalidInputStringException;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptDto;
import ru.clevertec.kli.receiptmachine.service.ReceiptService;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService service;

    @GetMapping
    public List<ReceiptDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ReceiptDto get(@PathVariable int id) {
        return service.get(id);
    }

    @PostMapping
    public ReceiptDto add(@RequestParam String[] args) throws InvalidInputStringException {
        return service.add(args);
    }
}
