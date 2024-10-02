package client.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import client.client.handler.ProductoHandler;

@Configuration
public class RouterConfig {


    @Bean
    public RouterFunction<ServerResponse> routes(ProductoHandler handler){
        return route(GET("/client/producto"),handler::listar)
        .andRoute(GET("/client/producto/{id}"), handler::ver)
        .andRoute(POST("/client/producto/"), handler::crear)
        .andRoute(POST("/client/producto/{id}"), handler::editar)
        .andRoute(DELETE("/client/producto/{id}"), handler::eliminar)
        .andRoute(POST("/client/producto/upload/{id}"), handler::upload);
        

    }
}
