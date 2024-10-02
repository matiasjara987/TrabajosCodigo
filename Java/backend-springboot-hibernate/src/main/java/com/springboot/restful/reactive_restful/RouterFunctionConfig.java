package com.springboot.restful.reactive_restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.restful.reactive_restful.handler.ProductoHandler;
import com.springboot.restful.reactive_restful.models.documents.Producto;
import com.springboot.restful.reactive_restful.service.IProductoCategoriaService;

/* Importamos de manera estatica para poder utilizar el metodo route sin tener que colocar ROuterFunctions */
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
/* Importación estatica para no tener que declarar requestPredicates para los metodos get, post, put, delete */
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterFunctionConfig {
/*
 *  Estos metodos son configuranciones y se anotan con @Bean    
 *  Estos metodos son los que tienen todas las rutas de nuestro API REST
 * Retorna un routerFunction con del tipo Server Response
 */

 @Autowired
 private IProductoCategoriaService service;
    @Bean
    /* estamos configurando y registrando las rutas de nuestra api */
    public RouterFunction<ServerResponse> routes(){
        /* Puede hacerse un mapeo a dos url de la siguiente forma .or(PRedicate/get/post/etc/("Url")) */
        return route(GET("/api/routes/productos").or(GET("/api/ruta/productos")), request  ->{
            return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            /* en el Body debemos decir el tipo que será el valor en este caso sería de la clase Producto  (Producto.class) */
            .body(service.findAll(), Producto.class);
        });
        /* Existe otra forma que sirve para poder desacoplar todo lo que sería del controlador de nuestras rutas (Todo lo que viene despues del return )  */
    }

/*Version desacoplada donde pasamos el parametro producthandler como argumento */
    @Bean
    public RouterFunction<ServerResponse> routerFunction(ProductoHandler handler){
                                                                  /* request -> handler.listar(request)  */
        return route(GET("/route/productos/desacoplado"), handler::listar)
                                                                    /*el metodo .and sirve para validar que nosotros recibimos el content type requerido  */
        /* Si en la petición no se envía un json y se envia x ejemplo un text dará error de not found ya que solo se permite json 
         * es una forma de validar en el backend*/
        .andRoute(GET("/route/productos/desacoplado/{id}"), handler::ver)
        .andRoute(POST("/route/productos/desacoplado/"), handler::crear)
        .andRoute(PUT("/route/productos/desacoplado/{id}"), handler::editar)
        .andRoute(DELETE("/route/productos/desacoplado/{id}"), handler::eliminar)
        .andRoute(POST("/route/productos/desacoplado/upload/{id}"), handler::upload)
        .andRoute(POST("/route/productos/desacoplado/crear"),handler::craerConFoto);
    }
}
