package ru.yuzhakov.subscriber.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yuzhakov.subscriber.dto.ErrorResponse;
import ru.yuzhakov.subscriber.exceptions.SubscriptionNotAllowedException;
import ru.yuzhakov.subscriber.exceptions.SubscriptionNotFoundException;
import ru.yuzhakov.subscriber.exceptions.UserNotFoundException;
import ru.yuzhakov.subscriber.exceptions.UsersNotFoundExceptions;

@ControllerAdvice
public class RestExceptionController {
    private static final Logger log = LoggerFactory.getLogger(RestExceptionController.class);

    @ExceptionHandler(UsersNotFoundExceptions.class)
    public ResponseEntity<ErrorResponse> handleUsersNotFoundException(UsersNotFoundExceptions ex) {
        log.error("Произошла ошибка: {}", ex.getMessage(), ex);  // Логируем ошибку
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("USER_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("USER_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSubscriptionNotFoundException(SubscriptionNotFoundException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("SUB_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(SubscriptionNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleSubscriptionNotAllowedException(SubscriptionNotAllowedException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("SUB_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation failed");

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("VALIDATION_ERROR", errorMessage));
    }

    // Обработка некорректного JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid JSON format";
        if (ex.getCause() instanceof JsonParseException) {
            errorMessage = "Malformed JSON";
        }

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("INVALID_REQUEST", errorMessage));
    }
}
