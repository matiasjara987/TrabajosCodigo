package com.matiasjara.msvc.cursos.msvc.cursos.models.entitiy;

import jakarta.persistence.*;

@Entity
@Table(name="cursos_usuarios")
public class CursoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", unique = true)
    private Long usuarioId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    /*Como en java las instaancias son unicas el metodo remove nos daría error al no encontrar un parecido, lo que
    *                                               debemos hacer es modificar el metodo Equals para que compare los ids
    * (id == id)*/

    @Override
    public boolean equals(Object obj) {
       if (this == obj) {
           return true;
       }
       if (!(obj instanceof CursoUsuario)){
           return false;
       }
       CursoUsuario o = (CursoUsuario) obj;
        return this.usuarioId != null && this.usuarioId.equals(o.getUsuarioId());
    }
}
