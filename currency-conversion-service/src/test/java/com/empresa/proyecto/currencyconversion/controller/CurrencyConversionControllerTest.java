package com.empresa.proyecto.currencyconversion.controller;

import com.empresa.proyecto.currencyconversion.dto.CurrencyConversion;
import com.empresa.proyecto.currencyconversion.service.CurrencyConversionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@WebFluxTest(CurrencyConversionController.class)
public class CurrencyConversionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CurrencyConversionServiceImpl currencyConversionService;

    @InjectMocks
    private CurrencyConversionController currencyConversionController;

    private CurrencyConversion mockCurrencyConversion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockCurrencyConversion = new CurrencyConversion(
                "USD",
                "PEN",
                new BigDecimal("1.00"),
                new BigDecimal("3.65"),
                new BigDecimal("3.65")
        );
    }

    @Test
    public void testCalculateCurrencyConversion() {
        String from = "USD";
        String to = "PEN";
        BigDecimal amount = new BigDecimal("1.0");

        when(currencyConversionService.calculateCurrencyConversion(from, to, amount))
                .thenReturn(Mono.just(mockCurrencyConversion));

        webTestClient.get()
                .uri("/currency-conversion/from/{from}/to/{to}/amount/{amount}", from, to, amount)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.from").isEqualTo("USD")
                .jsonPath("$.to").isEqualTo("PEN")
                .jsonPath("$.amount").isEqualTo(1.00)
                .jsonPath("$.calculatedAmount").isEqualTo(3.65);
    }

    @Test
    public void testCalculateCurrencyConversion_NotFound() {
        String from = "USD";
        String to = "PEN";
        BigDecimal amount = new BigDecimal("1.00");

        when(currencyConversionService.calculateCurrencyConversion(from, to, amount))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/currency-conversion/from/{from}/to/{to}/amount/{amount}", from, to, amount)
                .exchange()
                .expectStatus().isNotFound();
    }
}