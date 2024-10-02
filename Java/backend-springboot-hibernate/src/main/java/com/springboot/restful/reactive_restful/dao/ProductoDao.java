package com.springboot.restful.reactive_restful.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.springboot.restful.reactive_restful.models.documents.Producto;

import reactor.core.publisher.Mono;

public interface ProductoDao extends ReactiveCrudRepository<Producto, String> {
    public Mono<Producto> findByNombre(String nombre);


    @Query("{'nombre': ?0}")
    public Mono<Producto> obtenerPorNombre(String nombre);
}
