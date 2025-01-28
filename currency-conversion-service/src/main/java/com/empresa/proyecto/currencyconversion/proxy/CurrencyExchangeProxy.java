package com.empresa.proyecto.currencyconversion.proxy;

import com.empresa.proyecto.currencyconversion.proxy.dto.CurrencyExchange;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class CurrencyExchangeProxy {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeProxy.class);
    private final WebClient webClient;
    private final String exchangeServiceBaseUrl;

    int count = 1;

    public CurrencyExchangeProxy(
            WebClient.Builder webClientBuilder,
            @Value("${currency-exchange-service.base-url}") String exchangeServiceBaseUrl
    ) {
        this.exchangeServiceBaseUrl = exchangeServiceBaseUrl;
        this.webClient = webClientBuilder.baseUrl(exchangeServiceBaseUrl).build();
    }

    @Cacheable(value = "currencyConversions", key = "#from + '-' + #to", unless = "#result == null")
    @Retry(name = "currencyExchangeService")
    @CircuitBreaker(name = "currencyExchangeService", fallbackMethod = "fallbackRetrieveExchangeValue")
    public Mono<CurrencyExchange> retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        logger.info("Calling currency-exchange service");
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
