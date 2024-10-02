package com.matiasjara.spring.cloud.msvc.usuarios.service;

import com.matiasjara.spring.cloud.msvc.usuarios.entities.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Usuario guardar(Usuario usuario);
    void Eliminar(Long id);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> listarPorIds(Iterable<Long> ids);



}
