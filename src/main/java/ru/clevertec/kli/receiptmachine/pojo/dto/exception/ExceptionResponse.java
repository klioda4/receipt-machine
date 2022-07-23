package ru.clevertec.kli.receiptmachine.pojo.dto.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {

    private int status;
    private String errorMessage;
    private String description;
}
