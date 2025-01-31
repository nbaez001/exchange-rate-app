package com.empresa.proyecto.currencyexchange.service;

import com.empresa.proyecto.currencyexchange.entity.CurrencyExchange;
import com.empresa.proyecto.currencyexchange.exception.ExchangeRateNotFoundException;
import com.empresa.proyecto.currencyexchange.repository.CurrencyExchangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

class CurrencyExchangeServiceTest {

    @Mock
    private CurrencyExchangeRepository currencyExchangeRepository;

    @InjectMocks
    private CurrencyExchangeServiceImpl currencyExchangeService;

    private CurrencyExchange mockCurrencyExchange;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockCurrencyExchange = new CurrencyExchange(0L, "USD", "PEN", new BigDecimal("3.65"));
    }

    @Test
    void testRetrieveExchangeValue_Success() {
        String from = "USD";
        String to = "PEN";

        when(currencyExchangeRepository.findByFromAndTo(from, to))
                .thenReturn(Mono.just(mockCurrencyExchange));

        StepVerifier.create(currencyExchangeService.retrieveExchangeValue(from, to))
                .expectNextMatches(exchange ->
                        exchange.getFrom().equals("USD") &&
                                exchange.getTo().equals("PEN") &&
                                exchange.getExchangeRate().compareTo(new BigDecimal("3.65")) == 0)
                .verifyComplete();
    }

    @Test
    void testRetrieveExchangeValue_NotFound() {
        String from = "USD";
        String to = "GBP";

        when(currencyExchangeRepository.findByFromAndTo(from, to))
                .thenReturn(Mono.empty());

        StepVerifier.create(currencyExchangeService.retrieveExchangeValue(from, to))
                .expectErrorMatches(throwable ->
                        throwable instanceof ExchangeRateNotFoundException &&
                                throwable.getMessage().equals("Unable to find data for USD to GBP"))
                .verify();
    }
}