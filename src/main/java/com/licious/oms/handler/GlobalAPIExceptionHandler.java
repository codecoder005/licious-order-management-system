package com.licious.oms.handler;

import com.licious.oms.common.AppConstants;
import com.licious.oms.dto.ExceptionResponse;
import com.licious.oms.exception.AppException;
import com.licious.oms.exception.InSufficientItemsInStockException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalAPIExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorMessage(ex.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorMessage(ex.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ExceptionResponse> handleAppException(AppException e) {
        log.error("GlobalAPIExceptionHandler::AppException {}", e.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorMessage(e.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
    @ExceptionHandler(InSufficientItemsInStockException.class)
    public ResponseEntity<ExceptionResponse> handleInSufficientItemsInStockException(InSufficientItemsInStockException e) {
        log.error("GlobalAPIExceptionHandler::InSufficientItemsInStockException {}", e.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorMessage(e.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindException(BindException ex) {
        log.error("GlobalAPIExceptionHandler::WebExchangeBindException {}", ex.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        Map<String, Object> errors = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        exceptionResponse.setErrorMessage(AppConstants.ErrorMessage.VALIDATION_CRITERIA_FAILED);
        exceptionResponse.setErrors(errors);
        exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(value = {
            EntityNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("GlobalAPIExceptionHandler::EntityNotFoundException {}", e.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorMessage(e.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageConversionException(HttpMessageConversionException e) {
        log.error("GlobalAPIExceptionHandler::InvalidFormatException {}", e.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorMessage(e.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ExceptionResponse> handleServletException(ServletException e) {
        log.error("GlobalAPIExceptionHandler::ServletException {}", e.getMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setErrorMessage(e.getMessage());
        exceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
