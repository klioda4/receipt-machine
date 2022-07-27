package ru.clevertec.kli.receiptmachine.controller.advice;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.clevertec.kli.receiptmachine.exception.RepositoryException;
import ru.clevertec.kli.receiptmachine.exception.ValidationException;
import ru.clevertec.kli.receiptmachine.pojo.dto.exception.ExceptionResponse;

@ControllerAdvice
public class MainExceptionHandler {

    private static final Logger logger = LogManager.getLogger(MainExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleOtherExceptions(Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        logger.error("Unhandled exception", e);
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled server error, try "
            + "to request later");
    }

    @ExceptionHandler({RepositoryException.class, SQLException.class})
    public ResponseEntity<ExceptionResponse> handleRepositoryExceptions(RepositoryException e) {
        logger.error("Repository exception", e);
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Database problem, try to "
            + "request later");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchElementException(
        NoSuchElementException e) {

        String message = "Element with specified id not found";
        logger.info(message);
        return getResponseEntity(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException e) {
        String message = String.format(
            "Validation of request item (%s) failed",
            e.getInputString());
        logger.info(message);
        return getResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", message);
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(HttpStatus status,
        String description) {

        return getResponseEntity(status, status.getReasonPhrase(), description);
    }

    private ResponseEntity<ExceptionResponse> getResponseEntity(HttpStatus status, String message,
        String description) {

        return new ResponseEntity<>(
            ExceptionResponse.builder()
                .status(status.value())
                .errorMessage(message)
                .description(description)
                .build(),
            status);
    }
}
