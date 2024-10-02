package com.springboot.restful.reactive_restful.controllers;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.springboot.restful.reactive_restful.models.documents.Producto;
import com.springboot.restful.reactive_restful.service.IProductoCategoriaService;

import jakarta.validation.Valid;
import reactor.core.publisher.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoCategoriaService service;

    @Value("${config.uploads.path}")
    private String path;    

    @PostMapping("/upload/{id}")
    public Mono<ResponseEntity<Producto>> upload(@PathVariable String id, @RequestPart FilePart file){
        return service.findById(id).flatMap(p -> {
            p.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
            .replace(" ", "")
            .replace(":", "")
            .replace("\\", "")
            );
            return file.transferTo(new File(path + p.getFoto())).then(service.saveProducto(p));
        }).map(p ->  ResponseEntity.ok(p)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /* Otra forma de subir la foto */
    @PostMapping("/upload/v2")
    public  Mono<ResponseEntity<Producto>> crearConFoto(Producto producto, @RequestPart FilePart file){
                if(producto.getCreateAt()==null) {
                    producto.setCreateAt(new Date());
                }
                producto.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                .replace(" ", "")
                .replace(":", "")
                .replace("\\", "")
                );
     
                return file.transferTo(new File(path + producto.getFoto())).then(service.saveProducto(producto)).map(p -> 
                    ResponseEntity.created(URI.create("/api/productos/".concat(p.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(producto)
                );
            }
    
    /* Hay dos formas uno con un flux y otra con un Mono de un responseentity devovleivndo el flujo en el cuerpo de un responseentity */
    @GetMapping() 
public Flux<Producto>listar(){
    return service.findAll();
}

/* Opción dos con un mono de response entity */
@GetMapping("/opcion2")
public Mono<ResponseEntity<Flux<Producto>>> listarTodoMono(){
    /* Lo que hace el response entity es mandar un estado 200 mediante el método ok ademas guardamos en el cuerpo el fluz del producto
     * por defecto el ok devuelve 200, guarda lo que se paso dentro del okey y se mediatype es json.
     todo esto puede ser modificado
     */
    return Mono.just(ResponseEntity.ok()
    .contentType(MediaType.APPLICATION_JSON)
    .body(service.findAll())
    );
}
@GetMapping("/{id}")
public Mono<ResponseEntity<Producto>> verDetalle(@PathVariable String id){
    return service.findById(id).map(p -> ResponseEntity.ok().body(p)).defaultIfEmpty(ResponseEntity.notFound().build());
}
@PostMapping()
public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Producto> monoProducto) {
Map<String, Object> respuesta = new HashMap<String, Object>();
 return monoProducto.flatMap(producto ->{
    if (producto.getCreateAt()== null) {
        producto.setCreateAt(new Date());
    }

    return service.saveProducto(producto).map(p -> {
    respuesta.put("producto", p);
    respuesta.put("mensaje", "Producto creado con exito");
    respuesta.put("timestamp", new Date());
    return ResponseEntity
    .created(URI.create("/api/productos".concat(p.getId())))
    .contentType(MediaType.APPLICATION_JSON)
    .body(respuesta);
 });
}).onErrorResume(t -> {
        return Mono.just(t).cast(WebExchangeBindException.class)
        .flatMap(e -> Mono.just(e.getFieldErrors()))
            .flatMapMany(Flux::fromIterable)
            .map(fieldError-> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
            .collectList()
            .flatMap(list -> {
                respuesta.put("errors", list);
                return Mono.just(ResponseEntity.badRequest().body(respuesta));
            });
             
    }); 
  
    
}


@PutMapping("/{id}")
public Mono<ResponseEntity<Producto>> editar(@RequestBody Producto producto, @PathVariable String id){
    return service.findById(id).flatMap(p -> {
        p.setNombre(producto.getNombre());
        p.setPrecio(producto.getPrecio());
        p.setCategoria(producto.getCategoria());
    return service.saveProducto(p);
}).map(p -> ResponseEntity.created(URI.create("/api/producto/".concat(p.getId())))
.contentType(MediaType.APPLICATION_JSON)
.body(p))
.defaultIfEmpty(ResponseEntity.notFound().build());
}

@DeleteMapping("/{id}")
public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id) {
   return service.findById(id).flatMap(p -> {
    return service.deleteP(p)
    .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));

   }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
}

}
