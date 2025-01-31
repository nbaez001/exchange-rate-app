package com.empresa.proyecto.currencyconversion.service;

import com.empresa.proyecto.currencyconversion.dto.CurrencyConversion;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface CurrencyConversionService {

    Mono<CurrencyConversion> calculateCurrencyConversion(String from, String to, BigDecimal amount, String cupon);
}
