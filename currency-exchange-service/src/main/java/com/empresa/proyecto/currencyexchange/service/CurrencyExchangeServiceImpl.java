package com.empresa.proyecto.currencyexchange.service;

import com.empresa.proyecto.currencyexchange.entity.CurrencyExchange;
import com.empresa.proyecto.currencyexchange.exception.ExchangeRateNotFoundException;
import com.empresa.proyecto.currencyexchange.repository.CurrencyExchangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service class responsible for handling currency exchange rate operations.
 * <p>
 * This service interacts with the CurrencyExchangeRepository to fetch exchange rate data
 * and applies business logic to handle scenarios like missing exchange rate information.
 * </p>
 */
@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private final Logger log = LoggerFactory.getLogger(CurrencyExchangeServiceImpl.class);

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    /**
     * Retrieve the exchange value for a given currency pair.
     * <p>
     * If the exchange rate for the specified currency pair is not found, an
     * ExchangeRateNotFoundException will be thrown.
     * </p>
     *
     * @param from the source currency code (e.g., "USD").
     * @param to   the target currency code (e.g., "EUR").
     * @return a Mono emitting the CurrencyExchange object containing exchange rate details,
     * or an error if no data is found.
     */
    @Override
    public Mono<CurrencyExchange> retrieveExchangeValue(String from, String to) {
        return currencyExchangeRepository.findByFromAndTo(from, to)
                .switchIfEmpty(Mono.error(new ExchangeRateNotFoundException("Unable to find data for " + from + " to " + to)))
                .doOnSuccess(result -> log.info("Currency exchange result: {}", result))
                .doOnError(error -> log.error("Currency exchange failed: {}", error.getMessage()));
    }
}