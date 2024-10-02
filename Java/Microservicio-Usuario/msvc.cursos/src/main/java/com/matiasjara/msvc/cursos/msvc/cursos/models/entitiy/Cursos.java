package com.matiasjara.msvc.cursos.msvc.cursos.models.entitiy;

import com.matiasjara.msvc.cursos.msvc.cursos.models.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Cursos {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
@NotEmpty
private String nombre;
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
@JoinColumn(name = "curso_id")
/* un curso puede tener varios usuarios*/
private List<CursoUsuario> cursoUsuarios;

@Transient /* Es un campo no es parte del conteto JPA, solo se usará para poblar los datos de los usuarios(mscv usuario)*/
private List<Usuario> usuarios;

public Cursos(){
    cursoUsuarios = new ArrayList<>();
    usuarios = new ArrayList<>();
}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CursoUsuario> getCursoUsuarios() {
        return cursoUsuarios;
    }

    public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios) {
        this.cursoUsuarios = cursoUsuarios;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /* Metodo agregar usuarios al curso y eliminar*/
    public void addCursoUsuario(CursoUsuario cursoUsuario){
    cursoUsuarios.add(cursoUsuario); /* Agrega un nuevo curso usuario a la array*/
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario){
        cursoUsuarios.remove(cursoUsuario);
    }
}
