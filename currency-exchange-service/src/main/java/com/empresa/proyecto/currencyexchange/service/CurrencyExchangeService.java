package com.empresa.proyecto.currencyexchange.service;

import com.empresa.proyecto.currencyexchange.entity.CurrencyExchange;
import reactor.core.publisher.Mono;

public interface CurrencyExchangeService {

    Mono<CurrencyExchange> retrieveExchangeValue(String from, String to);
}
