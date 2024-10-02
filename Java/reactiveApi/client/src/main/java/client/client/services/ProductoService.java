package client.client.services;

import org.springframework.http.codec.multipart.FilePart;

import client.client.models.*;
import reactor.core.publisher.*;

public interface ProductoService {

    public Flux<Producto> findAll();
    public Mono<Producto> findByid(String id);

    public Mono<Producto> save(Producto producto);
    public Mono<Producto> update(Producto producto, String id);
    public Mono<Void> delete(String id);
    public Mono<Producto> upload (FilePart file, String id);

}
