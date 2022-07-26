package ru.clevertec.kli.receiptmachine.util.aop.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallLogItem {

    private LocalDateTime callTime;
    private String methodName;
    private Object[] arguments;
    private Object methodResult;
}
