package com.empresa.proyecto.currencyexchange.controller;

import com.empresa.proyecto.currencyexchange.entity.CurrencyExchange;
import com.empresa.proyecto.currencyexchange.service.CurrencyExchangeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controller responsible for handling requests related to currency exchange rates.
 * <p>
 * This class provides an endpoint to retrieve the exchange rate between two currencies.
 * It communicates with the CurrencyExchangeService to fetch the exchange rate.
 * </p>
 */
@RestController
public class CurrencyExchangeController {

    private final Logger log = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Autowired
    private CurrencyExchangeServiceImpl currencyExchangeService;

    /**
     * Retrieves the exchange rate between two currencies.
     *
     * @param from the source currency code (e.g., "USD")
     * @param to   the target currency code (e.g., "EUR")
     * @return a Mono emitting a CurrencyExchange object containing
     * the exchange rate details between the specified currencies.
     */
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public Mono<ResponseEntity<CurrencyExchange>> retrieveExchangeValue(
            @PathVariable String from,
            @PathVariable String to) {
        log.info("Request: " + from + " to " + to);
        return currencyExchangeService.retrieveExchangeValue(from, to)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(result -> log.info("Currency exchange successful: {}", result))
                .doOnError(error -> log.error("Currency exchange failed for {} to {}", from, to));
    }
}