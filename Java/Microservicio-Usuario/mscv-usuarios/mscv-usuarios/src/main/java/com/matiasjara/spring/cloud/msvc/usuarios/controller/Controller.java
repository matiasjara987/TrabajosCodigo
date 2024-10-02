package com.matiasjara.spring.cloud.msvc.usuarios.controller;

import com.matiasjara.spring.cloud.msvc.usuarios.entities.Usuario;
import com.matiasjara.spring.cloud.msvc.usuarios.logic.Validacion;
import com.matiasjara.spring.cloud.msvc.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    UsuarioService service;

    @Autowired
    Validacion validacion;

    @GetMapping("/listar")
    public List<Usuario> listar()
    {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id) {
        Optional<Usuario> u = service.findById(id);
        if (u.isPresent()) {
            return ResponseEntity.ok().body(u.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?>crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
     if (service.findByEmail(usuario.getEmail()).isPresent()){
         return ResponseEntity.badRequest().body(Collections.singletonMap("mensajes", "Ya existe un usuario con ese correo electronico"));
     }
        if (result.hasErrors()){
            return validacion.validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()){
            return validacion.validar(result);
        }

        Optional<Usuario> u = service.findById(id);
        if (u.isPresent()) {
            Usuario odb = u.get();
            if (usuario.getEmail().equalsIgnoreCase(odb.getEmail()) && service.findByEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensajes", "Ya existe un usuario con ese correo electronico"));
            }
            odb.setNombre(usuario.getNombre());
            odb.setApellido(usuario.getApellido());
            odb.setEmail(usuario.getEmail());
            odb.setPassword(usuario.getPassword());
            return ResponseEntity.ok().body(service.guardar(odb));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam  List<Long> ids){
        return ResponseEntity.ok().body(service.listarPorIds(ids));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> usuario = service.findById(id);
        if (usuario.isPresent()) {
            service.Eliminar(id);
            return ResponseEntity.noContent().build();  // HTTP 204 No Content
        }
        return ResponseEntity.notFound().build();  // HTTP 404 Not Found
    }


}
