package client.client.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import static org.springframework.http.MediaType.*;

import client.client.models.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImp implements ProductoService{
    @Autowired
    private WebClient client;
  
    @Override
    public Flux<Producto> findAll() {
    return client.get()
    .retrieve()
    .bodyToFlux(Producto.class);
}

    @Override
    public Mono<Producto> findByid(String id) {
        return client.get().uri("/{id}", Collections.singletonMap("id", id))
        .accept(APPLICATION_JSON)
        .exchangeToMono(r -> r.bodyToMono(Producto.class));
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return client.post()
        .accept(APPLICATION_JSON)
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(producto))
        .retrieve()
        .bodyToMono(Producto.class);
    }

    @Override
    public Mono<Producto> update(Producto producto, String id) {
        return client.put()

        .uri("/{id}", Collections.singletonMap("id", id))
        .accept(APPLICATION_JSON)
        .contentType(APPLICATION_JSON)
        .bodyValue(producto)
        .retrieve()
        .bodyToMono(Producto.class);
    }
    

    @Override
    public Mono<Void> delete(String id) {
        return client.delete().uri("{id}", Collections.singletonMap("id", id))
        .retrieve()
        .toBodilessEntity().then();
    }

    @Override
    public Mono<Producto> upload(FilePart file, String id) {
        /* Para poder hacer un request multipart debemos utilizar la clase Multipartbodybuilder */
        MultipartBodyBuilder parts = new MultipartBodyBuilder(); 
        parts.asyncPart("file", file.content(), DataBuffer.class).headers(h ->{
            h.setContentDispositionFormData("file", file.filename());
        });
        
        return client.post()
        .uri("/route/productos/desacoplado/upload/{id}", Collections.singletonMap("id", id))
        .contentType(MULTIPART_FORM_DATA)
        .bodyValue(parts.build())
        .retrieve()
        .bodyToMono(Producto.class);
    }

}
