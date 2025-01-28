package com.empresa.proyecto.currencyexchange.repository;

import com.empresa.proyecto.currencyexchange.entity.CurrencyExchange;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CurrencyExchangeRepository extends ReactiveCrudRepository<CurrencyExchange, Long> {

    Mono<CurrencyExchange> findByFromAndTo(String from, String to);
}
