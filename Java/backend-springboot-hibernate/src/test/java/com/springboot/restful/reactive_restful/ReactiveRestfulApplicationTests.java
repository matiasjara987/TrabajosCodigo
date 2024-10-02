package com.springboot.restful.reactive_restful;

import java.util.Collections;
import java.util.List;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.springboot.restful.reactive_restful.models.documents.Categoria;
import com.springboot.restful.reactive_restful.models.documents.Producto;
import com.springboot.restful.reactive_restful.service.IProductoCategoriaService;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveRestfulApplicationTests {

	@Autowired
	private WebTestClient client;
	@Autowired
	private IProductoCategoriaService service;
	@SuppressWarnings("null")
	@Test
	void test() {
		client.get()
		.uri("/route/productos/desacoplado")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Producto.class)
		.consumeWith(response ->  {
			List<Producto> productos = response.getResponseBody();
			productos.forEach(p -> {
				System.out.println(p.getNombre());
			});
			Assertions.assertThat(productos.size()>1).isTrue();
		});
		// .hasSize(2);
	}

	@Test
	void ver(){
		Producto producto = service.findByNombre("Mouse Gamer").block(); 
		client.get()
		.uri("/route/productos/desacoplado/{id}", Collections.singletonMap("id", producto.getId()))
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Mouse Gamer");

	}
	@Test
	void crearTest(){
		Categoria categoria = service.findCategoriaByNombre("Muebles").block();
		Producto producto = new Producto("matias", 1000.00, categoria);
		client.post().uri("/route/productos/desacoplado/")
		.contentType(MediaType.APPLICATION_JSON) /* Para la request	 */
		.accept(MediaType.APPLICATION_JSON) /* diferencia entre el accept es para la response */
		.body(Mono.just(producto), producto.getClass())
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("matias")
		.jsonPath("$.categoria.nombre").isEqualTo("Muebles");
	} 

	@Test
	void editar(){
		Producto producto = service.findByNombre("Mouse Gamer").block();
		Categoria categoria = service.findCategoriaByNombre("Computacion").block();

		Producto productoEditado = new Producto("Logitech mouse", 200.32, categoria);
		client.put().uri("route/productos/desacoplado/{id}", Collections.singletonMap("id", producto.getId()))
		.contentType(MediaType.APPLICATION_JSON) /* Para la request	 */
		.accept(MediaType.APPLICATION_JSON) /* diferencia entre el accept es para la response */
		.body(Mono.just(productoEditado), producto.getClass())
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Logitech mouse");

	}
}
