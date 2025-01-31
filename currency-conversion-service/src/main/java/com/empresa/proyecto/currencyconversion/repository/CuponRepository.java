package com.empresa.proyecto.currencyconversion.repository;

import com.empresa.proyecto.currencyconversion.entity.Cupon;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CuponRepository extends ReactiveCrudRepository<Cupon, Long> {

    Mono<Cupon> findByCodigo(String codigo);
}
