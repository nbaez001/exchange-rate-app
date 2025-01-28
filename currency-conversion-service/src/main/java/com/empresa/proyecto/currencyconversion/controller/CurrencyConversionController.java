package com.empresa.proyecto.currencyconversion.controller;

import com.empresa.proyecto.currencyconversion.entity.CurrencyConversion;
import com.empresa.proyecto.currencyconversion.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Controller responsible for handling requests related to currency conversion.
 * <p>
 * This class exposes an endpoint to perform currency conversion based on the
 * specified source currency, target currency, and amount. It communicates with
 * the CurrencyConversionService to calculate the conversion rate.
 */
@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyConversionService currencyConversionService;

    /**
     * Calculates the conversion of a specified amount from one currency to another.
     *
     * @param from   the source currency code (e.g., USD, EUR)
     * @param to     the target currency code (e.g., GBP, JPY)
     * @param amount the amount to be converted
     * @return a Mono of CurrencyConversion containing the conversion details
     */
    @GetMapping("/currency-conversion/from/{from}/to/{to}/amount/{amount}")
    public Mono<ResponseEntity<CurrencyConversion>> getCurrencyConversion(@PathVariable String from,
                                                                          @PathVariable String to,
                                                                          @PathVariable BigDecimal amount) {
        return currencyConversionService.calculateCurrencyConversion(from, to, amount)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
