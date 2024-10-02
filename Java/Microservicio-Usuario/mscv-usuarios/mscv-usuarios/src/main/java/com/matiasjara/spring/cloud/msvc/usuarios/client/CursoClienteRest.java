package com.matiasjara.spring.cloud.msvc.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc.cursos", url = "localhost:8002")
public interface CursoClienteRest {

    @DeleteMapping("/eliminar-Cursousuario/{id}")
    void eliminarCursoUsuario (@PathVariable Long id);


}

