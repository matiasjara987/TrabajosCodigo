package com.matiasjara.spring.cloud.msvc.usuarios.repositories;

import com.matiasjara.spring.cloud.msvc.usuarios.entities.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UsuarioInterface extends CrudRepository<Usuario, Long> {


    public Optional<Usuario> findByEmail(String email);

}
