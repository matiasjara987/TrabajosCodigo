package com.matiasjara.msvc.cursos.msvc.cursos.services;

import com.matiasjara.msvc.cursos.msvc.cursos.models.Usuario;
import com.matiasjara.msvc.cursos.msvc.cursos.models.entitiy.Cursos;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Cursos> findAll();
    Optional<Cursos> findById(Long id);
    Cursos save(Cursos curso);
    void delete(Long id);
    void eliminarCursoUsuarioPorId(Long id);

    Optional<Cursos>  findByIdWithUsers(Long id);

    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);
}
