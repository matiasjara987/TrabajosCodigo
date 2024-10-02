package com.matiasjara.msvc.cursos.msvc.cursos.controllerCurso;

import com.matiasjara.msvc.cursos.msvc.cursos.logic.Validar;
import com.matiasjara.msvc.cursos.msvc.cursos.models.Usuario;
import com.matiasjara.msvc.cursos.msvc.cursos.models.entitiy.Cursos;
import com.matiasjara.msvc.cursos.msvc.cursos.services.CursoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/curso")
public class Controller {


    @Autowired
    private CursoService service;

    @Autowired
    private Validar v;

    @GetMapping("/listar")
    public List<Cursos> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Cursos> c =  service.findByIdWithUsers(id); //service.findById(id);
        if (c.isPresent()) {
            return ResponseEntity.ok(c.get());
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Cursos cursos, BindingResult result) {
        if (result.hasErrors()) {
            return v.validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cursos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Cursos cursos, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return v.validar(result);
        }
        Optional<Cursos> c = service.findById(id);
        if (c.isPresent()) {
            Cursos cdb = c.get();
            cdb.setNombre(cursos.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(cdb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Cursos> c = service.findById(id);
        if (c.isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /* Metodos para asignar un usuario a un curso */
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignar(@PathVariable(name = "cursoId") Long id, @RequestBody Usuario usuario){
        Optional<Usuario> o;
        try{
            o = service.asignarUsuario(usuario, id);

        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No existe el usuario por id: " + e.getMessage()));
        }
        if (o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crear(@PathVariable(name = "cursoId") Long id, @RequestBody Usuario usuario){
        Optional<Usuario> c;
        try{
            c = service.crearUsuario(usuario, id);

        } catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No se pudo crear el usuario, detalle: ") + e.getMessage());
        }
        if (c.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(c.get());
        }
    return  ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminar(@PathVariable(name = "cursoId") Long id, @RequestBody Usuario usuario){
        Optional<Usuario> c;
        try{c = service.eliminarUsuario(usuario, id);}
        catch (FeignException e){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje", "No se pudo eliminar el usuario, error: ") + e.getMessage());}
        if (c.isPresent()){return ResponseEntity.status(HttpStatus.CREATED).body(c.get());}
        return  ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/eliminar-Cursousuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuario (@PathVariable Long id){
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

}
