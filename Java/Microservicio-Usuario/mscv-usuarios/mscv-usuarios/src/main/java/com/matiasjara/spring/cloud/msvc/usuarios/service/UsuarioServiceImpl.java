package com.matiasjara.spring.cloud.msvc.usuarios.service;

import com.matiasjara.spring.cloud.msvc.usuarios.client.CursoClienteRest;
import com.matiasjara.spring.cloud.msvc.usuarios.entities.Usuario;
import com.matiasjara.spring.cloud.msvc.usuarios.repositories.UsuarioInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    UsuarioInterface dao;

    @Autowired
    CursoClienteRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
    return (List<Usuario>) dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
     return dao.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
    return dao.save(usuario);
    }

    @Override
    @Transactional
    public void Eliminar(Long id) {
        dao.deleteById(id);
        client.eliminarCursoUsuario(id);

    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return dao.findByEmail(email);
    }

    @Override
    public List<Usuario> listarPorIds(Iterable<Long> ids) {
        return (List<Usuario>) dao.findAllById(ids);
    }
}
