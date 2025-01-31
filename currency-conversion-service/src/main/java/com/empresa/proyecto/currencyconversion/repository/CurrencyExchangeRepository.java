package com.empresa.proyecto.currencyconversion.repository;

import com.empresa.proyecto.currencyconversion.repository.dto.CurrencyExchange;
import reactor.core.publisher.Mono;

public interface CurrencyExchangeRepository {

    Mono<CurrencyExchange> retrieveExchangeValue(String from, String to);
}
