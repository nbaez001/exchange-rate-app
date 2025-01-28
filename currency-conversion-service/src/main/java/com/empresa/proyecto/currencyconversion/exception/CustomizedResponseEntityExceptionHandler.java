package com.empresa.proyecto.currencyconversion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorDetails>> handleAllExceptions(Exception ex, ServerWebExchange exchange) {
        return Mono.just(new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), ex.getMessage(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(ExchangeRateNotFoundException.class)
    public Mono<ResponseEntity<ErrorDetails>> handleExchangeRateNotFoundExceptions(ExchangeRateNotFoundException ex, ServerWebExchange exchange) {
        return Mono.just(new ResponseEntity<>(new ErrorDetails(LocalDateTime.now(), "Currency not found", ex.getMessage()), HttpStatus.NOT_FOUND));
    }
}