package com.matiasjara.msvc.cursos.msvc.cursos.logic;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Map;

public interface Validar {

    ResponseEntity<Map<String, String>> validar(BindingResult result);
}
