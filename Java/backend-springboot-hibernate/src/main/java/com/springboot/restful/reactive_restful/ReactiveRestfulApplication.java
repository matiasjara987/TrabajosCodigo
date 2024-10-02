package com.springboot.restful.reactive_restful;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.springboot.restful.reactive_restful.models.documents.Categoria;
import com.springboot.restful.reactive_restful.models.documents.Producto;
import com.springboot.restful.reactive_restful.service.IProductoCategoriaService;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveRestfulApplication  implements CommandLineRunner{

	@Autowired
	private IProductoCategoriaService service;

	@Autowired
	private ReactiveMongoTemplate template;

	private static final Logger log = LoggerFactory.getLogger(ReactiveRestfulApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ReactiveRestfulApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		template.dropCollection("productos").subscribe();
		template.dropCollection("categorias").subscribe();

		Categoria electronico =new Categoria("Electronico");
		Categoria deporte =new Categoria("Deporte");
		Categoria computacion =new Categoria("Computacion");
		Categoria muebles =new Categoria("Muebles");

		Flux.just(electronico, deporte, computacion, muebles)
		.flatMap(c -> {
			return service.saveCategoria(c);
		})
		.thenMany(
			Flux.just(
			 new Producto("Mouse Gamer", 1200.000, electronico),
			 new Producto("Camara Sony", 177.89, electronico),
			 new Producto("Notebook Hp Omen", 46.74, computacion),
			 new Producto("Mueble Rojo", 542.21, muebles),
			 new Producto("Bicicleta", 123.12,deporte)


		)
	.flatMap(Producto -> {
		Producto.setCreateAt(new Date());
		return service.saveProducto(Producto);
	})	


		) //fin then many
	.subscribe(Producto -> log.info("Insert: " + Producto.getId()));
	
	}

}
