package ru.clevertec.kli.receiptmachine.util.io.print;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptDto;
import ru.clevertec.kli.receiptmachine.pojo.dto.ReceiptPositionDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ReceiptStreamWriterTest {

    StringWriter stringWriter;
    ReceiptStreamWriter testingWriter;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        testingWriter = new ReceiptStreamWriter(
            new PrintWriter(stringWriter));
    }

    @Test
    @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
    void whenWriteReceipt_thenHaveCorrectValuesInOutput() {
        BigDecimal productValue = BigDecimal.valueOf(2.5)
            .setScale(2);
        ReceiptPositionDto position = ReceiptPositionDto.builder()
            .quantity(1)
            .productName("product1")
            .productPrice(productValue)
            .valueWithoutDiscount(productValue)
            .valueWithDiscount(productValue)
            .build();
        List<ReceiptPositionDto> positions = List.of(position, position);
        ReceiptDto receipt = ReceiptDto.builder()
            .time(LocalDateTime.now())
            .positions(positions)
            .sumWithoutCardDiscount(BigDecimal.valueOf(5).setScale(2))
            .totalValue(BigDecimal.valueOf(4).setScale(2))
            .discountCardPercent(20f)
            .build();

        testingWriter.write(receipt);

        String result = stringWriter.toString();
        assertThat(result, is(not(blankString())));
        assertThat(result, containsString("5.00"));
        assertThat(result, containsString("2.50"));
        assertThat(result, containsString("4.00"));
    }
}
