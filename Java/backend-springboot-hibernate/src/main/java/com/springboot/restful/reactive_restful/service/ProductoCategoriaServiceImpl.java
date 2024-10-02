package com.springboot.restful.reactive_restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.restful.reactive_restful.dao.CategoriaDao;
import com.springboot.restful.reactive_restful.dao.ProductoDao;
import com.springboot.restful.reactive_restful.models.documents.Categoria;
import com.springboot.restful.reactive_restful.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class ProductoCategoriaServiceImpl implements IProductoCategoriaService {
    @Autowired
    private ProductoDao pDao;

    @Autowired
    private CategoriaDao cDao;
    @Override
    public Flux<Producto> findAll() {
        return pDao.findAll();
    }

    @Override
    public Mono<Producto> findById(String id) {
        return pDao.findById(id);
    }

    @Override
    public Mono<Producto> saveProducto(Producto producto) {
        return pDao.save(producto);
    }

    @Override
    public Flux<Categoria> findAllCategoria() {
     return cDao.findAll();
    }

    
    @Override
    public Mono<Void> deleteC(Categoria categoria) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteP'");
    }

    @Override
    public Mono<Categoria> saveCategoria(Categoria categoria) {
        return cDao.save(categoria);
    }

    @Override
    public Mono<Void> deleteP(Producto producto) {
            return pDao.delete(producto);
    }

    @Override
    public Mono<Producto> findByNombre(String nombre) {
        return pDao.findByNombre(nombre);
    }

    @Override
    public Mono<Categoria> findCategoriaByNombre(String nombre) {
        return cDao.findByNombre(nombre);
    }

}
