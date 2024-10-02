package com.springboot.restful.reactive_restful.service;

import org.springframework.stereotype.Service;

import com.springboot.restful.reactive_restful.models.documents.Categoria;
import com.springboot.restful.reactive_restful.models.documents.Producto;
import reactor.core.publisher.*;

@Service
public interface IProductoCategoriaService {
 

public Flux<Producto> findAll();
public Mono<Producto> findById(String id);
public Mono<Producto> saveProducto(Producto producto);
public Flux<Categoria> findAllCategoria();
public Mono<Void> deleteP(Producto producto);
public Mono<Void> deleteC(Categoria categoria);
public Mono<Categoria> saveCategoria(Categoria categoria);
public Mono<Producto> findByNombre(String nombre);
public Mono<Categoria> findCategoriaByNombre(String nombre); 


}
