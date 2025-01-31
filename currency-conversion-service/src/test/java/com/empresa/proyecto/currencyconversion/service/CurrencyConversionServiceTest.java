package com.empresa.proyecto.currencyconversion.service;

import com.empresa.proyecto.currencyconversion.dto.CurrencyConversion;
import com.empresa.proyecto.currencyconversion.repository.CurrencyExchangeRepositoryImpl;
import com.empresa.proyecto.currencyconversion.repository.dto.CurrencyExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class CurrencyConversionServiceTest {

    @Mock
    private CurrencyExchangeRepositoryImpl currencyExchangeProxy;

    @InjectMocks
    private CurrencyConversionServiceImpl currencyConversionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateCurrencyConversion() {
        Long id = 0L;
        String from = "USD";
        String to = "PEN";
        BigDecimal amount = new BigDecimal("1.00");
        BigDecimal exchangeRate = new BigDecimal("3.65");

        CurrencyExchange mockExchange = new CurrencyExchange(id, from, to, exchangeRate);

        when(currencyExchangeProxy.retrieveExchangeValue(from, to))
                .thenReturn(Mono.just(mockExchange));

        Mono<CurrencyConversion> resultMono = currencyConversionService.calculateCurrencyConversion(from, to, amount);

        CurrencyConversion result = resultMono.block();
        assertEquals(from, result.getFrom());
        assertEquals(to, result.getTo());
        assertEquals(amount, result.getAmount());
        assertEquals(exchangeRate, result.getExchangeRate());
        assertEquals(amount.multiply(exchangeRate), result.getCalculatedAmount());
    }

    @Test
    public void testCalculateCurrencyConversion_EmptyResponse() {
        String from = "USD";
        String to = "PEN";
        BigDecimal amount = new BigDecimal("1.00");

        when(currencyExchangeProxy.retrieveExchangeValue(from, to))
                .thenReturn(Mono.empty());

        Mono<CurrencyConversion> resultMono = currencyConversionService.calculateCurrencyConversion(from, to, amount);

        assertNull(resultMono.block());
    }
}