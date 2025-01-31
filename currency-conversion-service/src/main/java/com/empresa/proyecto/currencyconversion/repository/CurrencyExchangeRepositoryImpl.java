package com.empresa.proyecto.currencyconversion.repository;

import com.empresa.proyecto.currencyconversion.repository.dto.CurrencyExchange;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Repository
public class CurrencyExchangeRepositoryImpl implements CurrencyExchangeRepository {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeRepositoryImpl.class);
    private final WebClient webClient;
    private final String exchangeServiceBaseUrl;

    int count = 1;

    public CurrencyExchangeRepositoryImpl(
            WebClient.Builder webClientBuilder,
            @Value("${currency-exchange-service.base-url}") String exchangeServiceBaseUrl
    ) {
        this.exchangeServiceBaseUrl = exchangeServiceBaseUrl;
        this.webClient = webClientBuilder.baseUrl(exchangeServiceBaseUrl).build();
    }

    @Override
    @Cacheable(value = "currencyConversions", key = "#from + '-' + #to", unless = "#result == null")
    @Retry(name = "currencyExchangeService")
    @CircuitBreaker(name = "currencyExchangeService", fallbackMethod = "fallbackRetrieveExchangeValue")
    public Mono<CurrencyExchange> retrieveExchangeValue(String from, String to) {
        logger.info("Calling currency-exchange, called " + (count++) + " times at " + new Date());
        return webClient.get()
                .uri("/currency-exchange/from/{from}/to/{to}", from, to)
                .retrieve()
                .bodyToMono(CurrencyExchange.class)
                .doOnError(error -> logger.error("Error occurred while fetching exchange rate", error));
    }

    public Mono<CurrencyExchange> fallbackRetrieveExchangeValue(String from, String to, Throwable throwable) {
        logger.error("Fallback triggered due to: {}", throwable.getMessage());
        return Mono.just(new CurrencyExchange(0L, from, to, BigDecimal.ONE));
    }
}
