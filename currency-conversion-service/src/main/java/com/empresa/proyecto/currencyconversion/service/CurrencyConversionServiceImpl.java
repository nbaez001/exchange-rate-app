package com.empresa.proyecto.currencyconversion.service;

import com.empresa.proyecto.currencyconversion.dto.CurrencyConversion;
import com.empresa.proyecto.currencyconversion.repository.CurrencyExchangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Service responsible for performing currency conversion operations.
 * <p>
 * This service communicates with a CurrencyExchangeProxy to retrieve
 * the exchange rate for a given source and target currency pair. It calculates
 * the converted amount based on the specified amount and the retrieved exchange rate.
 * </p>
 */

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionServiceImpl.class);

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    /**
     * Calculates the currency conversion for the given parameters.
     *
     * @param from   the source currency code (e.g., "USD")
     * @param to     the target currency code (e.g., "EUR")
     * @param amount the amount to convert
     * @return a Mono emitting a CurrencyConversion object containing
     * the details of the conversion, including the converted amount and exchange rate.
     */
    @Override
    public Mono<CurrencyConversion> calculateCurrencyConversion(String from, String to, BigDecimal amount) {
        return currencyExchangeRepository.retrieveExchangeValue(from, to)
                .map(cu -> new CurrencyConversion(
                        from, to, amount,
                        amount.multiply(cu.getExchangeRate()),
                        cu.getExchangeRate())
                )
                .doOnSuccess(result -> logger.info("Currency conversion result: {}", result))
                .doOnError(error -> logger.error("Currency conversion failed: {}", error.getMessage()));
    }
}
