package com.matiasjara.msvc.cursos.msvc.cursos.repositories;

import com.matiasjara.msvc.cursos.msvc.cursos.models.entitiy.Cursos;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Cursos, Long> {
@Modifying
@Query("delete from CursoUsuario cu where cu.usuarioId =?1")
    void eliminarCursoUsuarioPorId(Long id);

}
