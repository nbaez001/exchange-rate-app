package com.empresa.proyecto.currencyexchange.controller;

import com.empresa.proyecto.currencyexchange.entity.CurrencyExchange;
import com.empresa.proyecto.currencyexchange.service.CurrencyExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@WebFluxTest(CurrencyExchangeController.class)
public class CurrencyExchangeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CurrencyExchangeService currencyExchangeService;

    private CurrencyExchange mockCurrencyExchange;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockCurrencyExchange = new CurrencyExchange(0L, "USD", "PEN", new BigDecimal("3.65"));
    }

    @Test
    public void testRetrieveExchangeValue() {
        String from = "USD";
        String to = "PEN";

        when(currencyExchangeService.retrieveExchangeValue(from, to))
                .thenReturn(Mono.just(mockCurrencyExchange));

        webTestClient.get()
                .uri("/currency-exchange/from/{from}/to/{to}", from, to)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.from").isEqualTo("USD")
                .jsonPath("$.to").isEqualTo("PEN")
                .jsonPath("$.exchangeRate").isEqualTo(3.65);
    }

    @Test
    public void testRetrieveExchangeValue_NotFound() {
        String from = "USD";
        String to = "PEN";

        when(currencyExchangeService.retrieveExchangeValue(from, to))
                .thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/currency-exchange/from/{from}/to/{to}", from, to)
                .exchange()
                .expectStatus().isNotFound();
    }
}