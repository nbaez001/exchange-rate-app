package com.empresa.proyecto.currencyconversion.service;

import com.empresa.proyecto.currencyconversion.entity.CurrencyConversion;
import com.empresa.proyecto.currencyconversion.proxy.CurrencyExchangeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
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
public class CurrencyConversionService {

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;

    /**
     * Calculates the currency conversion for the given parameters.
     *
     * @param from   the source currency code (e.g., "USD")
     * @param to     the target currency code (e.g., "EUR")
     * @param amount the amount to convert
     * @return a Mono emitting a CurrencyConversion object containing
     * the details of the conversion, including the converted amount and exchange rate.
     */
    public Mono<CurrencyConversion> calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal amount
    ) {
        return currencyExchangeProxy.retrieveExchangeValue(from, to)
                .map(cu -> new CurrencyConversion(
                        from, to, amount,
                        amount.multiply(cu.getExchangeRate()),
                        cu.getExchangeRate())
                );
    }
}
