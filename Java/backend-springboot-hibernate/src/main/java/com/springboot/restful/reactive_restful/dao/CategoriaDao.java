package com.springboot.restful.reactive_restful.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.springboot.restful.reactive_restful.models.documents.Categoria;

import reactor.core.publisher.Mono;

public interface CategoriaDao extends ReactiveCrudRepository<Categoria, String> {


    public Mono<Categoria> findByNombre(String nombre); 
}
