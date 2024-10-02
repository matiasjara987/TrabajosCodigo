package com.springboot.restful.reactive_restful.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.restful.reactive_restful.models.documents.Categoria;
import com.springboot.restful.reactive_restful.models.documents.Producto;
import com.springboot.restful.reactive_restful.service.IProductoCategoriaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.springframework.web.reactive.function.BodyInserters.*;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.UUID; 

/* Es importante anotar con component no con controller aunqeu hará de controlador */
@Component
public class ProductoHandler {

 @Autowired
 private Validator validator;
    
 @Autowired
 private IProductoCategoriaService service;

 @Value("${config.uploads.path}")
 private String path;

    public Mono<ServerResponse> listar( ServerRequest request){
         return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            /* en el Body debemos decir el tipo que será el valor en este caso sería de la clase Producto  (Producto.class) */
            .body(service.findAll(), Producto.class);
    } 

    public Mono<ServerResponse> ver(ServerRequest request){
        /* Utilizamos flatmap en ves de map porque server response es el de tipo reactivo
         * anteriormente veniamos trabajando con map porque el responseentity no es del tipo reactivo y con el map se trabaja. 
         * ahora como es reactivo solo se utiliza flatmap para no realizar ninguna conversión
         */
        return service.findById(request.pathVariable("id")).flatMap(p -> {
            return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            /*COmo se esta guardando un objeto (p estatico debemos usar body inserters con from value) */
            .body(fromValue(p));
        }).switchIfEmpty(ServerResponse.notFound().build()); 
    
        /*  La diferencia entre los metodos utilizados en el body del metodo listar y el ver está en su logica
        en el metodo listar si podemos observar el body está recibiendo un flujo con la inyección de service por eso se utiliza el producto.class para declarar que el flujo es de productos
        EN CAMBIO el metodo ver está recibiendo con el metodo flatmap un objeto por eso se insertar mediante el metodo bodyinserters
        */

    }
    
    public Mono<ServerResponse> crear(ServerRequest request){
        Mono<Producto> producto = request.bodyToMono(Producto.class);
        return producto.flatMap(p -> {
        Errors errors = new BeanPropertyBindingResult(p, Producto.class.getName());
        validator.validate(p, errors);
            if (errors.hasErrors()) {
                return Flux.fromIterable(errors.getFieldErrors())
                .map(fielderror-> "el campo " + fielderror.getField() + " " + fielderror.getDefaultMessage())
                .collectList()
                .flatMap(list -> ServerResponse.badRequest().body(fromValue(list)));     
            } else {
                if(p.getCreateAt() == null) p.setCreateAt(new Date());
                return service.saveProducto(p).flatMap(pdb ->{
                    return ServerResponse
                    .created(URI.create("/route/productos/desacoplado/".concat(pdb.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromValue(pdb));
                    });
            }
            
            
        });
    }


    public Mono<ServerResponse> editar(ServerRequest request){
        Mono<Producto> producto = request.bodyToMono(Producto.class);
        Mono<Producto> productoDB = service.findById(request.pathVariable("id"));
        return productoDB.zipWith(producto, (db, req) -> {
            db.setNombre(req.getNombre());
            db.setPrecio(req.getPrecio());
            db.setCategoria(req.getCategoria());
            return db;
        }).flatMap(p ->{
            return  ServerResponse.created(URI.create("/route/productos/desacoplado/".concat(p.getId()))).contentType(MediaType.APPLICATION_JSON).body(service.saveProducto(p), Producto.class);
        }).switchIfEmpty(ServerResponse.notFound().build());

    } // fin editar

    public Mono<ServerResponse> eliminar(ServerRequest request){
        Mono<Producto> productoDB = service.findById(request.pathVariable("id"));
        return productoDB.flatMap(p -> service.deleteP(p).then(ServerResponse.noContent().build()))
        .switchIfEmpty(ServerResponse.notFound().build());
    } //fin eliminar 

    public Mono<ServerResponse> upload(ServerRequest request){
        return request.multipartData()
        .map(multipart -> multipart.toSingleValueMap().get("file"))
        .cast(FilePart.class)
        .flatMap(file -> service.findById(request.pathVariable("id"))
        .flatMap(p -> {
            p.setFoto(UUID.randomUUID().toString() +"-"+ file.filename()
            .replace(" ", "-")
            .replace(":", "")
            .replace("\\", ""));
            return file.transferTo(new File(path + p.getFoto())).then(service.saveProducto(p));
        })).flatMap(p -> ServerResponse.created(URI.create("/route/productos/desacoplado/".concat(p.getId()))).contentType(MediaType.APPLICATION_JSON).body(fromValue(p)).switchIfEmpty(ServerResponse.notFound().build()));
        
    }

    public Mono<ServerResponse> craerConFoto(ServerRequest request){
        Mono<Producto> producto = request.multipartData().map(multipart -> {
            FormFieldPart nombre = (FormFieldPart) multipart.toSingleValueMap().get("nombre");
            FormFieldPart precio = (FormFieldPart) multipart.toSingleValueMap().get("precio");
            FormFieldPart categoriaId = (FormFieldPart) multipart.toSingleValueMap().get("Categoria.id");
            FormFieldPart categoriaNombre = (FormFieldPart) multipart.toSingleValueMap().get("Categoria.hombre");

            Categoria categoria = new Categoria(categoriaNombre.value());
            categoria.setId(categoriaId.value());
            return new Producto(nombre.value(), Double.parseDouble(precio.value()), categoria);
        });//fin fp

        return request.multipartData()
        .map(multipart -> multipart.toSingleValueMap().get("file"))
        .cast(FilePart.class)
        .flatMap(file -> producto
        .flatMap(p -> {
            p.setFoto(UUID.randomUUID().toString() +"-"+ file.filename()
            .replace(" ", "-")
            .replace(":", "")
            .replace("\\", ""));
            p.setCreateAt(new Date());
            return file.transferTo(new File(path + p.getFoto())).then(service.saveProducto(p));
        })).flatMap(p -> ServerResponse.created(URI.create("/route/productos/desacoplado/".concat(p.getId()))).contentType(MediaType.APPLICATION_JSON).body(fromValue(p)).switchIfEmpty(ServerResponse.notFound().build()));
        
    }
}

