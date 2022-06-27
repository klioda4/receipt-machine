package ru.clevertec.kli.receiptmachine.pojo.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.kli.receiptmachine.util.serialize.LocalDateTimeJsonDeserializer;
import ru.clevertec.kli.receiptmachine.util.serialize.LocalDateTimeJsonSerializer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptDto {

    private int id;

    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    private LocalDateTime time;
    private BigDecimal sumWithoutCardDiscount;
    private float discountCardPercent;
    private BigDecimal totalValue;

    private List<ReceiptPositionDto> positions;
}
