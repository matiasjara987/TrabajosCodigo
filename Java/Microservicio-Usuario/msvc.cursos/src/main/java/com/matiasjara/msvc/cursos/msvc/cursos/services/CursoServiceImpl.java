package com.matiasjara.msvc.cursos.msvc.cursos.services;

import com.matiasjara.msvc.cursos.msvc.cursos.clients.UsuarioClientRest;
import com.matiasjara.msvc.cursos.msvc.cursos.models.Usuario;
import com.matiasjara.msvc.cursos.msvc.cursos.models.entitiy.CursoUsuario;
import com.matiasjara.msvc.cursos.msvc.cursos.models.entitiy.Cursos;
import com.matiasjara.msvc.cursos.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository dao;

    @Autowired
    private UsuarioClientRest client;
    @Override
    @Transactional(readOnly = true)
    public List<Cursos> findAll() {
        return (List<Cursos>) dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cursos> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional
    public Cursos save(Cursos curso) {
        return dao.save(curso);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        dao.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        dao.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Cursos> o = dao.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioMsvc = client.detalle(usuario.getId());
            Cursos curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            dao.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Cursos> o = dao.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioMsvc = client.crear(usuario);
            Cursos curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            dao.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Cursos> o = dao.findById(cursoId);
        if(o.isPresent()){
            Usuario usuarioMsvc = client.detalle(usuario.getId());
            Cursos curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.removeCursoUsuario(cursoUsuario);
            dao.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<Cursos>  findByIdWithUsers(Long id){
        Optional<Cursos> c = findById(id);
        if (c.isPresent()){
                Cursos cursos = c.get();
                if (!cursos.getCursoUsuarios().isEmpty()){
                    List<Long> ids = cursos.getCursoUsuarios().stream().map(CursoUsuario::getUsuarioId).toList();
                    List<Usuario> usuarios = client.obtenerAlumnosPorCurso(ids);
                    cursos.setUsuarios(usuarios);
                }
                return  Optional.of(cursos);
        }
        return Optional.empty();
    }

}
