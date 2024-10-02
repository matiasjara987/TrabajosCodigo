package client.client.handler;


import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;

import static org.springframework.http.MediaType.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import client.client.models.Producto;
import client.client.services.ProductoService;
import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

    @Autowired
    private ProductoService service;
    public Mono<ServerResponse> listar(ServerRequest request){
        return ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(service.findAll(), Producto.class);
    
    }

    
    public Mono<ServerResponse> ver(ServerRequest request){
        String id = request.pathVariable("id");
        return service.findByid(id).flatMap(p -> ServerResponse.ok().contentType(APPLICATION_JSON).bodyValue(p))
        .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> crear(ServerRequest request) {
        Mono<Producto> producto = request.bodyToMono(Producto.class);
        return producto.flatMap(p -> {
            if (p.getCreateAt() == null) {
                p.setCreateAt(new Date());
            }
            return service.save(p);
        }).flatMap(p -> ServerResponse.created(URI.create("/client/producto".concat(p.getId())))
        .contentType(APPLICATION_JSON).bodyValue(p))
        .onErrorResume(error -> {
        WebClientResponseException errorResponse = (WebClientResponseException) error;
        if (errorResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(errorResponse.getResponseBodyAsString());   
        }
        return Mono.error(errorResponse);
    });
    }
    public Mono<ServerResponse> editar(ServerRequest request){
        Mono<Producto> producto = request.bodyToMono(Producto.class);
        String id = request.pathVariable("id");
        return producto.flatMap(p -> ServerResponse.created(URI.create("/client/producto".concat(id))).contentType(APPLICATION_JSON).body(service.update(p, id), Producto.class));
    }

    public Mono<ServerResponse> eliminar(ServerRequest request) {
        String id = request.pathVariable("id");
        return service.delete(id).then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> upload (ServerRequest request){
        String id = request.pathVariable("id");
        return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file")).cast(FilePart.class).flatMap(file -> service.upload(file, id)).flatMap(p -> ServerResponse.created(URI.create("/route/productos/desacoplado/upload/".concat(p.getId()))).contentType(APPLICATION_JSON).bodyValue(p));

    }
}
